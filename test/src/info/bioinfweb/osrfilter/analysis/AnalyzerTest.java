package info.bioinfweb.osrfilter.analysis;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

import info.bioinfweb.osrfilter.data.PairComparison;
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
	
	
	private PairComparison searchComparisonByNames(String name1, String name2, Map<TreePair, PairComparison> map) {
		for (TreePair pair : map.keySet()) {
			if ((name1.equals(pair.getTreeA().getName()) && name2.equals(pair.getTreeB().getName())) ||
					(name1.equals(pair.getTreeB().getName()) && name2.equals(pair.getTreeA().getName()))) {
				
				return map.get(pair);
			}
		}
		return null;
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

	
	@Test
	public void test_compareAll_userExpression() throws IOException, Exception {
		Analyzer analyzer = new Analyzer(new CompareTextElementDataParameters());
		analyzer.getUserExpressions().put("test", "c(0) + c(1)");
		Map<TreePair, PairComparison> map = analyzer.compareAll(10, new TreeIterator("data/PolytomyWithSubtree.tre", "data/PolytomyOnlyLeaves.tre"));

		assertEquals(1, map.size());
		PairComparison comparison = map.values().iterator().next();
		assertTreeComparison(comparison, 0, 1, 1, 2, 0, 6);
		
		Object userValue = comparison.getUserValues().get("test");
		assertTrue(userValue instanceof Double);
		assertEquals(3.0, ((Double)userValue).doubleValue(), 0.000001);
	}
}
