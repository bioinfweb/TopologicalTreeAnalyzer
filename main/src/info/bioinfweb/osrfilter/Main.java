package info.bioinfweb.osrfilter;


import java.io.File;

import org.apache.commons.collections4.MultiValuedMap;

import info.bioinfweb.osrfilter.analysis.Analyzer;
import info.bioinfweb.osrfilter.data.PairComparison;
import info.bioinfweb.osrfilter.data.TreeIdentifier;
import info.bioinfweb.osrfilter.io.TreeIterator;
import info.bioinfweb.treegraph.document.undo.CompareTextElementDataParameters;



public class Main {
	public static void main(String[] args) {
		
		
		Analyzer analyzer = new Analyzer(new CompareTextElementDataParameters());
		try {
			 MultiValuedMap<TreeIdentifier, PairComparison> map = analyzer.compareAll(1000, new TreeIterator(
					 new File("../info.bioinfweb.osrfilter.test/data/Tree1.tre")/*, 
					 new File("../info.bioinfweb.osrfilter.test/data/Tree2.tre")*/));
			 //System.out.println(map.keySet().size());
			 //System.out.println(map.keySet());
			 
			for (TreeIdentifier identifier : map.keySet()) {
				System.out.println(identifier);
				for (PairComparison comparison : map.get(identifier)) {
					System.out.println("  " + comparison);
				}
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
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
