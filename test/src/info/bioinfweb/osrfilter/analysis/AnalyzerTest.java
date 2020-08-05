package info.bioinfweb.osrfilter.analysis;


import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

import info.bioinfweb.osrfilter.data.PairComparison;
import info.bioinfweb.osrfilter.data.TreeIdentifier;
import info.bioinfweb.osrfilter.data.TreePair;
import info.bioinfweb.osrfilter.io.TreeIterator;
import info.bioinfweb.treegraph.document.undo.CompareTextElementDataParameters;



public class AnalyzerTest {
	private Map<TreePair, PairComparison> performCompareAll(int groupSize, String... fileNames) throws IOException, Exception {
		return new Analyzer(new CompareTextElementDataParameters()).compareAll(groupSize, new TreeIterator(fileNames));
	}

	
	private void assertTreeComparison(PairComparison comparison, int expectedMatchingSplits, int expectedConflictingSplitsAB, 
			int expectedNotMatchingSplitsAB,	int expectedConflictingSplitsBA, int expectedNotMatchingSplitsBA, int expectedSharedTerminal) {
		
		assertEquals(expectedMatchingSplits, comparison.getMatchingSplits());
		assertEquals(expectedConflictingSplitsAB, comparison.getConflictingSplitsAB());
		assertEquals(expectedNotMatchingSplitsAB, comparison.getNotMatchingSplitsAB());
		assertEquals(expectedConflictingSplitsBA, comparison.getConflictingSplitsBA());
		assertEquals(expectedNotMatchingSplitsBA, comparison.getNotMatchingSplitsBA());
		assertEquals(expectedSharedTerminal, comparison.getSharedTerminals());
	}
	
	
	private void assertNextTreeComparison(Iterator<TreePair> iterator, Map<TreePair, PairComparison> map, String expectedNameA, String expectedNameB,  
			int expectedMatchingSplits, int expectedConflictingSplitsAB, int expectedNotMatchingSplitsAB,	int expectedConflictingSplitsBA, 
			int expectedNotMatchingSplitsBA, int expectedSharedTerminal) {
		
		assertTrue(iterator.hasNext());
		TreePair pair = iterator.next();
		assertEquals(expectedNameA, pair.getTreeA().getName());
		assertEquals(expectedNameB, pair.getTreeB().getName());
		
		assertTreeComparison(map.get(pair), expectedMatchingSplits, expectedConflictingSplitsAB, expectedNotMatchingSplitsAB, 
				expectedConflictingSplitsBA, expectedNotMatchingSplitsBA, expectedSharedTerminal);
	}
	
	
	private void assertNextTreeComparison(Iterator<PairComparison> iterator, int expectedMatchingSplits, int expectedConflictingSplitsAB, 
			int expectedNotMatchingSplitsAB,	int expectedConflictingSplitsBA, int expectedNotMatchingSplitsBA, int expectedSharedTerminal) {
		
		assertTrue(iterator.hasNext());
		assertTreeComparison(iterator.next(), expectedMatchingSplits, expectedConflictingSplitsAB, expectedNotMatchingSplitsAB, 
				expectedConflictingSplitsBA, expectedNotMatchingSplitsBA, expectedSharedTerminal);
	}
	
	
	@Test
	public void test_compareAll_asymmetricPair() throws IOException, Exception {
		Map<TreePair, PairComparison> map = performCompareAll(10, "data/PolytomyWithSubtree.tre", "data/PolytomyOnlyLeaves.tre"); 
		assertEquals(1, map.size());
		assertTreeComparison(map.values().iterator().next(), 0, 1, 1, 2, 0, 6);
	}

	
	@Test
	public void test_compareAll_asymmetricPairWithAdditionalLeaves() throws IOException, Exception {
		Map<TreePair, PairComparison> map = 
				performCompareAll(10, "data/PolytomyWithSubtreeWithAdditionalLeaves.tre", "data/PolytomyOnlyLeavesWithAdditionalLeaves.tre"); 
		assertEquals(1, map.size());
		assertTreeComparison(map.values().iterator().next(), 0, 1, 1, 2, 0, 6);
	}

	
	@Test
	public void test_compareAll_identicalInSharedLeafSet1() throws IOException, Exception {
		Map<TreePair, PairComparison> map = performCompareAll(10, "data/PolytomyWithSubtree.tre", "data/PolytomyWithSubtreeWithAdditionalLeaves.tre"); 
		assertEquals(1, map.size());
		assertTreeComparison(map.values().iterator().next(), 2, 0, 0, 0, 0, 6);
	}

	
	@Test
	public void test_compareAll_identicalInSharedLeafSet2() throws IOException, Exception {
		Map<TreePair, PairComparison> map = performCompareAll(10, "data/PolytomyOnlyLeaves.tre", "data/PolytomyOnlyLeavesWithAdditionalLeaves.tre"); 
		assertEquals(1, map.size());
		assertTreeComparison(map.values().iterator().next(), 2, 0, 0, 0, 0, 6);
	}

	
	@Test
	public void test_compareAll_rootSubtreeCount_2SubtreesNoLeavesVS3SubtreesNoLeaves() throws IOException, Exception {
		Map<TreePair, PairComparison> map = performCompareAll(10, "data/RootWith2SubtreesNoLeaves.tre", "data/RootWith3SubtreesNoLeaves.tre"); 
		assertEquals(1, map.size());
		assertTreeComparison(map.values().iterator().next(), 3, 0, 0, 0, 0, 6);
	}

	
	@Test
	public void test_compareAll_rootSubtreeCount_3SubtreesNoLeavesVS2Subtrees2Leaves() throws IOException, Exception {
		Map<TreePair, PairComparison> map = performCompareAll(10, "data/RootWith3SubtreesNoLeaves.tre", "data/RootWith2Subtrees2Leaves.tre"); 
		assertEquals(1, map.size());
		assertTreeComparison(map.values().iterator().next(), 2, 0, 1, 0, 0, 6);  // "RootWith2Subtrees2Leaves" has an actual polytomy on its root and is therefore not identical with the other tree.
	}

	
	@Test
	public void test_compareAll_rootSubtreeCount_2SubtreesNoLeavesVS2Subtrees2Leaves() throws IOException, Exception {
		Map<TreePair, PairComparison> map = performCompareAll(10, "data/RootWith2SubtreesNoLeaves.tre", "data/RootWith2Subtrees2Leaves.tre"); 
		assertEquals(1, map.size());
		assertTreeComparison(map.values().iterator().next(), 2, 0, 1, 0, 0, 6);  // "RootWith2Subtrees2Leaves" has an actual polytomy on its root and is therefore not identical with the other tree.
	}

	
	@Test
	public void test_compareAll_multipleGroups() throws IOException, Exception {
		File file = new File("data/SixTrees.nex");
		Map<TreePair, PairComparison> map = performCompareAll(4, file.getAbsolutePath()); 
		assertEquals(15, map.size());
		
		Iterator<TreePair> iterator = map.keySet().iterator();
		assertNextTreeComparison(iterator, map, "tree0", "tree1", 0, 2, 0, 1, 1, 6);
		//assertNextTreeComparison(iterator, 0, 1, 1, 2, 0, 6);  // tree0, tree1
	}
}
