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
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

import info.bioinfweb.commons.progress.VoidProgressMonitor;
import info.bioinfweb.treegraph.document.undo.CompareTextElementDataParameters;
import info.bioinfweb.tta.analysis.TopologicalAnalyzer;
import info.bioinfweb.tta.data.AnalysesData;
import info.bioinfweb.tta.data.PairComparisonData;
import info.bioinfweb.tta.data.TreeIdentifier;
import info.bioinfweb.tta.data.TreePair;
import info.bioinfweb.tta.data.parameters.ReferenceTreeDefinition;
import info.bioinfweb.tta.data.parameters.RuntimeParameters;
import info.bioinfweb.tta.test.VoidTopologicalWritingManager;



public class TopologicalAnalyzerTest {
	private AnalysesData performCompareAll(String... fileNames) throws IOException, Exception {
		return performCompareAll(RuntimeParameters.MAXIMUM, RuntimeParameters.MAXIMUM, fileNames);
	}
	
	
	private AnalysesData performCompareAll(long maxThreads, long maxMemory, String... fileNames) throws IOException, Exception {
		AnalysesData result = new AnalysesData();
		new TopologicalAnalyzer(new CompareTextElementDataParameters()).compareAll(new RuntimeParameters(maxThreads, maxMemory), fileNames, result, 
				new VoidTopologicalWritingManager(result), new VoidProgressMonitor());
		return result;
	}

	
	private AnalysesData performCompareWithReference(ReferenceTreeDefinition referenceTreeDefinition, String... fileNames) throws IOException, Exception {
		AnalysesData result = new AnalysesData();
		new TopologicalAnalyzer(new CompareTextElementDataParameters()).compareWithReference(
				referenceTreeDefinition.createTreeSelector(new File("").getAbsoluteFile()),  // No actual base directory required since all calls are made with absolute paths.
				fileNames, result, new VoidTopologicalWritingManager(result), new VoidProgressMonitor());
		return result;
	}

	
	public static void assertTreeComparison(PairComparisonData comparison, int expectedMatchingSplits, int expectedConflictingSplitsAB, 
			int expectedNotMatchingSplitsAB, int expectedConflictingSplitsBA, int expectedNotMatchingSplitsBA, int expectedSharedTerminal) {
		
		assertEquals(expectedMatchingSplits, comparison.getMatchingSplits());
		assertEquals(expectedConflictingSplitsAB, comparison.getConflictingSplitsAB());
		assertEquals(expectedNotMatchingSplitsAB, comparison.getNotMatchingSplitsAB());
		assertEquals(expectedConflictingSplitsBA, comparison.getConflictingSplitsBA());
		assertEquals(expectedNotMatchingSplitsBA, comparison.getNotMatchingSplitsBA());
		assertEquals(expectedSharedTerminal, comparison.getSharedTerminals());
	}
	
	
	public static PairComparisonData searchComparisonByNames(String name1, String name2, Map<TreePair, PairComparisonData> map) {
		for (TreePair pair : map.keySet()) {
			if ((name1.equals(pair.getTreeA().getName()) && name2.equals(pair.getTreeB().getName())) ||
					(name1.equals(pair.getTreeB().getName()) && name2.equals(pair.getTreeA().getName()))) {
				
				return map.get(pair);
			}
		}
		return null;
	}
	
	
	@SuppressWarnings("unused")
	private void assertNextTreeComparison(Iterator<PairComparisonData> iterator, int expectedMatchingSplits, int expectedConflictingSplitsAB, 
			int expectedNotMatchingSplitsAB,	int expectedConflictingSplitsBA, int expectedNotMatchingSplitsBA, int expectedSharedTerminal) {
		
		assertTrue(iterator.hasNext());
		assertTreeComparison(iterator.next(), expectedMatchingSplits, expectedConflictingSplitsAB, expectedNotMatchingSplitsAB, 
				expectedConflictingSplitsBA, expectedNotMatchingSplitsBA, expectedSharedTerminal);
	}
	
	
	@Test
	public void test_compareAll_asymmetricPair() throws IOException, Exception {
		Map<TreePair, PairComparisonData> map = performCompareAll("data/PolytomyWithSubtree.tre", "data/PolytomyOnlyLeaves.tre").getComparisonMap(); 
		assertEquals(1, map.size());
		assertTreeComparison(map.values().iterator().next(), 0, 1, 1, 2, 0, 6);
	}

	
	@Test
	public void test_compareAll_asymmetricPairWithAdditionalLeaves() throws IOException, Exception {
		Map<TreePair, PairComparisonData> map = performCompareAll("data/PolytomyWithSubtreeWithAdditionalLeaves.tre", 
				"data/PolytomyOnlyLeavesWithAdditionalLeaves.tre").getComparisonMap(); 
		assertEquals(1, map.size());
		assertTreeComparison(map.values().iterator().next(), 0, 1, 1, 2, 0, 6);
	}

	
	@Test
	public void test_compareAll_identicalInSharedLeafSet1() throws IOException, Exception {
		Map<TreePair, PairComparisonData> map = performCompareAll("data/PolytomyWithSubtree.tre", 
				"data/PolytomyWithSubtreeWithAdditionalLeaves.tre").getComparisonMap(); 
		assertEquals(1, map.size());
		assertTreeComparison(map.values().iterator().next(), 2, 0, 0, 0, 0, 6);
	}

	
	@Test
	public void test_compareAll_identicalInSharedLeafSet2() throws IOException, Exception {
		Map<TreePair, PairComparisonData> map = performCompareAll("data/PolytomyOnlyLeaves.tre", 
				"data/PolytomyOnlyLeavesWithAdditionalLeaves.tre").getComparisonMap(); 
		assertEquals(1, map.size());
		assertTreeComparison(map.values().iterator().next(), 2, 0, 0, 0, 0, 6);
	}

	
	@Test
	public void test_compareAll_rootSubtreeCount_2SubtreesNoLeavesVS3SubtreesNoLeaves() throws IOException, Exception {
		Map<TreePair, PairComparisonData> map = performCompareAll("data/RootWith2SubtreesNoLeaves.tre", 
				"data/RootWith3SubtreesNoLeaves.tre").getComparisonMap(); 
		assertEquals(1, map.size());
		assertTreeComparison(map.values().iterator().next(), 3, 0, 0, 0, 0, 6);
	}

	
	@Test
	public void test_compareAll_rootSubtreeCount_3SubtreesNoLeavesVS2Subtrees2Leaves() throws IOException, Exception {
		Map<TreePair, PairComparisonData> map = performCompareAll("data/RootWith3SubtreesNoLeaves.tre", 
				"data/RootWith2Subtrees2Leaves.tre").getComparisonMap(); 
		assertEquals(1, map.size());
		assertTreeComparison(map.values().iterator().next(), 2, 0, 1, 0, 0, 6);  // "RootWith2Subtrees2Leaves" has an actual polytomy on its root and is therefore not identical with the other tree.
	}

	
	@Test
	public void test_compareAll_rootSubtreeCount_2SubtreesNoLeavesVS2Subtrees2Leaves() throws IOException, Exception {
		Map<TreePair, PairComparisonData> map = performCompareAll("data/RootWith2SubtreesNoLeaves.tre", 
				"data/RootWith2Subtrees2Leaves.tre").getComparisonMap(); 
		assertEquals(1, map.size());
		assertTreeComparison(map.values().iterator().next(), 2, 0, 1, 0, 0, 6);  // "RootWith2Subtrees2Leaves" has an actual polytomy on its root and is therefore not identical with the other tree.
	}

	
//TODO Refactor this method to test multiple groups based on memory usage.
//	@Test
//	public void test_compareAll_multipleGroups() throws IOException, Exception {
//		File file = new File("data/SixTrees.nex");
//		AnalysesData analysesData = performCompareAll(4, file.getAbsolutePath());
//		
//		assertEquals(6, analysesData.getTreeCount());
//		assertEquals(6, analysesData.getInputOrder().size());
//		Iterator<TreeIdentifier> iterator = analysesData.getInputOrder().iterator();
//		assertEquals("tree0", iterator.next().getName());
//		assertEquals("tree1", iterator.next().getName());
//		assertEquals("tree2", iterator.next().getName());
//		assertEquals("tree3", iterator.next().getName());
//		assertEquals("tree4", iterator.next().getName());
//		assertEquals("tree5", iterator.next().getName());
//		
//		Map<TreePair, PairComparisonData> map = analysesData.getComparisonMap(); 
//		assertEquals(15, map.size());
//		
//		assertTreeComparison(searchComparisonByNames("tree0", "tree1", map), 0, 2, 0, 1, 1, 6);
//		assertTreeComparison(searchComparisonByNames("tree0", "tree2", map), 0, 2, 0, 2, 1, 6);
//		assertTreeComparison(searchComparisonByNames("tree0", "tree3", map), 0, 2, 0, 1, 1, 5);
//		assertTreeComparison(searchComparisonByNames("tree0", "tree4", map), 0, 2, 0, 1, 1, 5);
//		assertTreeComparison(searchComparisonByNames("tree0", "tree5", map), 0, 2, 0, 1, 1, 5);
//		
//		assertTreeComparison(searchComparisonByNames("tree1", "tree2", map), 2, 0, 0, 0, 1, 6);
//		assertTreeComparison(searchComparisonByNames("tree1", "tree3", map), 2, 0, 0, 0, 0, 5);
//		assertTreeComparison(searchComparisonByNames("tree1", "tree4", map), 2, 0, 0, 0, 0, 5);
//		assertTreeComparison(searchComparisonByNames("tree1", "tree5", map), 2, 0, 0, 0, 0, 5);
//
//		assertTreeComparison(searchComparisonByNames("tree2", "tree3", map), 2, 0, 0, 0, 0, 5);
//		assertTreeComparison(searchComparisonByNames("tree2", "tree4", map), 2, 0, 0, 0, 0, 5);
//		assertTreeComparison(searchComparisonByNames("tree2", "tree5", map), 2, 0, 0, 0, 0, 5);
//
//		assertTreeComparison(searchComparisonByNames("tree3", "tree4", map), 2, 0, 0, 0, 0, 5);
//		assertTreeComparison(searchComparisonByNames("tree3", "tree5", map), 2, 0, 0, 0, 0, 5);
//
//		assertTreeComparison(searchComparisonByNames("tree4", "tree5", map), 2, 0, 0, 0, 0, 5);
//	}

	
	private void testCompareWithReference(File file, ReferenceTreeDefinition referenceTreeDefinition) throws IOException, Exception {
		AnalysesData analysesData = performCompareWithReference(referenceTreeDefinition, file.getAbsolutePath());
		
		assertEquals(3, analysesData.getTreeCount());
		assertEquals(3, analysesData.getInputOrder().size());
		Iterator<TreeIdentifier> iterator = analysesData.getInputOrder().iterator();
		assertEquals("tree1", iterator.next().getID());
		assertEquals("tree2", iterator.next().getID());
		assertEquals("tree3", iterator.next().getID());
		
		Map<TreePair, PairComparisonData> map = analysesData.getComparisonMap(); 
		assertEquals(2, map.size());
		
		assertNotNull(map.get(new TreePair(new TreeIdentifier(file, "tree2", null), new TreeIdentifier(file, "tree1", null))));
		assertNotNull(map.get(new TreePair(new TreeIdentifier(file, "tree2", null), new TreeIdentifier(file, "tree3", null))));
		assertNull(map.get(new TreePair(new TreeIdentifier(file, "tree2", null), new TreeIdentifier(file, "tree2", null))));
		assertNull(map.get(new TreePair(new TreeIdentifier(file, "tree1", null), new TreeIdentifier(file, "tree3", null))));
	}

	
	@Test
	public void test_compareWithReference_ID() throws IOException, Exception {
		File file = new File("data/NeXMLTrees.xml");
		testCompareWithReference(file, new ReferenceTreeDefinition.IDReferenceTreeDefinition(file.getAbsolutePath(), "tree2"));
	}

	
	@Test
	public void test_compareWithReference_Name() throws IOException, Exception {
		File file = new File("data/NeXMLTrees.xml");
		testCompareWithReference(file, new ReferenceTreeDefinition.NameReferenceTreeDefinition(file.getAbsolutePath(), "Second tree"));
	}

	
	@Test
	public void test_compareWithReference_Index() throws IOException, Exception {
		File file = new File("data/NeXMLTrees.xml");
		testCompareWithReference(file, new ReferenceTreeDefinition.IndexReferenceTreeDefinition(file.getAbsolutePath(), 1));
	}
}
