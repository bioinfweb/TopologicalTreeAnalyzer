/*
 * Topological Tree Analyzer - User defined topological analyses of large sets of phylogenetic trees. 
 * Copyright (C) 2020-2021  Ben C. Stöver
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
package info.bioinfweb.tta.io.filter;


import static org.junit.Assert.*;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import info.bioinfweb.jphyloio.formats.JPhyloIOFormatIDs;
import info.bioinfweb.tta.analysis.AnalysisManager;
import info.bioinfweb.tta.analysis.TopologicalAnalyzerTest;
import info.bioinfweb.tta.analysis.UserExpressionsManagerTest;
import info.bioinfweb.tta.data.TreeIdentifier;
import info.bioinfweb.tta.data.TreeOrder;
import info.bioinfweb.tta.data.UserExpression;
import info.bioinfweb.tta.data.UserExpressions;
import info.bioinfweb.tta.data.UserValues;
import info.bioinfweb.tta.data.database.TreeUserDataTable;
import info.bioinfweb.tta.data.parameters.filter.NumericTreeFilterDefinition;
import info.bioinfweb.tta.data.parameters.filter.TreeFilterThreshold;



public class FilterTest {
//	@Test
//	public void testAbsoluteNumericFilter() {
//		NumericTreeFilterDefinition.Absolute definition = 
//				new NumericTreeFilterDefinition.Absolute("filter", "userValue", true, JPhyloIOFormatIDs.NEXML_FORMAT_ID);
//		definition.getThresholds().add(new TreeFilterThreshold(10, null));
//		definition.getThresholds().add(new TreeFilterThreshold(12, null));
//		
//		File file = new File("data/DifferentTerminalCount.nex");
//		
//		TreeIdentifier id0 = new TreeIdentifier(file, "tree0", null);
//		TreeIdentifier id1 = new TreeIdentifier(file, "tree1", null);
//		TreeIdentifier id2 = new TreeIdentifier(file, "tree2", null);
//		TreeIdentifier id3 = new TreeIdentifier(file, "tree3", null);
//		TreeIdentifier id4 = new TreeIdentifier(file, "tree4", null);
//		
//		Map<TreeIdentifier, TreeData> treeDataMap = new HashMap<TreeIdentifier, TreeData>();
//		treeDataMap.put(id0, createTreeData(18));
//		treeDataMap.put(id1, createTreeData(20));
//		treeDataMap.put(id2, createTreeData(2));
//		treeDataMap.put(id3, createTreeData(12));
//		treeDataMap.put(id4, createTreeData(7));
//		
//		TreeFilter<NumericTreeFilterDefinition.Absolute> filter = TreeFilterFactory.getInstance().createTreeFilter(definition, treeDataMap);
//
//		
//		assertTrue(filter.hasNext());
//		TreeFilterSet set = filter.next();
//		assertEquals("filter_10.0.nexml", set.getFileName());
//		assertEquals(2, set.getTrees().size());
//		assertTrue(set.getTrees().contains(id2));
//		assertTrue(set.getTrees().contains(id4));
//		
//		assertTrue(filter.hasNext());
//		set = filter.next();
//		assertEquals("filter_12.0.nexml", set.getFileName());
//		assertEquals(3, set.getTrees().size());
//		assertTrue(set.getTrees().contains(id2));
//		assertTrue(set.getTrees().contains(id3));
//		assertTrue(set.getTrees().contains(id4));
//		
//		assertFalse(filter.hasNext());
//	}
	
	
	private void putUserValue(TreeUserDataTable table, TreeIdentifier identifier, double value) throws SQLException {
		UserValues<TreeIdentifier> userValues = new UserValues<TreeIdentifier>(identifier);
		userValues.getUserValues().put("userValue", value);
		table.put(userValues);
	}
	
	
	@Test
	public void testRelativeNumericFilter() throws SQLException {
		NumericTreeFilterDefinition.Relative definition = 
				new NumericTreeFilterDefinition.Relative("filter", "userValue", true, JPhyloIOFormatIDs.NEXML_FORMAT_ID);
		definition.getThresholds().add(new TreeFilterThreshold(.4, null));
		definition.getThresholds().add(new TreeFilterThreshold(.6, null));
		
		File file = new File("data/DifferentTerminalCount.nex");
		
		TreeIdentifier id0 = new TreeIdentifier(file, "tree0", null);
		TreeIdentifier id1 = new TreeIdentifier(file, "tree1", null);
		TreeIdentifier id2 = new TreeIdentifier(file, "tree2", null);
		TreeIdentifier id3 = new TreeIdentifier(file, "tree3", null);
		TreeIdentifier id4 = new TreeIdentifier(file, "tree4", null);
		
		List<TreeIdentifier> treeOrderList = new ArrayList<TreeIdentifier>();
		treeOrderList.add(id0);
		treeOrderList.add(id1);
		treeOrderList.add(id2);
		treeOrderList.add(id3);
		treeOrderList.add(id4);
		TreeOrder treeOrder = new TreeOrder(treeOrderList);
		
		UserExpressions expressions = new UserExpressions();
		UserExpressionsManagerTest.putExpression("userValue", new UserExpression(true, "terminals()", Double.class), expressions);
		AnalysisManager.createUserValueDatabase(new File(TopologicalAnalyzerTest.DATABASE_FOLDER), expressions);
		
		Connection userDataConnection = DriverManager.getConnection(AnalysisManager.createDatabaseURL(new File(TopologicalAnalyzerTest.DATABASE_FOLDER), 
				AnalysisManager.USER_DATA_FILE_PREFIX));
		try {
			TreeUserDataTable table = new TreeUserDataTable(userDataConnection, treeOrder, expressions.treeUserValueNames());
			putUserValue(table, id0, 18);
			putUserValue(table, id1, 20);
			putUserValue(table, id2, 2);
			putUserValue(table, id3, 12);
			putUserValue(table, id4, 7);
			
			TreeFilter<NumericTreeFilterDefinition.Relative> filter = TreeFilterFactory.getInstance().createTreeFilter(definition, table);
			
			assertTrue(filter.hasNext());
			TreeFilterSet set = filter.next();
			assertEquals("filter_0.4.nexml", set.getFileName());
			assertEquals(2, set.getTrees().size());
			assertTrue(set.getTrees().contains(id2));
			assertTrue(set.getTrees().contains(id4));
			
			assertTrue(filter.hasNext());
			set = filter.next();
			assertEquals("filter_0.6.nexml", set.getFileName());
			assertEquals(3, set.getTrees().size());
			assertTrue(set.getTrees().contains(id2));
			assertTrue(set.getTrees().contains(id3));
			assertTrue(set.getTrees().contains(id4));
			
			assertFalse(filter.hasNext());
		}
		finally {
			userDataConnection.close();
			TopologicalAnalyzerTest.deleteDatabaseFiles();
		}
	}
	
	
//	@Test
//	public void testBooleanFilter() {
//		BooleanTreeFilterDefinition definition = 
//				new BooleanTreeFilterDefinition("filter", "userValue", JPhyloIOFormatIDs.NEXML_FORMAT_ID);
//		
//		File file = new File("data/DifferentTerminalCount.nex");
//		
//		TreeIdentifier id0 = new TreeIdentifier(file, "tree0", null);
//		TreeIdentifier id1 = new TreeIdentifier(file, "tree1", null);
//		TreeIdentifier id2 = new TreeIdentifier(file, "tree2", null);
//		TreeIdentifier id3 = new TreeIdentifier(file, "tree3", null);
//		TreeIdentifier id4 = new TreeIdentifier(file, "tree4", null);
//		
//		Map<TreeIdentifier, TreeData> treeDataMap = new HashMap<TreeIdentifier, TreeData>();
//		treeDataMap.put(id0, createTreeData(false));
//		treeDataMap.put(id1, createTreeData(true));
//		treeDataMap.put(id2, createTreeData(false));
//		treeDataMap.put(id3, createTreeData(true));
//		treeDataMap.put(id4, createTreeData(false));
//		
//		TreeFilter<BooleanTreeFilterDefinition> filter = TreeFilterFactory.getInstance().createTreeFilter(definition, treeDataMap);
//
//		
//		assertTrue(filter.hasNext());
//		TreeFilterSet set = filter.next();
//		assertEquals("filter.nexml", set.getFileName());
//		assertEquals(2, set.getTrees().size());
//		assertTrue(set.getTrees().contains(id1));
//		assertTrue(set.getTrees().contains(id3));
//		
//		assertFalse(filter.hasNext());
//	}
}
