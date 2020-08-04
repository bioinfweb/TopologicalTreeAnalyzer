package info.bioinfweb.osrfilter.analysis;


import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import info.bioinfweb.osrfilter.data.PairComparison;
import info.bioinfweb.osrfilter.data.TreePair;
import info.bioinfweb.osrfilter.io.TreeIterator;
import info.bioinfweb.treegraph.document.undo.CompareTextElementDataParameters;



public class AnalyzerTest {
	private Map<TreePair, PairComparison> performCompareAll(String... fileNames) throws IOException, Exception {
		return new Analyzer(new CompareTextElementDataParameters()).compareAll(1000, new TreeIterator(fileNames));
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
	
	
//	Zu Testen:
//
//		- Polytomien
//		- Asymmetrie
//		- Unterschiedliche LeafSets
//		- Unterschiedliche "Wurzeltopologien"
//      - auch 3 oder mehr Teilbäume an der Wurzel wegen unifyTopology()
	
	
//@Test
//public void test_comparePair() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//	NewickStringReader newickReader = new NewickStringReader();
//	OSRFilterTree tree1 = new OSRFilterTree(new TreeIdentifier(null, "id0"), newickReader.read("((A, B), C, (D, E));"));
//	OSRFilterTree tree2 = new OSRFilterTree(new TreeIdentifier(null, "id1"), newickReader.read("((A, B), (C, D, E));"));
//	
////	System.out.println(tree1.getTree().getPaintStart().getChildren().size());
////	System.out.println(tree2.getTree().getPaintStart().getChildren().size());
//	
//	Analyzer analyzer = new Analyzer(new CompareTextElementDataParameters());
//	
//	Method method = TestTools.getPrivateMethod(Analyzer.class, "comparePair", OSRFilterTree.class, OSRFilterTree.class);
//	PairComparison comparison = (PairComparison)method.invoke(analyzer, tree1, tree2);
//	
//	System.out.println(comparison.getMatchingSplits());
//	System.out.println(comparison.getConflictingSplits());
//	assertEquals(5, comparison.getSharedTerminals());
//}


	@Test
	public void test_compareAll_asymmetricPair() throws IOException, Exception {
		Map<TreePair, PairComparison> map = performCompareAll("data/PolytomyWithSubtree.tre", "data/PolytomyOnlyLeaves.tre"); 
		assertEquals(1, map.size());
		assertTreeComparison(map.values().iterator().next(), 0, 1, 1, 2, 0, 6);
	}

	
	@Test
	public void test_compareAll_rootSubtreeCount_2SubtreesNoLeavesVS3SubtreesNoLeaves() throws IOException, Exception {
		Map<TreePair, PairComparison> map = performCompareAll("data/RootWith2SubtreesNoLeaves.tre", "data/RootWith3SubtreesNoLeaves.tre"); 
		assertEquals(1, map.size());
		assertTreeComparison(map.values().iterator().next(), 3, 0, 0, 0, 0, 6);
	}

	
	@Test
	public void test_compareAll_rootSubtreeCount_3SubtreesNoLeavesVS2Subtrees2Leaves() throws IOException, Exception {
		Map<TreePair, PairComparison> map = performCompareAll("data/RootWith3SubtreesNoLeaves.tre", "data/RootWith2Subtrees2Leaves.tre"); 
		assertEquals(1, map.size());
		assertTreeComparison(map.values().iterator().next(), 2, 0, 1, 0, 0, 6);  // "RootWith2Subtrees2Leaves" has actual polytomy on its root and is therefore not identical with the other tree.
	}

	
	@Test
	public void test_compareAll_rootSubtreeCount_2SubtreesNoLeavesVS2Subtrees2Leaves() throws IOException, Exception {
		Map<TreePair, PairComparison> map = performCompareAll("data/RootWith2SubtreesNoLeaves.tre", "data/RootWith2Subtrees2Leaves.tre"); 
		assertEquals(1, map.size());
		assertTreeComparison(map.values().iterator().next(), 2, 0, 1, 0, 0, 6);  // "RootWith2Subtrees2Leaves" has actual polytomy on its root and is therefore not identical with the other tree.
	}
}
