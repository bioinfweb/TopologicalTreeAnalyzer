package info.bioinfweb.osrfilter.analysis;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import info.bioinfweb.osrfilter.data.PairComparison;
import info.bioinfweb.osrfilter.data.SplitsTree;
import info.bioinfweb.osrfilter.data.TreeIdentifier;
import info.bioinfweb.osrfilter.io.TreeIterator;
import info.bioinfweb.treegraph.document.topologicalcalculation.LeafSet;
import info.bioinfweb.treegraph.document.topologicalcalculation.TopologicalCalculator;
import info.bioinfweb.treegraph.document.undo.CompareTextElementDataParameters;



public class Analyzer {
	public static final String KEY_LEAF_REFERENCE = Analyzer.class.getName() + ".LeafSet";
	
	
	private TopologicalCalculator topologicalCalculator;
	
	
	public Analyzer(CompareTextElementDataParameters compareParameters) {
		super();
		topologicalCalculator = new TopologicalCalculator(false, KEY_LEAF_REFERENCE, compareParameters);
	}


	private PairComparison comparePair(SplitsTree tree1, SplitsTree tree2) {
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
			else {  //TODO These conflicts are only counted, if a split in the other tree in conflict is found. Not finding a match is not sufficient.
				//topologicalCalculator.findHighestConflict(searchRoot, conflictNodeLeafSet, supportAdapter, parseText);
				//TODO Search for conflicting leaf sets as done in TG.
				conflictingSplits++;
			}
		}
		
		return new PairComparison(matchingSplits, conflictingSplits, sharedTerminals.childCount());
	}
	
	
	public MultiValuedMap<TreeIdentifier, PairComparison> compareAll(int groupSize, TreeIterator treeIterator) {  //TODO Evaluate if the return type is a useful data structure for the use cases or should be changed.
		MultiValuedMap<TreeIdentifier, PairComparison> result = new ArrayListValuedHashMap<>();
		int start = 0;
		int treeCount = Integer.MAX_VALUE;
		List<SplitsTree> trees = new ArrayList<SplitsTree>(groupSize);
		while (start < treeCount) {
			treeIterator.reset();
			
			// Skip previously processed trees:
			for (int pos = 0; pos < start; pos++) {
				treeIterator.next();  // This many trees must be present, otherwise start would not have been set to the current value.
			}
			
			// Load current group:
			while (treeIterator.hasNext() && (trees.size() < groupSize)) {
				trees.add(treeIterator.next());
			}
			
			// Compare loaded group:
			for (int pos1 = 0; pos1 < trees.size(); pos1++) {  //TODO Parallelize this loop.
				for (int pos2 = pos1 + 1; pos2 < trees.size(); pos2++) {
					PairComparison comparison = comparePair(trees.get(pos1), trees.get(pos2));
					result.put(trees.get(pos1).getTreeIdentifier(), comparison);
					result.put(trees.get(pos2).getTreeIdentifier(), comparison);
				}
			}
			treeCount = start + trees.size();

			// Compare group with subsequent trees:
			while (treeIterator.hasNext()) {
				treeCount++;
				SplitsTree tree = treeIterator.next();
				for (int pos = 0; pos < trees.size(); pos++) {  //TODO Parallelize this loop.
					PairComparison comparison = comparePair(trees.get(pos), tree);
					result.put(trees.get(pos).getTreeIdentifier(), comparison);
					result.put(tree.getTreeIdentifier(), comparison);
				}
			}
			
			start += trees.size();
			trees.clear();
			System.gc();  //TODO Should this be done to make sure that memory is really freed up before new trees are loaded?
		}
		
		return result;
	}
}
