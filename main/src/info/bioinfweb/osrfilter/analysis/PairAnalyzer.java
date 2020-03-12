package info.bioinfweb.osrfilter.analysis;


import java.util.HashSet;
import java.util.Set;

import info.bioinfweb.osrfilter.data.PairComparison;
import info.bioinfweb.osrfilter.data.SplitsTree;
import info.bioinfweb.treegraph.document.topologicalcalculation.LeafSet;



public class PairAnalyzer {
	public PairComparison analyzePair(SplitsTree tree1, SplitsTree tree2) {
		LeafSet sharedTerminals = tree1.getTerminalSet().and(tree2.getTerminalSet());
		
		// Create set for shared terminal leaf sets of tree 2 to be reused for each leaf set in tree1:
		Set<LeafSet> comparedSplits = new HashSet<LeafSet>(tree1.getSplits().size());
		for (LeafSet leafSet2 : tree2.getSplits()) {
			comparedSplits.add(leafSet2.and(sharedTerminals));
		}  //TODO This map might be stored and later reused if many trees shared the same leaf set.

		// Compare splits:
		int matchingSplits = 0;
		int conflictingSplits = 0;
		for (LeafSet leafSet1 : tree1.getSplits()) {
			leafSet1 = leafSet1.and(sharedTerminals);

			if (comparedSplits.contains(leafSet1) || comparedSplits.contains(leafSet1.complement())) {
				matchingSplits++;
			}
			else {
				conflictingSplits++;
			}
		}
		
		return new PairComparison(matchingSplits, conflictingSplits, sharedTerminals.childCount());
	}
}
