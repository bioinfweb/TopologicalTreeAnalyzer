package info.bioinfweb.osrfilter.analysis;


import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

import info.bioinfweb.commons.progress.VoidProgressMonitor;
import info.bioinfweb.osrfilter.data.AnalysesData;
import info.bioinfweb.osrfilter.data.PairComparisonData;
import info.bioinfweb.osrfilter.data.TreeIdentifier;
import info.bioinfweb.osrfilter.data.TreePair;
import info.bioinfweb.treegraph.document.undo.CompareTextElementDataParameters;



public class TopologicalAnalyzerTest {
	private AnalysesData performCompareAll(int groupSize, String... fileNames) throws IOException, Exception {
		AnalysesData result = new AnalysesData();
		new TopologicalAnalyzer(new CompareTextElementDataParameters()).compareAll(groupSize, fileNames, result, 
				new VoidProgressMonitor());
		return result;
	}

	
	public static void assertTreeComparison(PairComparisonData comparison, int expectedMatchingSplits, int expectedConflictingSplitsAB, 
			int expectedNotMatchingSplitsAB,	int expectedConflictingSplitsBA, int expectedNotMatchingSplitsBA, int expectedSharedTerminal) {
		
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
		Map<TreePair, PairComparisonData> map = performCompareAll(10, "data/PolytomyWithSubtree.tre", "data/PolytomyOnlyLeaves.tre").getComparisonMap(); 
		assertEquals(1, map.size());
		assertTreeComparison(map.values().iterator().next(), 0, 1, 1, 2, 0, 6);
	}

	
	@Test
	public void test_compareAll_asymmetricPairWithAdditionalLeaves() throws IOException, Exception {
		Map<TreePair, PairComparisonData> map = performCompareAll(10, "data/PolytomyWithSubtreeWithAdditionalLeaves.tre", 
				"data/PolytomyOnlyLeavesWithAdditionalLeaves.tre").getComparisonMap(); 
		assertEquals(1, map.size());
		assertTreeComparison(map.values().iterator().next(), 0, 1, 1, 2, 0, 6);
	}

	
	@Test
	public void test_compareAll_identicalInSharedLeafSet1() throws IOException, Exception {
		Map<TreePair, PairComparisonData> map = performCompareAll(10, "data/PolytomyWithSubtree.tre", 
				"data/PolytomyWithSubtreeWithAdditionalLeaves.tre").getComparisonMap(); 
		assertEquals(1, map.size());
		assertTreeComparison(map.values().iterator().next(), 2, 0, 0, 0, 0, 6);
	}

	
	@Test
	public void test_compareAll_identicalInSharedLeafSet2() throws IOException, Exception {
		Map<TreePair, PairComparisonData> map = performCompareAll(10, "data/PolytomyOnlyLeaves.tre", 
				"data/PolytomyOnlyLeavesWithAdditionalLeaves.tre").getComparisonMap(); 
		assertEquals(1, map.size());
		assertTreeComparison(map.values().iterator().next(), 2, 0, 0, 0, 0, 6);
	}

	
	@Test
	public void test_compareAll_rootSubtreeCount_2SubtreesNoLeavesVS3SubtreesNoLeaves() throws IOException, Exception {
		Map<TreePair, PairComparisonData> map = performCompareAll(10, "data/RootWith2SubtreesNoLeaves.tre", 
				"data/RootWith3SubtreesNoLeaves.tre").getComparisonMap(); 
		assertEquals(1, map.size());
		assertTreeComparison(map.values().iterator().next(), 3, 0, 0, 0, 0, 6);
	}

	
	@Test
	public void test_compareAll_rootSubtreeCount_3SubtreesNoLeavesVS2Subtrees2Leaves() throws IOException, Exception {
		Map<TreePair, PairComparisonData> map = performCompareAll(10, "data/RootWith3SubtreesNoLeaves.tre", 
				"data/RootWith2Subtrees2Leaves.tre").getComparisonMap(); 
		assertEquals(1, map.size());
		assertTreeComparison(map.values().iterator().next(), 2, 0, 1, 0, 0, 6);  // "RootWith2Subtrees2Leaves" has an actual polytomy on its root and is therefore not identical with the other tree.
	}

	
	@Test
	public void test_compareAll_rootSubtreeCount_2SubtreesNoLeavesVS2Subtrees2Leaves() throws IOException, Exception {
		Map<TreePair, PairComparisonData> map = performCompareAll(10, "data/RootWith2SubtreesNoLeaves.tre", 
				"data/RootWith2Subtrees2Leaves.tre").getComparisonMap(); 
		assertEquals(1, map.size());
		assertTreeComparison(map.values().iterator().next(), 2, 0, 1, 0, 0, 6);  // "RootWith2Subtrees2Leaves" has an actual polytomy on its root and is therefore not identical with the other tree.
	}

	
	@Test
	public void test_compareAll_multipleGroups() throws IOException, Exception {
		File file = new File("data/SixTrees.nex");
		AnalysesData analysesData = performCompareAll(4, file.getAbsolutePath());
		
		assertEquals(6, analysesData.getTreeCount());
		assertEquals(6, analysesData.getInputOrder().size());
		Iterator<TreeIdentifier> iterator = analysesData.getInputOrder().iterator();
		assertEquals("tree0", iterator.next().getName());
		assertEquals("tree1", iterator.next().getName());
		assertEquals("tree2", iterator.next().getName());
		assertEquals("tree3", iterator.next().getName());
		assertEquals("tree4", iterator.next().getName());
		assertEquals("tree5", iterator.next().getName());
		
		Map<TreePair, PairComparisonData> map = analysesData.getComparisonMap(); 
		assertEquals(15, map.size());
		
		assertTreeComparison(searchComparisonByNames("tree0", "tree1", map), 0, 2, 0, 1, 1, 6);
		assertTreeComparison(searchComparisonByNames("tree0", "tree2", map), 0, 2, 0, 2, 1, 6);
		assertTreeComparison(searchComparisonByNames("tree0", "tree3", map), 0, 2, 0, 1, 1, 5);
		assertTreeComparison(searchComparisonByNames("tree0", "tree4", map), 0, 2, 0, 1, 1, 5);
		assertTreeComparison(searchComparisonByNames("tree0", "tree5", map), 0, 2, 0, 1, 1, 5);
		
		assertTreeComparison(searchComparisonByNames("tree1", "tree2", map), 2, 0, 0, 0, 1, 6);
		assertTreeComparison(searchComparisonByNames("tree1", "tree3", map), 2, 0, 0, 0, 0, 5);
		assertTreeComparison(searchComparisonByNames("tree1", "tree4", map), 2, 0, 0, 0, 0, 5);
		assertTreeComparison(searchComparisonByNames("tree1", "tree5", map), 2, 0, 0, 0, 0, 5);

		assertTreeComparison(searchComparisonByNames("tree2", "tree3", map), 2, 0, 0, 0, 0, 5);
		assertTreeComparison(searchComparisonByNames("tree2", "tree4", map), 2, 0, 0, 0, 0, 5);
		assertTreeComparison(searchComparisonByNames("tree2", "tree5", map), 2, 0, 0, 0, 0, 5);

		assertTreeComparison(searchComparisonByNames("tree3", "tree4", map), 2, 0, 0, 0, 0, 5);
		assertTreeComparison(searchComparisonByNames("tree3", "tree5", map), 2, 0, 0, 0, 0, 5);

		assertTreeComparison(searchComparisonByNames("tree4", "tree5", map), 2, 0, 0, 0, 0, 5);
	}
}
