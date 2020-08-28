package info.bioinfweb.osrfilter.analysis;


import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import info.bioinfweb.osrfilter.data.TreeData;
import info.bioinfweb.osrfilter.data.TreeIdentifier;



public class TreeSorterTest {
	@Test
	public void test_sort() {
		final String userValueName = "userValue";
		
		Map<TreeIdentifier, TreeData> treeDataMap = new HashMap<TreeIdentifier, TreeData>();
		
		TreeData treeData = new TreeData();
		treeData.getUserValues().put(userValueName, -2.3);
		treeDataMap.put(new TreeIdentifier(new File("Tree1.nexml"), "tree0", "treeName0"), treeData);
		
		treeData = new TreeData();
		treeData.getUserValues().put(userValueName, 1);
		treeDataMap.put(new TreeIdentifier(new File("Tree1.nexml"), "tree1", "treeName1"), treeData);
		
		treeData = new TreeData();
		treeData.getUserValues().put(userValueName, 5.8);
		treeDataMap.put(new TreeIdentifier(new File("Tree1.nexml"), "tree2", "treeName2"), treeData);
		
		treeData = new TreeData();
		treeData.getUserValues().put(userValueName, 18.0);
		treeDataMap.put(new TreeIdentifier(new File("Tree1.nexml"), "tree3", "treeName3"), treeData);
		
		TreeSorter sorter = new TreeSorter();
		List<TreeIdentifier> list = sorter.sort(treeDataMap, userValueName, true);
		assertEquals(4, list.size());
		assertEquals("tree0", list.get(0).getID());
		assertEquals("tree1", list.get(1).getID());
		assertEquals("tree2", list.get(2).getID());
		assertEquals("tree3", list.get(3).getID());

		list = sorter.sort(treeDataMap, userValueName, false);
		assertEquals(4, list.size());
		assertEquals("tree3", list.get(0).getID());
		assertEquals("tree2", list.get(1).getID());
		assertEquals("tree1", list.get(2).getID());
		assertEquals("tree0", list.get(3).getID());
	}
}
