/*
 * Topological Tree Analyzer - User defined topological analyses of large sets of phylogenetic trees. 
 * Copyright (C) 2020  Ben C. Stöver
 * <http://bioinfweb.info/TTA>
 * 
 * This file is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package info.bioinfweb.tta.analysis;


import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import org.junit.Test;
import org.nfunk.jep.ParseException;

import info.bioinfweb.tta.data.AnalysesData;
import info.bioinfweb.tta.data.TreeData;
import info.bioinfweb.tta.data.TreeIdentifier;
import info.bioinfweb.tta.data.TreePair;
import info.bioinfweb.tta.data.UserExpression;
import info.bioinfweb.tta.data.UserExpressions;
import info.bioinfweb.tta.data.UserValues;
import info.bioinfweb.tta.data.database.DatabaseIterator;



public class UserExpressionsManagerTest {
	private TreeIdentifier identifierByName(String name, AnalysesData analysesData) {
		for (TreeIdentifier identifier : analysesData.getInputOrder()) {
			if (name.equals(identifier.getName())) {
				return identifier;
			}
		}
		return null;
	}
	
	
	private TreeIdentifier identifierByFileName(String fileName, AnalysesData analysesData) {
		for (TreeIdentifier identifier : analysesData.getInputOrder()) {
			if (fileName.equals(identifier.getFile().getName())) {
				return identifier;
			}
		}
		return null;
	}
	
	
	private TreeData searchTreeDataByFileName(String fileName, AnalysesData analysesData) throws SQLException {
		return analysesData.getTreeData().get(identifierByFileName(fileName, analysesData));
	}
	
	
	private UserValues<TreeIdentifier> searchTreeUserDataByFileName(String fileName, AnalysesData analysesData) throws SQLException {
		return analysesData.getTreeUserData().get(identifierByFileName(fileName, analysesData));
	}
	
	
	private UserValues<TreeIdentifier> searchTreeUserDataByName(String name, AnalysesData analysesData) throws SQLException {
		return analysesData.getTreeUserData().get(identifierByName(name, analysesData));
	}
	
	
	private TreeData searchTreeDataByTreeName(String treeName, Map<TreeIdentifier, TreeData> map) {
		for (TreeIdentifier identifier : map.keySet()) {
			if (treeName.equals(identifier.getName())) {
				return map.get(identifier);
			}
		}
		return null;
	}
	
	
	private UserValues<TreePair> searchPairUserDataByNames(String name1, String name2, AnalysesData analysesData) throws SQLException {
		return analysesData.getPairUserDataValue(identifierByName(name1, analysesData), identifierByName(name2, analysesData));
	}
	
	
	private <T> void assertStringUserValue(UserValues<?> userValues, String name, String expectedValue) {
		Object userValue = userValues.getUserValue(name);
		assertNotNull(userValue);
		assertTrue(userValue instanceof String);
		assertEquals(expectedValue, (String)userValue);
	}
	
	
	private void assertDoubleUserValue(UserValues<?> userValues, String name, double expectedValue) {
		Object userValue = userValues.getUserValue(name);
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
	public void test_checkExpressions_invalidParameterCount() {
		try {
			UserExpressions expressions = new UserExpressions();
			expressions.getExpressions().put("exp0", new UserExpression(false, "splits(0, 18)"));
			new UserExpressionsManager().setExpressions(expressions);
			fail("No exception thrown.");
		}
		catch (ParseException e) {
			assertTrue(e.getMessage().startsWith("Function \"splits\" requires 1 parameter"));
		}
	}

	
	@Test
	public void test_checkExpressions_invalidParameterType() {
		try {
			UserExpressions expressions = new UserExpressions();
			expressions.getExpressions().put("exp0", new UserExpression(false, "splits(\"A\")"));
			new UserExpressionsManager().setExpressions(expressions);
			fail("No exception thrown.");
		}
		catch (ParseException e) {
			assertEquals("Invalid parameter type. This function must have one numeric parameter when used to calculate pair data.", e.getMessage());
		}
	}

	
	@Test
	public void test_evaluateExpressions_treeDataFunction() throws IOException, Exception {
		UserExpressions expressions = new UserExpressions();
		expressions.getExpressions().put("treeTerminals", new UserExpression(true, "terminals()"));
		expressions.getExpressions().put("pairFirstTerminal", new UserExpression(false, "terminals(0)"));
		expressions.getExpressions().put("pairSecondTerminal", new UserExpression(false, "terminals(1)"));
		UserExpressionsManager manager = new UserExpressionsManager();
		manager.setExpressions(expressions);  // Must be done before performCompareAll() and createUserValueDatabase() because both need the expression order list that is created in here.

		AnalysesData analysesData = TopologicalAnalyzerTest.performCompareAll(expressions, "data/PolytomyWithSubtree.tre", "data/PolytomyOnlyLeaves.tre");
		try {
			AnalysisManager.createUserValueDatabase(new File(TopologicalAnalyzerTest.DATABASE_FOLDER), expressions);
			manager.evaluateExpressions(analysesData);
			
			assertEquals(6.0, (Double)searchTreeUserDataByFileName("PolytomyWithSubtree.tre", analysesData).getUserValue("treeTerminals"), 0.000001);
			assertEquals(6.0, (Double)searchTreeUserDataByFileName("PolytomyOnlyLeaves.tre", analysesData).getUserValue("treeTerminals"), 0.000001);
			
			DatabaseIterator<TreePair, UserValues<TreePair>> iterator = analysesData.getPairUserData().valueIterator();
			assertTrue(iterator.hasNext());
			UserValues<TreePair> pairUserData = iterator.next();
			assertEquals(6.0, (Double)pairUserData.getUserValue("pairFirstTerminal"), 0.000001);
			assertEquals(6.0, (Double)pairUserData.getUserValue("pairSecondTerminal"), 0.000001);
			assertFalse(iterator.hasNext());
		}
		finally {
			analysesData.close();
			TopologicalAnalyzerTest.deleteDatabaseFiles();
		}
	}

	
	@Test
	public void test_compareAll_userExpression() throws Exception {
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
		manager.setExpressions(expressions);  // Must be done before performCompareAll() and createUserValueDatabase() because both need the expression order list that is created in here.

		AnalysesData analysesData = TopologicalAnalyzerTest.performCompareAll(expressions, "data/PolytomyWithSubtree.tre", "data/PolytomyOnlyLeaves.tre");
		try {
			AnalysisManager.createUserValueDatabase(new File(TopologicalAnalyzerTest.DATABASE_FOLDER), expressions);
			manager.evaluateExpressions(analysesData);
	
			DatabaseIterator<TreePair, UserValues<TreePair>> iterator = analysesData.getPairUserData().valueIterator();
			assertTrue(iterator.hasNext());
			UserValues<TreePair> pairUserData = iterator.next();
			assertDoubleUserValue(pairUserData, "testSplitsA", 2.0);
			assertDoubleUserValue(pairUserData, "testSplitsB", 2.0);
			assertDoubleUserValue(pairUserData, "testC", 3.0);
			assertDoubleUserValue(pairUserData, "testN", 1.0);
			assertDoubleUserValue(pairUserData, "testTerminals", 12.0);
			assertDoubleUserValue(pairUserData, "testMSharedTerminals", -6.0);
			assertStringUserValue(pairUserData, "testID", "tree1 tree1");
			assertDoubleUserValue(pairUserData, "testUserValue", 3.0);
			assertDoubleUserValue(pairUserData, "treeUserValueReferenceFromPair0", 7.0);
			assertDoubleUserValue(pairUserData, "treeUserValueReferenceFromPair1", 8.0);
			assertDoubleUserValue(pairUserData, "min", -7.0);
			assertDoubleUserValue(pairUserData, "max", 18.0);
			assertDoubleUserValue(pairUserData, "sum", 40.0);
			assertDoubleUserValue(pairUserData, "product", 24.0);
			assertDoubleUserValue(pairUserData, "arithMean", 5.0);
			assertDoubleUserValue(pairUserData, "geomMean", 4.0);
			assertDoubleUserValue(pairUserData, "harmMean", 3.0);
			assertDoubleUserValue(pairUserData, "median", 6.0);
			assertFalse(iterator.hasNext());

			UserValues<TreeIdentifier> treeUserData = searchTreeUserDataByFileName("PolytomyWithSubtree.tre", analysesData);
			assertDoubleUserValue(treeUserData, "treeUserValue", 6.0);
			assertDoubleUserValue(treeUserData, "treeUserValueReference", 12.0);
			treeUserData = searchTreeUserDataByFileName("PolytomyOnlyLeaves.tre", analysesData);
			assertDoubleUserValue(treeUserData, "treeUserValue", 6.0);
			assertDoubleUserValue(treeUserData, "treeUserValueReference", 12.0);
		}
		finally {
			analysesData.close();
			TopologicalAnalyzerTest.deleteDatabaseFiles();
		}
	}

	
	@Test
	public void test_compareAll_iteratingOverUserExpression() throws Exception {
		UserExpressions expressions = new UserExpressions();
		expressions.getExpressions().put("pairUserValue", new UserExpression(false, "abs(terminals(0) - terminals(1))"));
		expressions.getExpressions().put("minOfPairUserValues", new UserExpression(true, "minOfPairUserValues(\"pairUserValue\")"));
		expressions.getExpressions().put("sumOfPairUserValues", new UserExpression(true, "sumOfPairUserValues(\"pairUserValue\")"));
		expressions.getExpressions().put("medianOfPairUserValues", new UserExpression(true, "medianOfPairUserValues(\"pairUserValue\")"));
		expressions.getExpressions().put("arithMeanOfPairUserValues", new UserExpression(true, "arithMeanOfPairUserValues(\"pairUserValue\")"));
		UserExpressionsManager manager = new UserExpressionsManager();
		manager.setExpressions(expressions);

		AnalysesData analysesData = TopologicalAnalyzerTest.performCompareAll(expressions, "data/DifferentTerminalCount.nex");
		try {
			AnalysisManager.createUserValueDatabase(new File(TopologicalAnalyzerTest.DATABASE_FOLDER), expressions);
			manager.evaluateExpressions(analysesData);
	
			// Assert comparison data:
			assertDoubleUserValue(searchPairUserDataByNames("tree0", "tree1", analysesData), "pairUserValue", 3.0);
			assertDoubleUserValue(searchPairUserDataByNames("tree0", "tree2", analysesData), "pairUserValue", 2.0);
			assertDoubleUserValue(searchPairUserDataByNames("tree0", "tree3", analysesData), "pairUserValue", 4.0);
			assertDoubleUserValue(searchPairUserDataByNames("tree1", "tree2", analysesData), "pairUserValue", 1.0);
			assertDoubleUserValue(searchPairUserDataByNames("tree1", "tree3", analysesData), "pairUserValue", 1.0);
			assertDoubleUserValue(searchPairUserDataByNames("tree2", "tree3", analysesData), "pairUserValue", 2.0);
	
			// Assert tree data:
			UserValues<TreeIdentifier> treeUserData = searchTreeUserDataByName("tree0", analysesData);
			assertDoubleUserValue(treeUserData, "minOfPairUserValues", 2.0);
			assertDoubleUserValue(treeUserData, "sumOfPairUserValues", 9.0);
			assertDoubleUserValue(treeUserData, "arithMeanOfPairUserValues", 3.0);
			assertDoubleUserValue(treeUserData, "medianOfPairUserValues", 3.0);
			
			treeUserData = searchTreeUserDataByName("tree1", analysesData);
			assertDoubleUserValue(treeUserData, "minOfPairUserValues", 1.0);
			assertDoubleUserValue(treeUserData, "sumOfPairUserValues", 5.0);
			assertDoubleUserValue(treeUserData, "arithMeanOfPairUserValues", 5.0 / 3.0);
			assertDoubleUserValue(treeUserData, "medianOfPairUserValues", 1.0);
			
			treeUserData = searchTreeUserDataByName("tree2", analysesData);
			assertDoubleUserValue(treeUserData, "minOfPairUserValues", 1.0);
			assertDoubleUserValue(treeUserData, "sumOfPairUserValues", 5.0);
			assertDoubleUserValue(treeUserData, "arithMeanOfPairUserValues", 5.0 / 3.0);
			assertDoubleUserValue(treeUserData, "medianOfPairUserValues", 2.0);
			
			treeUserData = searchTreeUserDataByName("tree3", analysesData);
			assertDoubleUserValue(treeUserData, "minOfPairUserValues", 1.0);
			assertDoubleUserValue(treeUserData, "sumOfPairUserValues", 7.0);
			assertDoubleUserValue(treeUserData, "arithMeanOfPairUserValues", 7.0 / 3.0);
			assertDoubleUserValue(treeUserData, "medianOfPairUserValues", 2.0);
		}
		finally {
			analysesData.close();
			TopologicalAnalyzerTest.deleteDatabaseFiles();
		}
	}

	
	@Test
	public void test_compareAll_userExpression_invalidUserDataReference() throws IOException, Exception {
		try {
			UserExpressions expressions = new UserExpressions();
			expressions.getExpressions().put("testUserValue", new UserExpression(false, "pairUserValue(\"someValue\")"));
			new UserExpressionsManager().setExpressions(expressions);
			fail("No exception thrown.");
		}
		catch (ParseException e) {
			assertEquals("Referenced user value \"someValue\" was not defined.", e.getMessage());
		}
	}
}
