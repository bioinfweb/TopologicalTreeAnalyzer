package info.bioinfweb.osrfilter.analysis;


import info.bioinfweb.osrfilter.data.PairComparison;
import info.bioinfweb.osrfilter.data.SplitsTree;
import info.bioinfweb.treegraph.document.topologicalcalculation.LeafSet;



public class PairAnalyzer {
	public PairComparison analyzePair(SplitsTree tree1, SplitsTree tree2) {
		//TODO Should different terminal sets be supported? -> Yes. This would not allow for a reusable HashMap, except if many trees still share the same taxon set.
		
		int matchingSplits = 0;
		int conflictingSplits = 0;
		for (LeafSet leafSet : tree1.getSplits()) {
			if (tree2.getSplits().contains(leafSet) || tree2.getSplits().contains(leafSet.complement())) {
				matchingSplits++;
			}
			else {
				conflictingSplits++;
			}
		}
		
		return new PairComparison(matchingSplits, conflictingSplits, -1); //TODO Determine number of terminals.
	}
}
