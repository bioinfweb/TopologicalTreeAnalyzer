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
	
	
	private <T> void assertStringUserValue(PairComparisonData comparison, String name, String expectedValue) {
		Object userValue = comparison.getUserValues().get(name);
		assertNotNull(userValue);
		assertTrue(userValue instanceof String);
		assertEquals(expectedValue, (String)userValue);
	}
	
	
	private void assertDoubleUserValue(PairComparisonData comparison, String name, double expectedValue) {
		Object userValue = comparison.getUserValues().get(name);
		assertNotNull(userValue);
		assertTrue(userValue instanceof Double);
		assertEquals(expectedValue, ((Double)userValue).doubleValue(), 0.000001);
	}
	
	
	@Test
	public void test_checkExpressions_order() throws ParseException {
		UserExpressionsManager manager = new UserExpressionsManager();
		manager.addExpression(false, "exp3", "userValue(\"exp2\")");
		manager.addExpression(false, "exp1", "userValue(\"exp0\")");
		manager.addExpression(false, "exp2", "userValue(\"exp0\") + userValue(\"exp1\")");
		manager.addExpression(false, "exp0", "m()");

		manager.checkExpressions();
		
		for (int i = 0; i < 4; i++) {
			assertEquals("exp" + i, manager.getExpressionOrder().get(i));
		}
	}

	
	@Test
	public void test_checkExpressions_orderMultipleReferences() throws ParseException {
		UserExpressionsManager manager = new UserExpressionsManager();
		manager.addExpression(false, "ref0", "2 * userValue(\"referenced\")");
		manager.addExpression(false, "ref1", "userValue(\"referenced\") - 1");
		manager.addExpression(false, "referenced", "m()");
		manager.addExpression(false, "ref2", "userValue(\"referenced\")");

		manager.checkExpressions();
		
		assertEquals("referenced", manager.getExpressionOrder().get(0));
		for (int i = 1; i < 4; i++) {
			assertTrue(manager.getExpressionOrder().get(i).startsWith("ref"));
		}
	}

	
	@Test(expected=ParseException.class)  //TODO Test expression more specifically
	public void test_checkExpressions_circularReferences() throws ParseException {
		UserExpressionsManager manager = new UserExpressionsManager();
		manager.addExpression(false, "exp0", "userValue(\"exp2\")");
		manager.addExpression(false, "exp1", "userValue(\"exp0\")");
		manager.addExpression(false, "exp2", "userValue(\"exp1\")");

		manager.checkExpressions();
	}

	
	@Test(expected=ParseException.class)  //TODO Test expression more specifically
	public void test_checkExpressions_invalidParameterCount() throws ParseException {
		UserExpressionsManager manager = new UserExpressionsManager();
		manager.addExpression(false, "exp0", "splits(0, 18)");
		manager.checkExpressions();
	}

	
	@Test(expected=ParseException.class)  //TODO Test expression more specifically
	public void test_checkExpressions_invalidParameterType() throws ParseException {
		UserExpressionsManager manager = new UserExpressionsManager();
		manager.addExpression(false, "exp0", "splits(\"A\")");
		manager.checkExpressions();
	}

	
	@Test
	public void test_evaluateExpressions_treeDataFunction() throws IOException, Exception {
		AnalysesData analysesData = new AnalysesData();
		new TopologicalAnalyzer(new CompareTextElementDataParameters()).compareAll(10, 
				new TreeIterator("data/PolytomyWithSubtree.tre", "data/PolytomyOnlyLeaves.tre"), analysesData);
		
		UserExpressionsManager manager = new UserExpressionsManager();
		manager.addExpression(true, "treeTerminals", "terminals()");
		manager.addExpression(false, "pairFirstTerminal", "terminals(0)");
		manager.addExpression(false, "pairSecondTerminal", "terminals(1)");
		manager.checkExpressions();
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
		
		UserExpressionsManager manager = new UserExpressionsManager();
		manager.addExpression(false, "testSplitsA", "splits(0)");
		manager.addExpression(false, "testSplitsB", "splits(1)");
		manager.addExpression(false, "testC", "c(0) + c(1)");
		manager.addExpression(false, "testN", "n(0) + n(1)");
		manager.addExpression(false, "testTerminals", "terminals(0) + terminals(1)");
		manager.addExpression(false, "testMSharedTerminals", "m() - sharedTerminals()");
		manager.addExpression(false, "testID", "id(0) + \" \" + id(1)");
		manager.addExpression(false, "testUserValue", "userValue(\"testC\")");
		manager.checkExpressions();
		manager.evaluateExpressions(analysesData);
		
		assertEquals(1, analysesData.getComparisonMap().size());
		PairComparisonData comparison = analysesData.getComparisonMap().values().iterator().next();
		TopologicalAnalyzerTest.assertTreeComparison(comparison, 0, 1, 1, 2, 0, 6);
		
		assertDoubleUserValue(comparison, "testSplitsA", 2.0);
		assertDoubleUserValue(comparison, "testSplitsB", 2.0);
		assertDoubleUserValue(comparison, "testC", 3.0);
		assertDoubleUserValue(comparison, "testN", 1.0);
		assertDoubleUserValue(comparison, "testTerminals", 12.0);
		assertDoubleUserValue(comparison, "testMSharedTerminals", -6.0);
		assertStringUserValue(comparison, "testID", "tree1 tree1");
		assertDoubleUserValue(comparison, "testUserValue", 3.0);  //TODO Whether this works depends in the order in the map. Either expressions need to be sorted by their dependencies or the map must store the order they were added.
	}


	@Test(expected=ParseException.class)  //TODO Test expression more specifically
	public void test_compareAll_userExpression_invalidUserDataReference() throws IOException, Exception {
		UserExpressionsManager manager = new UserExpressionsManager();
		manager.addExpression(false, "testUserValue", "userValue(\"someValue\")");
		manager.checkExpressions();
	}
}
