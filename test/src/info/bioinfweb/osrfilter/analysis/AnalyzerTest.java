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
//	@Test
//	public void test_comparePair() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//		NewickStringReader newickReader = new NewickStringReader();
//		OSRFilterTree tree1 = new OSRFilterTree(new TreeIdentifier(null, "id0"), newickReader.read("((A, B), C, (D, E));"));
//		OSRFilterTree tree2 = new OSRFilterTree(new TreeIdentifier(null, "id1"), newickReader.read("((A, B), (C, D, E));"));
//		
////		System.out.println(tree1.getTree().getPaintStart().getChildren().size());
////		System.out.println(tree2.getTree().getPaintStart().getChildren().size());
//		
//		Analyzer analyzer = new Analyzer(new CompareTextElementDataParameters());
//		
//		Method method = TestTools.getPrivateMethod(Analyzer.class, "comparePair", OSRFilterTree.class, OSRFilterTree.class);
//		PairComparison comparison = (PairComparison)method.invoke(analyzer, tree1, tree2);
//		
//		System.out.println(comparison.getMatchingSplits());
//		System.out.println(comparison.getConflictingSplits());
//		assertEquals(5, comparison.getSharedTerminals());
//	}
	

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
//		- Assymetrie
//		- Unterschiedliche LeafSets
//		- Unterschiedliche "Wurzeltopologien"

	
	@Test
	public void test_compareAll_asymmetricPair() throws IOException, Exception {
		File file1 = new File("data/PolytomyWithSubtree.tre");
		File file2 = new File("data/PolytomyOnlyLeaves.tre");
		
		Analyzer analyzer = new Analyzer(new CompareTextElementDataParameters());
		Map<TreePair, PairComparison> map = analyzer.compareAll(1000, new TreeIterator(file1,	file2));
		
		assertEquals(1, map.size());
		assertTreeComparison(map.values().iterator().next(), 0, 1, 1, 2, 0, 6);
	}

	
//	@Test
//	public void test_compareAll() throws IOException, Exception {
//		Analyzer analyzer = new Analyzer(new CompareTextElementDataParameters());
//		MultiValuedMap<TreeIdentifier, PairComparison> map = 
//				analyzer.compareAll(1000, new TreeIterator(new File("data/Tree1.tre"),	new File("data/Tree2.tre")));
//		
//		assertEquals(6, map.keySet().size());
//		Iterator<TreeIdentifier> identifierIterator = map.keySet().iterator();
//		
//		Iterator<PairComparison> comparisonIterator = assertNextComparisonIterator(identifierIterator, map);
//		//assertNextComparison(comparisonIterator, expectedMatchingSplits, expectedConflictingSplits, expectedSharedTerminal);
//		
//	}
}
