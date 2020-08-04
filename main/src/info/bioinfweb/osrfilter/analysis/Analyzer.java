package info.bioinfweb.osrfilter.analysis;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import info.bioinfweb.osrfilter.data.OSRFilterTree;
import info.bioinfweb.osrfilter.data.PairComparison;
import info.bioinfweb.osrfilter.data.TreePair;
import info.bioinfweb.osrfilter.io.TreeIterator;
import info.bioinfweb.treegraph.document.Node;
import info.bioinfweb.treegraph.document.nodebranchdata.NodeNameAdapter;
import info.bioinfweb.treegraph.document.topologicalcalculation.LeafSet;
import info.bioinfweb.treegraph.document.topologicalcalculation.NodeInfo;
import info.bioinfweb.treegraph.document.topologicalcalculation.TopologicalCalculator;
import info.bioinfweb.treegraph.document.undo.CompareTextElementDataParameters;



public class Analyzer {
	public static final String KEY_LEAF_REFERENCE = Analyzer.class.getName() + ".LeafSet";
	
	
	private TopologicalCalculator topologicalCalculator;
	private LeafSet sharedTerminals;
	private int matchingSplits;
	private int conflictingSplits;
	private int notMatchingSplits;
	
	
	public Analyzer(CompareTextElementDataParameters compareParameters) {
		super();
		topologicalCalculator = new TopologicalCalculator(false, KEY_LEAF_REFERENCE, compareParameters);
	}


	private TopologicalCalculator getTopologicalCalculator() {
		return topologicalCalculator;
	}


	private boolean hasTwoOrMoreSharedTerminalsOnBothSides(Node node) {
		LeafSet leafSet = getTopologicalCalculator().getLeafSet(node);
		return (leafSet.childCount() >= 2) && (leafSet.complement().childCount() >= 2);
	}
	
	
	private boolean hasConflict(Node searchRoot, LeafSet conflictNodeLeafSet) {
		Iterator<Node> iterator = getTopologicalCalculator().findAllConflicts(searchRoot, conflictNodeLeafSet).iterator();
		while (iterator.hasNext()) {
			Node node = iterator.next();
			if (hasTwoOrMoreSharedTerminalsOnBothSides(node)) {
				//System.out.print("conflict between: " + node.getUniqueName() + " ");
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Finds the support or conflict values in the source document.
	 * 
	 * @param sourceRoot the root of the subtree to add support values to (a node of the target document)
	 */
	private void processSubtree(Node targetRoot, OSRFilterTree otherTree) {
		LeafSet leafSet = getTopologicalCalculator().getLeafSet(targetRoot);
		
		if (hasTwoOrMoreSharedTerminalsOnBothSides(targetRoot)) {
			List<NodeInfo> bestSourceNodes = getTopologicalCalculator().findNodeWithAllLeaves(otherTree.getTree(), leafSet, sharedTerminals);  // An empty list should never be returned here, since two shared terminals were ensured to be present.
			
			if (bestSourceNodes.get(0).getAdditionalCount() == 0) {  // Exact match found.
				matchingSplits++;
			}
			else if (hasConflict(bestSourceNodes.get(0).getNode(), leafSet)) {
				//System.out.println(targetRoot.getUniqueName());
				conflictingSplits++;
			}
			else {
				notMatchingSplits++;
			}
		}
		
		for (Node child : targetRoot.getChildren()) {
			processSubtree(child, otherTree);
		}
	}

	
//	private void printTree(Node root, String identation) {
//		System.out.println(identation + root.getUniqueName() + " " + root.getData());
//		for (Node child : root.getChildren()) {
//			printTree(child, identation + "  ");
//		}
//	}
	
	
	private PairComparison comparePair(OSRFilterTree tree1, OSRFilterTree tree2) {
		getTopologicalCalculator().addLeafSets(tree1.getTree().getPaintStart(), NodeNameAdapter.getSharedInstance());  // Only leaves present in both trees will be considered, since
		getTopologicalCalculator().addLeafSets(tree2.getTree().getPaintStart(), NodeNameAdapter.getSharedInstance());  // filterIndexMapBySubtree() was called in the constructor.
		// (Adding these leave sets must happen after filterIndexMapBySubtree(), since this methods may change indices of terminals.)
		
//		printTree(tree1.getTree().getPaintStart(), "");
//		System.out.println();
//		printTree(tree2.getTree().getPaintStart(), "");
//		System.out.println();
		
		sharedTerminals = getTopologicalCalculator().getLeafSet(tree1.getTree().getPaintStart()).and(
				getTopologicalCalculator().getLeafSet(tree2.getTree().getPaintStart()));
		
		// Compare all nodes of tree1 with tree2:
		matchingSplits = 0;
		conflictingSplits = 0;
		notMatchingSplits = 0;
		processSubtree(tree1.getTree().getPaintStart(), tree2);
		
		PairComparison result = new PairComparison();
		result.setSharedTerminals(sharedTerminals.childCount());
		result.setMatchingSplits(matchingSplits);
		result.setConflictingSplitsAB(conflictingSplits);
		result.setNotMatchingSplitsAB(notMatchingSplits);
//		System.out.println(result);
		
		// Compare all nodes of tree2 with tree1:
		matchingSplits = 0;
		conflictingSplits = 0;
		notMatchingSplits = 0;
		processSubtree(tree2.getTree().getPaintStart(), tree1);
//		System.out.println(conflictingSplits);
		result.setConflictingSplitsBA(conflictingSplits);
		result.setNotMatchingSplitsBA(notMatchingSplits);
				
		return result;
	}
	
	
	public Map<TreePair, PairComparison> compareAll(int groupSize, TreeIterator treeIterator) throws Exception {  //TODO Evaluate if the return type is a useful data structure for the use cases or should be changed.
		Map<TreePair, PairComparison> result = new HashMap<TreePair, PairComparison>();
		int start = 0;
		int treeCount = Integer.MAX_VALUE;
		List<OSRFilterTree> trees = new ArrayList<OSRFilterTree>(groupSize);
		while (start < treeCount) {
			treeIterator.reset();
			
			// Skip previously processed trees:
			for (int pos = 0; pos < start; pos++) {
				treeIterator.next();  // This many trees must be present, otherwise start would not have been set to the current value.
			}
			
			// Load current group:
			while (treeIterator.hasNext() && (trees.size() < groupSize)) {
				OSRFilterTree tree = treeIterator.next();
				getTopologicalCalculator().addSubtreeToLeafValueToIndexMap(tree.getTree().getPaintStart(), NodeNameAdapter.getSharedInstance());
				trees.add(tree);
			}
			
			// Compare loaded group:
			for (int pos1 = 0; pos1 < trees.size(); pos1++) {  //TODO Parallelize this loop.
				for (int pos2 = pos1 + 1; pos2 < trees.size(); pos2++) {
					PairComparison comparison = comparePair(trees.get(pos1), trees.get(pos2));
					result.put(new TreePair(trees.get(pos1).getTreeIdentifier(), trees.get(pos2).getTreeIdentifier()), comparison);
				}
			}
			treeCount = start + trees.size();

			// Compare group with subsequent trees:
			while (treeIterator.hasNext()) {
				treeCount++;
				OSRFilterTree tree = treeIterator.next();
				getTopologicalCalculator().addSubtreeToLeafValueToIndexMap(tree.getTree().getPaintStart(), NodeNameAdapter.getSharedInstance());
				for (int pos = 0; pos < trees.size(); pos++) {  //TODO Parallelize this loop. Make sure usage of global fields is save. 
					PairComparison comparison = comparePair(trees.get(pos), tree);
					result.put(new TreePair(trees.get(pos).getTreeIdentifier(), tree.getTreeIdentifier()), comparison);
				}
			}
			
			start += trees.size();
			trees.clear();
			System.gc();  //TODO Should this be done to make sure that memory is really freed up before new trees are loaded?
		}
		
		return result;
	}
}
