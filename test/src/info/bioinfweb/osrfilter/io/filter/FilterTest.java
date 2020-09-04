package info.bioinfweb.osrfilter.io.filter;


import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import info.bioinfweb.jphyloio.formats.JPhyloIOFormatIDs;
import info.bioinfweb.osrfilter.data.TreeData;
import info.bioinfweb.osrfilter.data.TreeIdentifier;
import info.bioinfweb.osrfilter.data.parameters.filter.BooleanTreeFilterDefinition;
import info.bioinfweb.osrfilter.data.parameters.filter.NumericTreeFilterDefinition;
import info.bioinfweb.osrfilter.data.parameters.filter.TreeFilterThreshold;



public class FilterTest {
	private TreeData createTreeData(Object value) {
		TreeData treeData = new TreeData();
		treeData.getUserValues().put("userValue", value);
		return treeData;
	}
	
	
	@Test
	public void testAbsoluteNumericFilter() {
		NumericTreeFilterDefinition.Absolute definition = 
				new NumericTreeFilterDefinition.Absolute("filter", "userValue", true, JPhyloIOFormatIDs.NEXML_FORMAT_ID);
		definition.getThresholds().add(new TreeFilterThreshold(10, null));
		definition.getThresholds().add(new TreeFilterThreshold(12, null));
		
		File file = new File("data/DifferentTerminalCount.nex");
		
		TreeIdentifier id0 = new TreeIdentifier(file, "tree0", null);
		TreeIdentifier id1 = new TreeIdentifier(file, "tree1", null);
		TreeIdentifier id2 = new TreeIdentifier(file, "tree2", null);
		TreeIdentifier id3 = new TreeIdentifier(file, "tree3", null);
		TreeIdentifier id4 = new TreeIdentifier(file, "tree4", null);
		
		Map<TreeIdentifier, TreeData> treeDataMap = new HashMap<TreeIdentifier, TreeData>();
		treeDataMap.put(id0, createTreeData(18));
		treeDataMap.put(id1, createTreeData(20));
		treeDataMap.put(id2, createTreeData(2));
		treeDataMap.put(id3, createTreeData(12));
		treeDataMap.put(id4, createTreeData(7));
		
		TreeFilter<NumericTreeFilterDefinition.Absolute> filter = TreeFilterFactory.getInstance().createTreeFilter(definition, treeDataMap);

		
		assertTrue(filter.hasNext());
		TreeFilterSet set = filter.next();
		assertEquals("filter_10.0", set.getFileName());
		assertEquals(2, set.getTrees().size());
		assertTrue(set.getTrees().contains(id2));
		assertTrue(set.getTrees().contains(id4));
		
		assertTrue(filter.hasNext());
		set = filter.next();
		assertEquals("filter_12.0", set.getFileName());
		assertEquals(3, set.getTrees().size());
		assertTrue(set.getTrees().contains(id2));
		assertTrue(set.getTrees().contains(id3));
		assertTrue(set.getTrees().contains(id4));
		
		assertFalse(filter.hasNext());
	}
	
	
	@Test
	public void testRelativeNumericFilter() {
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
		
		Map<TreeIdentifier, TreeData> treeDataMap = new HashMap<TreeIdentifier, TreeData>();
		treeDataMap.put(id0, createTreeData(18));
		treeDataMap.put(id1, createTreeData(20));
		treeDataMap.put(id2, createTreeData(2));
		treeDataMap.put(id3, createTreeData(12));
		treeDataMap.put(id4, createTreeData(7));
		
		TreeFilter<NumericTreeFilterDefinition.Relative> filter = TreeFilterFactory.getInstance().createTreeFilter(definition, treeDataMap);

		
		assertTrue(filter.hasNext());
		TreeFilterSet set = filter.next();
		assertEquals("filter_0.4", set.getFileName());
		assertEquals(2, set.getTrees().size());
		assertTrue(set.getTrees().contains(id2));
		assertTrue(set.getTrees().contains(id4));
		
		assertTrue(filter.hasNext());
		set = filter.next();
		assertEquals("filter_0.6", set.getFileName());
		assertEquals(3, set.getTrees().size());
		assertTrue(set.getTrees().contains(id2));
		assertTrue(set.getTrees().contains(id3));
		assertTrue(set.getTrees().contains(id4));
		
		assertFalse(filter.hasNext());
	}
	
	
	@Test
	public void testBooleanFilter() {
		BooleanTreeFilterDefinition definition = 
				new BooleanTreeFilterDefinition("filter", "userValue", JPhyloIOFormatIDs.NEXML_FORMAT_ID);
		
		File file = new File("data/DifferentTerminalCount.nex");
		
		TreeIdentifier id0 = new TreeIdentifier(file, "tree0", null);
		TreeIdentifier id1 = new TreeIdentifier(file, "tree1", null);
		TreeIdentifier id2 = new TreeIdentifier(file, "tree2", null);
		TreeIdentifier id3 = new TreeIdentifier(file, "tree3", null);
		TreeIdentifier id4 = new TreeIdentifier(file, "tree4", null);
		
		Map<TreeIdentifier, TreeData> treeDataMap = new HashMap<TreeIdentifier, TreeData>();
		treeDataMap.put(id0, createTreeData(false));
		treeDataMap.put(id1, createTreeData(true));
		treeDataMap.put(id2, createTreeData(false));
		treeDataMap.put(id3, createTreeData(true));
		treeDataMap.put(id4, createTreeData(false));
		
		TreeFilter<BooleanTreeFilterDefinition> filter = TreeFilterFactory.getInstance().createTreeFilter(definition, treeDataMap);

		
		assertTrue(filter.hasNext());
		TreeFilterSet set = filter.next();
		assertEquals("filter", set.getFileName());
		assertEquals(2, set.getTrees().size());
		assertTrue(set.getTrees().contains(id1));
		assertTrue(set.getTrees().contains(id3));
		
		assertFalse(filter.hasNext());
	}
}
