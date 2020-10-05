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


import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import info.bioinfweb.tta.analysis.TreeSorter;
import info.bioinfweb.tta.data.TreeData;
import info.bioinfweb.tta.data.TreeIdentifier;



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
		
		List<TreeIdentifier> list = TreeSorter.sort(treeDataMap, userValueName, true);
		assertEquals(4, list.size());
		assertEquals("tree0", list.get(0).getID());
		assertEquals("tree1", list.get(1).getID());
		assertEquals("tree2", list.get(2).getID());
		assertEquals("tree3", list.get(3).getID());

		list = TreeSorter.sort(treeDataMap, userValueName, false);
		assertEquals(4, list.size());
		assertEquals("tree3", list.get(0).getID());
		assertEquals("tree2", list.get(1).getID());
		assertEquals("tree1", list.get(2).getID());
		assertEquals("tree0", list.get(3).getID());
	}
}
