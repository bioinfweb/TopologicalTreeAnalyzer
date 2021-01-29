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
package info.bioinfweb.tta.io;


import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.apache.commons.collections4.map.ListOrderedMap;
import org.junit.Test;

import info.bioinfweb.tta.data.TreeData;
import info.bioinfweb.tta.data.TreeIdentifier;
import info.bioinfweb.tta.data.TreePair;
import info.bioinfweb.tta.data.parameters.ExportColumnList;
import info.bioinfweb.tta.io.UserValueTableWriter;



public class UserValueTableWriterTest {
//	@Test
//	public void test_writeTreeData() throws IOException {
//		StringWriter writer = new StringWriter();
//		
//		ExportColumnList exportColumns = new ExportColumnList();
//		exportColumns.getColumns().add("exp0");
//		exportColumns.getColumns().add("exp1");
//		
//		Map<TreeIdentifier, TreeData> treeMap = new ListOrderedMap<TreeIdentifier, TreeData>();
//		TreeData treeData = new TreeData();
//		treeData.getUserValues().put("exp0", "abc");
//		treeData.getUserValues().put("exp1", 1.0);
//		treeMap.put(new TreeIdentifier(new File("data/Tree1.tre"), "tree0", "Tree A0"), treeData);
//		treeData = new TreeData();
//		treeData.getUserValues().put("exp0", "def");
//		treeData.getUserValues().put("exp1", 2.0);
//		treeMap.put(new TreeIdentifier(new File("data/Tree1.tre"), "tree1", "Tree A1"), treeData);
//		treeData = new TreeData();
//		treeData.getUserValues().put("exp0", "g");
//		treeData.getUserValues().put("exp1", -3.5);
//		treeMap.put(new TreeIdentifier(new File("data/Tree2.tre"), "tree0", "Tree B0"), treeData);
//				
//		new UserValueTableWriter().writeTreeData(writer, exportColumns, treeMap);
//		
//		final String cd = exportColumns.getColumnDelimiter();
//		final String ld = exportColumns.getLineDelimiter();
//		assertEquals(
//				UserValueTableWriter.FILE_HEADING + cd + UserValueTableWriter.ID_HEADING + cd +	UserValueTableWriter.TREE_NAME_HEADING + cd + "exp0" + cd + "exp1" + ld +  
//				"data" + File.separator + "Tree1.tre" + cd + "tree0" + cd + "Tree A0" + cd + "abc" + cd + "1.0" + ld + 
//				"data" + File.separator + "Tree1.tre" + cd + "tree1" + cd + "Tree A1" + cd + "def" + cd + "2.0" + ld + 
//				"data" + File.separator + "Tree2.tre" + cd + "tree0" + cd + "Tree B0" + cd + "g" + cd + "-3.5",
//				writer.getBuffer().toString());
//	}
//	
//	
//	@Test
//	public void test_writePairData() throws IOException {
//		StringWriter writer = new StringWriter();
//		
//		ExportColumnList exportColumns = new ExportColumnList();
//		exportColumns.getColumns().add("exp0");
//		exportColumns.getColumns().add("exp1");
//		
//		Map<TreePair, PairComparisonData> comparisonMap = new ListOrderedMap<TreePair, PairComparisonData>();
//		PairComparisonData comparisonData = new PairComparisonData();
//		comparisonData.getUserValues().put("exp0", "abc");
//		comparisonData.getUserValues().put("exp1", 1.0);
//		comparisonMap.put(new TreePair(new TreeIdentifier(new File("data/Tree1.tre"), "tree0", null), 
//				new TreeIdentifier(new File("data/Tree2.tre"), "tree0", null)), comparisonData);
//				
//		new UserValueTableWriter().writePairData(writer, exportColumns, comparisonMap);
//		
//		final String cd = exportColumns.getColumnDelimiter();
//		final String ld = exportColumns.getLineDelimiter();
//		assertEquals(
//				UserValueTableWriter.FILE_HEADING + UserValueTableWriter.TREE_A_SUFFIX + cd + UserValueTableWriter.ID_HEADING + UserValueTableWriter.TREE_A_SUFFIX + cd +	
//				UserValueTableWriter.TREE_NAME_HEADING + UserValueTableWriter.TREE_A_SUFFIX + cd + 
//				UserValueTableWriter.FILE_HEADING + UserValueTableWriter.TREE_B_SUFFIX + cd + UserValueTableWriter.ID_HEADING + UserValueTableWriter.TREE_B_SUFFIX + cd +	
//				UserValueTableWriter.TREE_NAME_HEADING + UserValueTableWriter.TREE_B_SUFFIX + cd + 
//				"exp0" + cd + "exp1" + ld +  
//				"data" + File.separator + "Tree1.tre" + cd + "tree0" + cd + cd + 
//				"data" + File.separator + "Tree2.tre" + cd + "tree0" + cd + cd + 
//				"abc" + cd + "1.0", 
//				writer.getBuffer().toString());
//	}
}
