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

			if (comparedSplits.contains(leafSet1) || comparedSplits.contains(leafSet1.complement())) {  //TODO Handle partly matching polytomy. Will currently be counted as a conflict. This could only be done, if topological information is conserved. For all leaf sets that are polytomies, the groups of the first level would have to be stored.
				matchingSplits++;
			}
			else {  //TODO These conflicts are only counted, if a split in the other in tree in conflict is found. Not finding a match is not sufficient.
				//TODO Search for conflicting leaf sets as done in TG.
				conflictingSplits++;
			}
		}
		
		return new PairComparison(matchingSplits, conflictingSplits, sharedTerminals.childCount());
	}
}
