package info.bioinfweb.osrfilter.analysis;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.collections4.MultiValuedMap;
import org.junit.Test;

import info.bioinfweb.osrfilter.data.PairComparison;
import info.bioinfweb.osrfilter.data.TreeIdentifier;
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
	

	private Iterator<PairComparison> assertNextComparisonIterator(Iterator<TreeIdentifier> iterator, MultiValuedMap<TreeIdentifier, PairComparison> map) {
		assertTrue(iterator.hasNext());
		return map.get(iterator.next()).iterator();
	}
	
	
	private void assertNextComparison(Iterator<PairComparison> iterator, int expectedMatchingSplits, int expectedConflictingSplits, int expectedSharedTerminal) {
		assertTrue(iterator.hasNext());
		PairComparison comparison = iterator.next();
		assertEquals(expectedMatchingSplits, comparison.getMatchingSplits());
		assertEquals(expectedConflictingSplits, comparison.getConflictingSplits());
		assertEquals(expectedSharedTerminal, comparison.getSharedTerminals());
	}
	
	
	@Test
	public void test_compareAll() throws IOException, Exception {
		Analyzer analyzer = new Analyzer(new CompareTextElementDataParameters());
		MultiValuedMap<TreeIdentifier, PairComparison> map = 
				analyzer.compareAll(1000, new TreeIterator(new File("data/Tree1.tre"),	new File("data/Tree2.tre")));
		
		assertEquals(6, map.keySet().size());
		Iterator<TreeIdentifier> identifierIterator = map.keySet().iterator();
		
		Iterator<PairComparison> comparisonIterator = assertNextComparisonIterator(identifierIterator, map);
		//assertNextComparison(comparisonIterator, expectedMatchingSplits, expectedConflictingSplits, expectedSharedTerminal);
		
	}
}
