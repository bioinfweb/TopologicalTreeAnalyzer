package info.bioinfweb.osrfilter.io;


import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.apache.commons.collections4.map.ListOrderedMap;
import org.junit.Test;

import info.bioinfweb.osrfilter.data.PairComparisonData;
import info.bioinfweb.osrfilter.data.TreeData;
import info.bioinfweb.osrfilter.data.TreeIdentifier;
import info.bioinfweb.osrfilter.data.TreePair;
import info.bioinfweb.osrfilter.data.parameters.ExportColumnList;



public class TableWriterTest {
	@Test
	public void test_writeTreeData() throws IOException {
		StringWriter writer = new StringWriter();
		
		ExportColumnList exportColumns = new ExportColumnList();
		exportColumns.getColumns().add("exp0");
		exportColumns.getColumns().add("exp1");
		
		Map<TreeIdentifier, TreeData> treeMap = new ListOrderedMap<TreeIdentifier, TreeData>();
		TreeData treeData = new TreeData();
		treeData.getUserValues().put("exp0", "abc");
		treeData.getUserValues().put("exp1", 1.0);
		treeMap.put(new TreeIdentifier(new File("data/Tree1.tre"), "tree0", "Tree A0"), treeData);
		treeData = new TreeData();
		treeData.getUserValues().put("exp0", "def");
		treeData.getUserValues().put("exp1", 2.0);
		treeMap.put(new TreeIdentifier(new File("data/Tree1.tre"), "tree1", "Tree A1"), treeData);
		treeData = new TreeData();
		treeData.getUserValues().put("exp0", "g");
		treeData.getUserValues().put("exp1", -3.5);
		treeMap.put(new TreeIdentifier(new File("data/Tree2.tre"), "tree0", "Tree B0"), treeData);
				
		new TableWriter().writeTreeData(writer, exportColumns, treeMap);
		
		final String cd = exportColumns.getColumnDelimiter();
		final String ld = exportColumns.getLineDelimiter();
		assertEquals(
				TableWriter.FILE_HEADING + cd + TableWriter.ID_HEADING + cd +	TableWriter.TREE_NAME_HEADING + cd + "exp0" + cd + "exp1" + ld +  
				"data" + File.separator + "Tree1.tre" + cd + "tree0" + cd + "Tree A0" + cd + "abc" + cd + "1.0" + ld + 
				"data" + File.separator + "Tree1.tre" + cd + "tree1" + cd + "Tree A1" + cd + "def" + cd + "2.0" + ld + 
				"data" + File.separator + "Tree2.tre" + cd + "tree0" + cd + "Tree B0" + cd + "g" + cd + "-3.5",
				writer.getBuffer().toString());
	}
	
	
	@Test
	public void test_writePairData() throws IOException {
		StringWriter writer = new StringWriter();
		
		ExportColumnList exportColumns = new ExportColumnList();
		exportColumns.getColumns().add("exp0");
		exportColumns.getColumns().add("exp1");
		
		Map<TreePair, PairComparisonData> comparisonMap = new ListOrderedMap<TreePair, PairComparisonData>();
		PairComparisonData comparisonData = new PairComparisonData();
		comparisonData.getUserValues().put("exp0", "abc");
		comparisonData.getUserValues().put("exp1", 1.0);
		comparisonMap.put(new TreePair(new TreeIdentifier(new File("data/Tree1.tre"), "tree0", null), 
				new TreeIdentifier(new File("data/Tree2.tre"), "tree0", null)), comparisonData);
				
		new TableWriter().writePairData(writer, exportColumns, comparisonMap);
		
		final String cd = exportColumns.getColumnDelimiter();
		final String ld = exportColumns.getLineDelimiter();
		assertEquals(
				TableWriter.FILE_HEADING + TableWriter.TREE_A_SUFFIX + cd + TableWriter.ID_HEADING + TableWriter.TREE_A_SUFFIX + cd +	
				TableWriter.TREE_NAME_HEADING + TableWriter.TREE_A_SUFFIX + cd + 
				TableWriter.FILE_HEADING + TableWriter.TREE_B_SUFFIX + cd + TableWriter.ID_HEADING + TableWriter.TREE_B_SUFFIX + cd +	
				TableWriter.TREE_NAME_HEADING + TableWriter.TREE_B_SUFFIX + cd + 
				"exp0" + cd + "exp1" + ld +  
				"data" + File.separator + "Tree1.tre" + cd + "tree0" + cd + cd + 
				"data" + File.separator + "Tree2.tre" + cd + "tree0" + cd + cd + 
				"abc" + cd + "1.0", 
				writer.getBuffer().toString());
	}
}
