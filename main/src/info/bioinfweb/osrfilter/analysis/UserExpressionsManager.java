package info.bioinfweb.osrfilter.analysis;


import java.util.ArrayList;
import java.util.Collections;
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
import info.bioinfweb.osrfilter.analysis.calculation.SharedTerminalsFunction;
import info.bioinfweb.osrfilter.analysis.calculation.SplitsFunction;
import info.bioinfweb.osrfilter.analysis.calculation.TerminalsFunction;
import info.bioinfweb.osrfilter.analysis.calculation.UserValueFunction;
import info.bioinfweb.osrfilter.data.PairComparison;



public class UserExpressionsManager {
	private Map<String, UserExpression> expressions;
	private List<String> expressionOrder;
	private List<String> unmodifiableExpressionOrder;
	private UserExpressionDataProvider expressionDataProvider;
	private JEP jep;

	
	public UserExpressionsManager() {
		super();
		jep = createJEP();
		expressions = new HashMap<String, UserExpression>();
		expressionOrder = new ArrayList<String>();
		unmodifiableExpressionOrder = Collections.unmodifiableList(expressionOrder);
	}


	private void addFunction(JEP parser, AbstractFunction function) {
		parser.addFunction(function.getName(), function);
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
		addFunction(result, new UserValueFunction(expressionDataProvider));
		
		return result;
	}


	public void addExpression(boolean hasTreeTarget, String name, String expression) throws ParseException {
		expressions.put(name, new UserExpression(hasTreeTarget, expression, jep.parse(expression)));
	}
	
	
	public List<String> getExpressionOrder() {
		return unmodifiableExpressionOrder;
	}


	private void determineDependenciesInSubtree(Node root, List<String> dependencies) throws ParseException {
		if ((root instanceof ASTFunNode) && (((ASTFunNode)root).getPFMC() instanceof UserValueFunction)) {
			if (root.jjtGetNumChildren() == 1) {
				Object value = ((ASTConstant)root.jjtGetChild(0)).getValue();
				if (value instanceof String) {
					dependencies.add((String)value);
				}
				else {
					throw new ParseException("Invalid parameter type. userValue() must have exactly one textual parameter.");  //TODO Unify this exception code with the code throwing exceptions in UserValueFunction.
				}
			}
			else {
				throw new ParseException("Invalid number of parameters. userValue() must have exactly one parameter.");  //TODO Unify this exception code with the code throwing exceptions in UserValueFunction.
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
		for (String name : expressions.keySet()) {
			List<String> dependencies = new ArrayList<String>();
			determineDependenciesInSubtree(expressions.get(name).getRoot(), dependencies);
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
			expressionOrder.add(name);
		}
		else if (!expressionOrder.contains(name)) {  // If order already contains the name it was processed before and not within this recursion and therefore a circular reference. Searching a map here (instead of an ordered set) is acceptable since the number of expressions will be limited and only a fraction of cases require a search.
			throw new ParseException("Circular reference to user value \"" + name + "\".");  //TODO Possibly add information on the parent reference or the whole circle?
		}
	}
	
	
	private void sortExpressions(Map<String, List<String>> dependencyMap) throws ParseException {
		expressionOrder.clear();
		while (!dependencyMap.isEmpty()) {
			processDependecies(dependencyMap.keySet().iterator().next(), dependencyMap);
		}
	}
	
	
	public void checkExpressions() throws ParseException {
		sortExpressions(determineDependencies());
		
		expressionDataProvider.setCurrentComparison(new PairComparison(2, 1, 1, 1, 1, 7, 7, 6, 4, 4));  //TODO Double check if this data is consistent.
		expressionDataProvider.setTreeID(0, "tree0");
		expressionDataProvider.setTreeID(1, "tree1");
		expressionDataProvider.setTreeName(0, "treeName0");
		expressionDataProvider.setTreeName(1, "treeName1");
		//TODO Provide user data in correct type. => This cannot really be done here. Evaluation would have to happen in checkExpressions() after sorting. User data could be set to the returned values there since the order matched dependencies.

		// Evaluate all expressions once with test values to make sure parameter types and counts match.
		for (String name : expressionOrder) {
			expressionDataProvider.getCurrentComparison().getUserValues().put(name, jep.evaluate(expressions.get(name).getRoot()));
		}
	}
	
	
	public void evaluateExpressions() throws ParseException {
		if (expressionOrder != null) {
			for (String name : expressionOrder) {
				UserExpression expression = expressions.get(name);
				if (expression.hasTreeTarget()) {
					//TODO Set respective functions and loop over all trees.
					//jep.evaluate(expressions.get(name));
				}
				else {
					//TODO Set respective functions and loop over all pairs.
				}
			}
		}
		else {
			throw new IllegalStateException("Expression order has not been determined. Call checkExpressions() first.");
		}
	}
		
	
//	// Calculate user-defined values:
//	for (String name : userExpressions.keySet()) {
//		expressionData.setCurrentComparison(result);
//		expressionData.setCurrentTreeA(tree1);
//		expressionData.setCurrentTreeB(tree2);
//
//		try {
//			parser.evaluate(parser.parseExpression(userExpressions.get(name)));  //TODO Evaluation only needs to be done once, not for every tree pair. (User value names are also identical for each pair.)
//			if (parser.hasError()) {
//				System.err.println(parser.getErrorInfo());  //TODO Replace with something more advanced.
//			}
//			else {
//				result.getUserValues().put(name, parser.getValueAsObject());
//			}
//		}
//		catch (ParseException e) {
//			System.err.println(e.getErrorInfo());  //TODO Replace with something more advanced when moved somewhere else. (See TODO above.)
//		}
//	}
	
	
	private static void printTree(Node root, String prefix) {
		System.out.print(prefix + root + " " + root.getClass().getCanonicalName());
		if ((root instanceof ASTFunNode) && (((ASTFunNode)root).getPFMC() instanceof UserValueFunction)) {
			System.out.println(" user function found->" + ((ASTConstant)root.jjtGetChild(0)).getValue());
		}
		else {
			System.out.println();
		}
		
		for (int i = 0; i < root.jjtGetNumChildren(); i++) {
			printTree(root.jjtGetChild(i), prefix + "  ");
		}
	}
	
	
	public static void main(String[] args) throws ParseException {
		JEP jep = new JEP();
		jep.addStandardConstants();
		jep.addStandardFunctions();
		
		PairComparison comparison = new PairComparison();
		UserExpressionDataProvider expressionData = new UserExpressionDataProvider();
		expressionData.setCurrentComparison(comparison);
		UserValueFunction function = new UserValueFunction(expressionData);
		jep.addFunction(function.getName(), function);
		
//		try {
//			Node root = jep.parse("userValue(\"someColumn\")");
//			printTree(root, "");
//		}
//		catch (ParseException e) {
//			e.printStackTrace();
//		}

//		comparison.getUserValues().put("someColumn", "value1");
//		jep.parseExpression("userValue(\"someColumn\")");
//		System.out.println(jep.getValueAsObject());
//
//		comparison.getUserValues().put("someColumn", "value2");
//		System.out.println(jep.getValueAsObject());

			Node root = jep.parse("userValue(\"someColumn\")");
			comparison.getUserValues().put("someColumn", "value1");
			System.out.println(jep.evaluate(root));
		
			comparison.getUserValues().put("someColumn", "value2");
			System.out.println(jep.evaluate(root));
	}
	
}
