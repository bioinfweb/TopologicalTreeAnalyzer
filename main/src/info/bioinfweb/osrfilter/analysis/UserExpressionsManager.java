package info.bioinfweb.osrfilter.analysis;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nfunk.jep.ASTConstant;
import org.nfunk.jep.ASTFunNode;
import org.nfunk.jep.JEP;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;

import info.bioinfweb.osrfilter.analysis.calculation.AbstractFunction;
import info.bioinfweb.osrfilter.analysis.calculation.CFunction;
import info.bioinfweb.osrfilter.analysis.calculation.IDFunction;
import info.bioinfweb.osrfilter.analysis.calculation.MFunction;
import info.bioinfweb.osrfilter.analysis.calculation.NFunction;
import info.bioinfweb.osrfilter.analysis.calculation.NameFunction;
import info.bioinfweb.osrfilter.analysis.calculation.PairUserValueFunction;
import info.bioinfweb.osrfilter.analysis.calculation.SharedTerminalsFunction;
import info.bioinfweb.osrfilter.analysis.calculation.SplitsFunction;
import info.bioinfweb.osrfilter.analysis.calculation.TerminalsFunction;
import info.bioinfweb.osrfilter.analysis.calculation.TreeUserValueFunction;
import info.bioinfweb.osrfilter.analysis.calculation.UserValueFunction;
import info.bioinfweb.osrfilter.analysis.calculation.vararg.ArithmeticMeanCalculator;
import info.bioinfweb.osrfilter.analysis.calculation.vararg.GeometicMeanCalculator;
import info.bioinfweb.osrfilter.analysis.calculation.vararg.HarmonicMeanCalculator;
import info.bioinfweb.osrfilter.analysis.calculation.vararg.IteratingPairUserValueFunction;
import info.bioinfweb.osrfilter.analysis.calculation.vararg.MaxCalculator;
import info.bioinfweb.osrfilter.analysis.calculation.vararg.MedianCalculator;
import info.bioinfweb.osrfilter.analysis.calculation.vararg.MinCalculator;
import info.bioinfweb.osrfilter.analysis.calculation.vararg.ProductCalculator;
import info.bioinfweb.osrfilter.analysis.calculation.vararg.SumCalculator;
import info.bioinfweb.osrfilter.analysis.calculation.vararg.VarArgCalculator;
import info.bioinfweb.osrfilter.analysis.calculation.vararg.VarArgFunction;
import info.bioinfweb.osrfilter.data.AnalysesData;
import info.bioinfweb.osrfilter.data.PairComparisonData;
import info.bioinfweb.osrfilter.data.TreeData;
import info.bioinfweb.osrfilter.data.TreeIdentifier;
import info.bioinfweb.osrfilter.data.TreePair;
import info.bioinfweb.osrfilter.data.UserExpression;
import info.bioinfweb.osrfilter.data.UserExpressions;



public class UserExpressionsManager {
	private JEP jep;
	private UserExpressionDataProvider expressionDataProvider;
	private UserExpressions expressions; 

	
	public UserExpressionsManager() {
		super();
		jep = createJEP();
	}


	private void addFunction(JEP parser, AbstractFunction function) {
		parser.addFunction(function.getName(), function);
	}
	
	
	private <T> void addCalculator(JEP parser, VarArgCalculator<T> calculator) {
		addFunction(parser, new VarArgFunction<T>(expressionDataProvider, calculator));
		addFunction(parser, new IteratingPairUserValueFunction(expressionDataProvider, calculator));
	}
	
	
	private JEP createJEP() {
		JEP result = new JEP();
		expressionDataProvider = new UserExpressionDataProvider();
		
		result.addStandardConstants();
		result.addStandardFunctions();
		
		addFunction(result, new SplitsFunction(expressionDataProvider));
		addFunction(result, new MFunction(expressionDataProvider));
		addFunction(result, new NFunction(expressionDataProvider));
		addFunction(result, new CFunction(expressionDataProvider));
		addFunction(result, new TerminalsFunction(expressionDataProvider));
		addFunction(result, new SharedTerminalsFunction(expressionDataProvider));
		addFunction(result, new IDFunction(expressionDataProvider));
		addFunction(result, new NameFunction(expressionDataProvider));
		addFunction(result, new PairUserValueFunction(expressionDataProvider));
		addFunction(result, new TreeUserValueFunction(expressionDataProvider));
		
		addCalculator(result, new MinCalculator());
		addCalculator(result, new MaxCalculator());
		addCalculator(result, new SumCalculator());
		addCalculator(result, new ProductCalculator());
		addCalculator(result, new ArithmeticMeanCalculator());
		addCalculator(result, new GeometicMeanCalculator());
		addCalculator(result, new HarmonicMeanCalculator());
		addCalculator(result, new MedianCalculator());
		
		return result;
	}


	public void setExpressions(UserExpressions expressions) throws ParseException {
		this.expressions = expressions;
		for (UserExpression expression : expressions.getExpressions().values()) {
			expressionDataProvider.setTreeExpression(expression.hasTreeTarget());
			expression.setRoot(jep.parse(expression.getExpression()));
		}
		checkExpressions();
	}
	
	
	private void determineDependenciesInSubtree(Node root, List<String> dependencies) throws ParseException {
		if ((root instanceof ASTFunNode) && (((ASTFunNode)root).getPFMC() instanceof UserValueFunction)) {
			if (root.jjtGetNumChildren() >= 1) {
				Object value = ((ASTConstant)root.jjtGetChild(0)).getValue();  //TODO Can this have another type of the user value name is calculated? => ClassCastException.
				if (value instanceof String) {
					dependencies.add((String)value);
				}
				else {
					throw new ParseException("Invalid parameter type for user value function.");
				}
			}
			else {
				throw new ParseException("Invalid number of parameters for user value function.");
			}
		}
		else {
			for (int i = 0; i < root.jjtGetNumChildren(); i++) {
				determineDependenciesInSubtree(root.jjtGetChild(i), dependencies);
			}
		}
	}
	
	
	private Map<String, List<String>> determineDependencies() throws ParseException {  //TODO Also support other user functions referencing multiple trees later.
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		for (String name : expressions.getExpressions().keySet()) {
			List<String> dependencies = new ArrayList<String>();
			determineDependenciesInSubtree(expressions.getExpressions().get(name).getRoot(), dependencies);
			result.put(name, dependencies);
		}
		return result;
	}
	
	
	private void processDependecies(String name, Map<String, List<String>> dependencyMap) throws ParseException {
		List<String> dependencies = dependencyMap.remove(name);
		if (dependencies != null) {
			for (String dependency : dependencies) {
				processDependecies(dependency, dependencyMap);  // Add all direct and indirect dependencies before this one is added.
			}
			expressions.getOrder().add(name);
		}
		else if (!expressions.getExpressions().containsKey(name)) {
			throw new ParseException("Referenced user value \"" + name + "\" was not defined.");
		}
		else if (!expressions.getOrder().contains(name)) {  // If order already contains the name it was processed before and not within this recursion and therefore a circular reference. Searching a map here (instead of an ordered set) is acceptable since the number of expressions will be limited and only a fraction of cases require a search.
			throw new ParseException("Circular reference to user value \"" + name + "\".");  //TODO Possibly add information on the parent reference or the whole circle?
		}
	}
	
	
	private void sortExpressions(Map<String, List<String>> dependencyMap) throws ParseException {
		expressions.getOrder().clear();
		while (!dependencyMap.isEmpty()) {
			processDependecies(dependencyMap.keySet().iterator().next(), dependencyMap);
		}
	}
	
	
	private void checkExpressions() throws ParseException {
		sortExpressions(determineDependencies());
		
		AnalysesData analysesData = new AnalysesData();
		TreeIdentifier identifierA = new TreeIdentifier(new File("trees.tre"), "tree0", "treeName0");
		TreeIdentifier identifierB = new TreeIdentifier(new File("trees.tre"), "tree1", "treeName1");
		expressionDataProvider.setTreeIdentifier(0, identifierA);
		expressionDataProvider.setTreeIdentifier(1, identifierB);
		
		analysesData.getTreeMap().put(identifierA, new TreeData(7, 4));  //TODO Double check if this data is consistent.
		analysesData.getTreeMap().put(identifierB, new TreeData(7, 4));  //TODO Double check if this data is consistent.
		analysesData.getComparisonMap().put(new TreePair(identifierA, identifierB), new PairComparisonData(2, 1, 1, 1, 1, 6));  //TODO Double check if this data is consistent.
		expressionDataProvider.setAnalysesData(analysesData);
		
		// Evaluate all expressions once with test values to make sure parameter types and counts match.
		for (String name : expressions.getOrder()) {
			UserExpression expression = expressions.getExpressions().get(name);
			expressionDataProvider.setTreeExpression(expression.hasTreeTarget());
			Object value = jep.evaluate(expressions.getExpressions().get(name).getRoot());
			if (expression.hasTreeTarget()) {
				expressionDataProvider.getCurrentTreeData(0).getUserValues().put(name, value);
				expressionDataProvider.getCurrentTreeData(1).getUserValues().put(name, value);
			}
			else {
				expressionDataProvider.getCurrentComparisonData().getUserValues().put(name, value);
			}
		}
	}
	
	
	public void evaluateExpressions(AnalysesData analysesData) throws ParseException {
		//TODO Possibly parallelize this. Several instances of expressionDataProvider would be required then. Should also multiple JEP instances be used then? (The functions there reference expressionDataProvider.)
		if (expressions.isConsistent()) {
			expressionDataProvider.setAnalysesData(analysesData);
			for (String name : expressions.getOrder()) {
				UserExpression expression = expressions.getExpressions().get(name);
				expressionDataProvider.setTreeExpression(expression.hasTreeTarget());
				if (expression.hasTreeTarget()) {  // Calculate values for all trees:
					expressionDataProvider.setTreeIdentifier(1, null);
					for (TreeIdentifier identifier : expressionDataProvider.getAnalysesData().getTreeMap().keySet()) {
						expressionDataProvider.setTreeIdentifier(0, identifier);
						expressionDataProvider.getCurrentTreeData(0).getUserValues().put(name, jep.evaluate(expression.getRoot()));
					}
				}
				else {  // Calculate values for all pairs:
					for (TreePair pair : expressionDataProvider.getAnalysesData().getComparisonMap().keySet()) {
						expressionDataProvider.setTreeIdentifier(0, pair.getTreeA());
						expressionDataProvider.setTreeIdentifier(1, pair.getTreeB());
						expressionDataProvider.getCurrentComparisonData().getUserValues().put(name, jep.evaluate(expression.getRoot()));
					}
				}
			}
		}
		else {
			throw new IllegalStateException("Expression order has not been determined. Call checkExpressions() first.");
		}
	}
}
