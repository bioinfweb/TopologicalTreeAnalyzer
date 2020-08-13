package info.bioinfweb.osrfilter.analysis;


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
import info.bioinfweb.osrfilter.analysis.calculation.SharedTerminalsFunction;
import info.bioinfweb.osrfilter.analysis.calculation.SplitsFunction;
import info.bioinfweb.osrfilter.analysis.calculation.TerminalsFunction;
import info.bioinfweb.osrfilter.analysis.calculation.UserValueFunction;
import info.bioinfweb.osrfilter.data.PairComparison;



public class UserExpressionsManager {
	private Map<String, Node> expressions = new HashMap<String, Node>();
	private List<String> expressionOrder;
	private UserExpressionData expressionData;
	private JEP jep;

	
	public UserExpressionsManager() {
		super();
		jep = createJEP();
	}


	private void addFunction(JEP parser, AbstractFunction function) {
		parser.addFunction(function.getName(), function);
	}
	
	
	private JEP createJEP() {
		JEP result = new JEP();
		expressionData = new UserExpressionData();
		
		result.addStandardConstants();
		result.addStandardFunctions();
		
		addFunction(result, new SplitsFunction(expressionData));
		addFunction(result, new MFunction(expressionData));
		addFunction(result, new NFunction(expressionData));
		addFunction(result, new CFunction(expressionData));
		addFunction(result, new TerminalsFunction(expressionData));
		addFunction(result, new SharedTerminalsFunction(expressionData));
		addFunction(result, new IDFunction(expressionData));
		addFunction(result, new NameFunction(expressionData));
		addFunction(result, new UserValueFunction(expressionData));
		
		return result;
	}


	public void addExpression(String name, String expression) throws ParseException {
		Node root = jep.parse(expression);
		
		//TODO An evaluation with test data should be done here already. Otherwise correct parameters will not be checked:
		//TODO Set test data
		jep.evaluate(root);
		
		expressions.put(name, root);
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
	}
	
	
	private Map<String, List<String>> determineDependencies() throws ParseException {  //TODO Also support other user functions referencing multiple trees later.
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		for (String name : expressions.keySet()) {
			List<String> dependencies = new ArrayList<String>();
			determineDependenciesInSubtree(expressions.get(name), dependencies);
			result.put(name, dependencies);
		}
		return result;
	}
	
	
	private void processDependecies(String name, Map<String, List<String>> dependencyMap, List<String> order) throws ParseException {
		List<String> dependencies = dependencyMap.remove(name);
		if (dependencies != null) {
			for (String dependency : dependencies) {
				processDependecies(dependency, dependencyMap, order);  // Add all direct and indirect dependencies before this one is added.
			}
			order.add(name);
		}
		else if (!order.contains(name)) {  // If order already contains the name it was processed before and not within this recursion and therefore a circular reference. Searching a map here (instead of an ordered set) is acceptable since the number of expressions will be limited and only a fraction of cases require a search.
			throw new ParseException("Circular reference to user value \"" + name + "\".");  //TODO Possibly add information on the parent reference or the whole circle?
		}
	}
	
	
	private List<String> sortExpressions(Map<String, List<String>> dependencyMap) throws ParseException {
		List<String> result = new ArrayList<String>();
		while (!dependencyMap.isEmpty()) {
			processDependecies(dependencyMap.keySet().iterator().next(), dependencyMap, result);
		}
		return result;
	}
	
	
	public void evaluateExpressions() throws ParseException {
		expressionOrder = sortExpressions(determineDependencies());
		
		for (String name : expressionOrder) {
			//jep.evaluate(expressions.get(name));  //TODO Loop this over all trees or pairs.
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
		UserExpressionData expressionData = new UserExpressionData();
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
