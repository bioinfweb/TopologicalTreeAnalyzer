package info.bioinfweb.osrfilter.analysis;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;
import org.nfunk.jep.ParseException;

import info.bioinfweb.osrfilter.data.AnalysesData;
import info.bioinfweb.osrfilter.data.PairComparisonData;
import info.bioinfweb.osrfilter.data.TreeData;
import info.bioinfweb.osrfilter.data.TreeIdentifier;
import info.bioinfweb.osrfilter.data.UserExpression;
import info.bioinfweb.osrfilter.data.UserExpressions;
import info.bioinfweb.osrfilter.io.TreeIterator;
import info.bioinfweb.treegraph.document.undo.CompareTextElementDataParameters;



public class UserExpressionsManagerTest {
	private TreeData searchTreeDataByFileName(String fileName, Map<TreeIdentifier, TreeData> map) {
		for (TreeIdentifier identifier : map.keySet()) {
			if (fileName.equals(identifier.getFile().getName())) {
				return map.get(identifier);
			}
		}
		return null;
	}
	
	
	private TreeData searchTreeDataByTreeName(String treeName, Map<TreeIdentifier, TreeData> map) {
		for (TreeIdentifier identifier : map.keySet()) {
			if (treeName.equals(identifier.getName())) {
				return map.get(identifier);
			}
		}
		return null;
	}
	
	
	private <T> void assertStringUserValue(Map<String, Object> map, String name, String expectedValue) {
		Object userValue = map.get(name);
		assertNotNull(userValue);
		assertTrue(userValue instanceof String);
		assertEquals(expectedValue, (String)userValue);
	}
	
	
	private void assertDoubleUserValue(Map<String, Object> map, String name, double expectedValue) {
		Object userValue = map.get(name);
		assertNotNull(userValue);
		assertTrue(userValue instanceof Double);
		assertEquals(expectedValue, ((Double)userValue).doubleValue(), 0.000001);
	}
	
	
	@Test
	public void test_checkExpressions_order() throws ParseException {
		UserExpressions expressions = new UserExpressions();
		expressions.getExpressions().put("exp3", new UserExpression(false, "pairUserValue(\"exp2\")"));
		expressions.getExpressions().put("exp1", new UserExpression(false, "pairUserValue(\"exp0\")"));
		expressions.getExpressions().put("exp2", new UserExpression(false, "pairUserValue(\"exp0\") + pairUserValue(\"exp1\")"));
		expressions.getExpressions().put("exp0", new UserExpression(false, "m()"));

		new UserExpressionsManager().setExpressions(expressions);
		
		for (int i = 0; i < 4; i++) {
			assertEquals("exp" + i, expressions.getOrder().get(i));
		}
	}

	
	@Test
	public void test_checkExpressions_orderMultipleReferences() throws ParseException {
		UserExpressions expressions = new UserExpressions();
		expressions.getExpressions().put("ref0", new UserExpression(false, "2 * pairUserValue(\"referenced\")"));
		expressions.getExpressions().put("ref1", new UserExpression(false, "pairUserValue(\"referenced\") - 1"));
		expressions.getExpressions().put("referenced", new UserExpression(false, "m()"));
		expressions.getExpressions().put("ref2", new UserExpression(false, "pairUserValue(\"referenced\")"));

		new UserExpressionsManager().setExpressions(expressions);
		
		assertEquals("referenced", expressions.getOrder().get(0));
		for (int i = 1; i < 4; i++) {
			assertTrue(expressions.getOrder().get(i).startsWith("ref"));
		}
	}

	
	@Test
	public void test_checkExpressions_circularReferences() throws ParseException {
		try {
			UserExpressions expressions = new UserExpressions();
			expressions.getExpressions().put("exp0", new UserExpression(false, "pairUserValue(\"exp2\")"));
			expressions.getExpressions().put("exp1", new UserExpression(false, "pairUserValue(\"exp0\")"));
			expressions.getExpressions().put("exp2", new UserExpression(false, "pairUserValue(\"exp1\")"));

			new UserExpressionsManager().setExpressions(expressions);
		}
		catch (ParseException e) {
			assertEquals("Circular reference to user value \"exp2\".", e.getMessage());
		}
	}

	
	@Test
	public void test_checkExpressions_invalidParameterCount() throws ParseException {
		try {
			UserExpressions expressions = new UserExpressions();
			expressions.getExpressions().put("exp0", new UserExpression(false, "splits(0, 18)"));
			new UserExpressionsManager().setExpressions(expressions);
		}
		catch (ParseException e) {
			assertTrue(e.getMessage().startsWith("Function \"splits\" requires 1 parameter"));
		}
	}

	
	@Test
	public void test_checkExpressions_invalidParameterType() throws ParseException {
		try {
			UserExpressions expressions = new UserExpressions();
			expressions.getExpressions().put("exp0", new UserExpression(false, "splits(\"A\")"));
			new UserExpressionsManager().setExpressions(expressions);
		}
		catch (ParseException e) {
			assertEquals("Invalid parameter type. This function must have one numeric parameter when used to calculate pair data.", e.getMessage());
		}
	}

	
	@Test
	public void test_evaluateExpressions_treeDataFunction() throws IOException, Exception {
		AnalysesData analysesData = new AnalysesData();
		new TopologicalAnalyzer(new CompareTextElementDataParameters()).compareAll(10, 
				new TreeIterator("data/PolytomyWithSubtree.tre", "data/PolytomyOnlyLeaves.tre"), analysesData);
		
		UserExpressions expressions = new UserExpressions();
		expressions.getExpressions().put("treeTerminals", new UserExpression(true, "terminals()"));
		expressions.getExpressions().put("pairFirstTerminal", new UserExpression(false, "terminals(0)"));
		expressions.getExpressions().put("pairSecondTerminal", new UserExpression(false, "terminals(1)"));
		UserExpressionsManager manager = new UserExpressionsManager();
		manager.setExpressions(expressions);
		manager.evaluateExpressions(analysesData);
		
		assertEquals(6.0, (Double)searchTreeDataByFileName("PolytomyWithSubtree.tre", analysesData.getTreeMap()).getUserValues().get("treeTerminals"), 0.000001);
		assertEquals(6.0, (Double)searchTreeDataByFileName("PolytomyOnlyLeaves.tre", analysesData.getTreeMap()).getUserValues().get("treeTerminals"), 0.000001);
		
		Map<String, Object> pairUserValues = analysesData.getComparisonMap().values().iterator().next().getUserValues();
		assertEquals(6.0, (Double)pairUserValues.get("pairFirstTerminal"), 0.000001);
		assertEquals(6.0, (Double)pairUserValues.get("pairSecondTerminal"), 0.000001);
	}

	
	@Test
	public void test_compareAll_userExpression() throws Exception {
		AnalysesData analysesData = new AnalysesData();
		new TopologicalAnalyzer(new CompareTextElementDataParameters()).compareAll(10, 
				new TreeIterator("data/PolytomyWithSubtree.tre", "data/PolytomyOnlyLeaves.tre"), analysesData);
		
		UserExpressions expressions = new UserExpressions();
		expressions.getExpressions().put("testSplitsA", new UserExpression(false, "splits(0)"));
		expressions.getExpressions().put("testSplitsB", new UserExpression(false, "splits(1)"));
		expressions.getExpressions().put("testC", new UserExpression(false, "c(0) + c(1)"));
		expressions.getExpressions().put("testN", new UserExpression(false, "n(0) + n(1)"));
		expressions.getExpressions().put("testTerminals", new UserExpression(false, "terminals(0) + terminals(1)"));
		expressions.getExpressions().put("testMSharedTerminals", new UserExpression(false, "m() - sharedTerminals()"));
		expressions.getExpressions().put("testID", new UserExpression(false, "id(0) + \" \" + id(1)"));
		expressions.getExpressions().put("testUserValue", new UserExpression(false, "pairUserValue(\"testC\")"));
		expressions.getExpressions().put("treeUserValue", new UserExpression(true, "terminals()"));
		expressions.getExpressions().put("treeUserValueReference", new UserExpression(true, "2 * treeUserValue(\"treeUserValue\")"));
		expressions.getExpressions().put("treeUserValueReferenceFromPair0", new UserExpression(false, "treeUserValue(\"treeUserValue\", 0) + 1"));
		expressions.getExpressions().put("treeUserValueReferenceFromPair1", new UserExpression(false, "treeUserValue(\"treeUserValue\", 1) + 2"));
		expressions.getExpressions().put("min", new UserExpression(false, "min(18, -7, 2)"));
		expressions.getExpressions().put("max", new UserExpression(false, "max(18, -7, 2)"));
		expressions.getExpressions().put("sum", new UserExpression(false, "sum(18, 20, 2)"));
		expressions.getExpressions().put("product", new UserExpression(false, "product(2, 4, 3)"));
		expressions.getExpressions().put("arithMean", new UserExpression(false, "arithMean(6, 6, 3)"));
		expressions.getExpressions().put("geomMean", new UserExpression(false, "geomMean(2, 4, 2)"));
		expressions.getExpressions().put("harmMean", new UserExpression(false, "harmMean(2, 2, 3, 6, 6, 3)"));
		expressions.getExpressions().put("median", new UserExpression(false, "median(6, 256, 3)"));
		UserExpressionsManager manager = new UserExpressionsManager();
		manager.setExpressions(expressions);
		manager.evaluateExpressions(analysesData);

		assertEquals(1, analysesData.getComparisonMap().size());
		PairComparisonData comparison = analysesData.getComparisonMap().values().iterator().next();
		TopologicalAnalyzerTest.assertTreeComparison(comparison, 0, 1, 1, 2, 0, 6);
		
		assertDoubleUserValue(comparison.getUserValues(), "testSplitsA", 2.0);
		assertDoubleUserValue(comparison.getUserValues(), "testSplitsB", 2.0);
		assertDoubleUserValue(comparison.getUserValues(), "testC", 3.0);
		assertDoubleUserValue(comparison.getUserValues(), "testN", 1.0);
		assertDoubleUserValue(comparison.getUserValues(), "testTerminals", 12.0);
		assertDoubleUserValue(comparison.getUserValues(), "testMSharedTerminals", -6.0);
		assertStringUserValue(comparison.getUserValues(), "testID", "tree1 tree1");
		assertDoubleUserValue(comparison.getUserValues(), "testUserValue", 3.0);
		assertDoubleUserValue(comparison.getUserValues(), "treeUserValueReferenceFromPair0", 7.0);
		assertDoubleUserValue(comparison.getUserValues(), "treeUserValueReferenceFromPair1", 8.0);
		assertDoubleUserValue(comparison.getUserValues(), "min", -7.0);
		assertDoubleUserValue(comparison.getUserValues(), "max", 18.0);
		assertDoubleUserValue(comparison.getUserValues(), "sum", 40.0);
		assertDoubleUserValue(comparison.getUserValues(), "product", 24.0);
		assertDoubleUserValue(comparison.getUserValues(), "arithMean", 5.0);
		assertDoubleUserValue(comparison.getUserValues(), "geomMean", 4.0);
		assertDoubleUserValue(comparison.getUserValues(), "harmMean", 3.0);
		assertDoubleUserValue(comparison.getUserValues(), "median", 6.0);
		
		Map<String, Object> map = searchTreeDataByFileName("PolytomyWithSubtree.tre", analysesData.getTreeMap()).getUserValues();
		assertDoubleUserValue(map, "treeUserValue", 6.0);
		assertDoubleUserValue(map, "treeUserValueReference", 12.0);
		map = searchTreeDataByFileName("PolytomyOnlyLeaves.tre", analysesData.getTreeMap()).getUserValues();
		assertDoubleUserValue(map, "treeUserValue", 6.0);
		assertDoubleUserValue(map, "treeUserValueReference", 12.0);
	}

	
	@Test
	public void test_compareAll_iteratingOverUserExpression() throws Exception {
		AnalysesData analysesData = new AnalysesData();
		new TopologicalAnalyzer(new CompareTextElementDataParameters()).compareAll(10, 
				new TreeIterator("data/DifferentTerminalCount.nex"), analysesData);
		
		UserExpressions expressions = new UserExpressions();
		expressions.getExpressions().put("pairUserValue", new UserExpression(false, "abs(terminals(0) - terminals(1))"));
		expressions.getExpressions().put("minOfPairUserValues", new UserExpression(true, "minOfPairUserValues(\"pairUserValue\")"));
		expressions.getExpressions().put("sumOfPairUserValues", new UserExpression(true, "sumOfPairUserValues(\"pairUserValue\")"));
		expressions.getExpressions().put("medianOfPairUserValues", new UserExpression(true, "medianOfPairUserValues(\"pairUserValue\")"));
		expressions.getExpressions().put("arithMeanOfPairUserValues", new UserExpression(true, "arithMeanOfPairUserValues(\"pairUserValue\")"));
		UserExpressionsManager manager = new UserExpressionsManager();
		manager.setExpressions(expressions);
		manager.evaluateExpressions(analysesData);

		assertEquals(6, analysesData.getComparisonMap().size());
		
		// Assert comparison data:
		PairComparisonData comparison = TopologicalAnalyzerTest.searchComparisonByNames("tree0", "tree1", analysesData.getComparisonMap());
		assertDoubleUserValue(comparison.getUserValues(), "pairUserValue", 3.0);
		comparison = TopologicalAnalyzerTest.searchComparisonByNames("tree0", "tree2", analysesData.getComparisonMap());
		assertDoubleUserValue(comparison.getUserValues(), "pairUserValue", 2.0);
		comparison = TopologicalAnalyzerTest.searchComparisonByNames("tree0", "tree3", analysesData.getComparisonMap());
		assertDoubleUserValue(comparison.getUserValues(), "pairUserValue", 4.0);
		comparison = TopologicalAnalyzerTest.searchComparisonByNames("tree1", "tree2", analysesData.getComparisonMap());
		assertDoubleUserValue(comparison.getUserValues(), "pairUserValue", 1.0);
		comparison = TopologicalAnalyzerTest.searchComparisonByNames("tree1", "tree3", analysesData.getComparisonMap());
		assertDoubleUserValue(comparison.getUserValues(), "pairUserValue", 1.0);
		comparison = TopologicalAnalyzerTest.searchComparisonByNames("tree2", "tree3", analysesData.getComparisonMap());
		assertDoubleUserValue(comparison.getUserValues(), "pairUserValue", 2.0);

		// Assert tree data:
		Map<String, Object> map = searchTreeDataByTreeName("tree0", analysesData.getTreeMap()).getUserValues();
		assertDoubleUserValue(map, "minOfPairUserValues", 2.0);
		assertDoubleUserValue(map, "sumOfPairUserValues", 9.0);
		assertDoubleUserValue(map, "arithMeanOfPairUserValues", 3.0);
		assertDoubleUserValue(map, "medianOfPairUserValues", 3.0);
		
		map = searchTreeDataByTreeName("tree1", analysesData.getTreeMap()).getUserValues();
		assertDoubleUserValue(map, "minOfPairUserValues", 1.0);
		assertDoubleUserValue(map, "sumOfPairUserValues", 5.0);
		assertDoubleUserValue(map, "arithMeanOfPairUserValues", 5.0 / 3.0);
		assertDoubleUserValue(map, "medianOfPairUserValues", 1.0);
		
		map = searchTreeDataByTreeName("tree2", analysesData.getTreeMap()).getUserValues();
		assertDoubleUserValue(map, "minOfPairUserValues", 1.0);
		assertDoubleUserValue(map, "sumOfPairUserValues", 5.0);
		assertDoubleUserValue(map, "arithMeanOfPairUserValues", 5.0 / 3.0);
		assertDoubleUserValue(map, "medianOfPairUserValues", 2.0);
		
		map = searchTreeDataByTreeName("tree3", analysesData.getTreeMap()).getUserValues();
		assertDoubleUserValue(map, "minOfPairUserValues", 1.0);
		assertDoubleUserValue(map, "sumOfPairUserValues", 7.0);
		assertDoubleUserValue(map, "arithMeanOfPairUserValues", 7.0 / 3.0);
		assertDoubleUserValue(map, "medianOfPairUserValues", 2.0);
	}

	
	@Test
	public void test_compareAll_userExpression_invalidUserDataReference() throws IOException, Exception {
		try {
			UserExpressions expressions = new UserExpressions();
			expressions.getExpressions().put("testUserValue", new UserExpression(false, "pairUserValue(\"someValue\")"));
			new UserExpressionsManager().setExpressions(expressions);
		}
		catch (ParseException e) {
			assertEquals("Referenced user value \"someValue\" was not defined.", e.getMessage());
		}
	}
}
