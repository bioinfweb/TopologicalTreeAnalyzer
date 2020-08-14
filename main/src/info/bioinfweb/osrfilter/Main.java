package info.bioinfweb.osrfilter;


import java.io.File;
import java.util.Map;

import info.bioinfweb.osrfilter.analysis.TopologicalAnalyzer;
import info.bioinfweb.osrfilter.data.PairComparisonData;
import info.bioinfweb.osrfilter.data.TreePair;
import info.bioinfweb.osrfilter.io.TreeIterator;
import info.bioinfweb.treegraph.document.undo.CompareTextElementDataParameters;



public class Main {
	public static void main(String[] args) {
		// Load trees.
		// Perform pairwise comparisons, including calculating a shared taxon set and rerooting one of each pair to match the other. (Note the different root topologies which are identical if not root is assumed.)
		// - Later, each comparison should be in a separate thread and all cores should be used.
		// - Tree iteration:
		//   - Use two readers. One for the first and one for the second tree to be compared. The first one reads all trees once. For each tree from 
		//     the first reader, a new second reader is created that reads all trees behind the first one.
		//   - The iteration may also run over separate files.
		// Output results.
	}
}
