package info.bioinfweb.osrfilter.analysis;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.bioinfweb.osrfilter.data.AnalysesData;
import info.bioinfweb.osrfilter.data.OSRFilterTree;
import info.bioinfweb.osrfilter.data.PairComparisonData;
import info.bioinfweb.osrfilter.data.TreeData;
import info.bioinfweb.osrfilter.data.TreePair;
import info.bioinfweb.osrfilter.io.TreeIterator;
import info.bioinfweb.treegraph.document.Node;
import info.bioinfweb.treegraph.document.nodebranchdata.NodeNameAdapter;
import info.bioinfweb.treegraph.document.topologicalcalculation.LeafSet;
import info.bioinfweb.treegraph.document.topologicalcalculation.NodeInfo;
import info.bioinfweb.treegraph.document.topologicalcalculation.TopologicalCalculator;
import info.bioinfweb.treegraph.document.undo.CompareTextElementDataParameters;



public class TopologicalAnalyzer {
	public static final String KEY_LEAF_REFERENCE = TopologicalAnalyzer.class.getName() + ".LeafSet";
	
	
	private TopologicalCalculator topologicalCalculator;
	private LeafSet sharedTerminals;
	private int matchingSplits;
	private int conflictingSplits;
	private int notMatchingSplits;
	
	
	public TopologicalAnalyzer(CompareTextElementDataParameters compareParameters) {
		super();
		topologicalCalculator = new TopologicalCalculator(false, KEY_LEAF_REFERENCE, compareParameters);
	}

	
	private TopologicalCalculator getTopologicalCalculator() {
		return topologicalCalculator;
	}


	private boolean hasTwoOrMoreSharedTerminalsOnBothSides(Node node) {
		LeafSet leafSet = getTopologicalCalculator().getLeafSet(node).and(sharedTerminals);
		return (leafSet.childCount() >= 2) && (leafSet.complement().childCount() >= 2);
	}
	
	
	private boolean hasConflict(Node searchRoot, LeafSet conflictNodeLeafSet) {
		return !getTopologicalCalculator().findAllConflicts(searchRoot, conflictNodeLeafSet, sharedTerminals).isEmpty();
	}
	
	
	/**
	 * Finds the support or conflict values in the source document.
	 * 
	 * @param sourceRoot the root of the subtree to add support values to (a node of the target document)
	 */
	private void processSubtree(Node targetRoot, OSRFilterTree otherTree) {
		LeafSet leafSet = getTopologicalCalculator().getLeafSet(targetRoot);
		
		if (targetRoot.hasParent() && hasTwoOrMoreSharedTerminalsOnBothSides(targetRoot)) {  // The root branch is not matched. Branches leading to only one shared terminal are not matched. 
			List<NodeInfo> bestSourceNodes = getTopologicalCalculator().findNodeWithAllLeaves(otherTree.getTree(), leafSet, sharedTerminals);  // An empty list should never be returned here, since two shared terminals were ensured to be present.
			
			if (bestSourceNodes.get(0).getAdditionalCount() == 0) {  // Exact match found.
				//System.out.println("match " + targetRoot.getUniqueName() + " " + bestSourceNodes.get(0).getNode().getUniqueName());
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

	
	@SuppressWarnings("unused")
	private void printTree(Node root, String identation) {
		System.out.println(identation + root.getUniqueName() + " " + root.getData());
		for (Node child : root.getChildren()) {
			printTree(child, identation + "  ");
		}
	}
	
	
	private void resetTopologicalData() {
		matchingSplits = 0;
		conflictingSplits = 0;
		notMatchingSplits = 0;
	}
	
	
	private void comparePair(OSRFilterTree tree1, OSRFilterTree tree2, AnalysesData analysesData) {
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
		resetTopologicalData();
		processSubtree(tree1.getTree().getPaintStart(), tree2);

		//Store tree data: (Note that tree2 was already processed before.)
		TreeData treeData = new TreeData();
		treeData.setTerminals(getTopologicalCalculator().getLeafSet(tree1.getTree().getPaintStart()).childCount());
		treeData.setSplits(matchingSplits + conflictingSplits + notMatchingSplits);
		analysesData.getTreeMap().put(tree1.getTreeIdentifier(), treeData);
		
		// Store comparison data;
		PairComparisonData comparisonData = new PairComparisonData();
		comparisonData.setSharedTerminals(sharedTerminals.childCount());
		comparisonData.setMatchingSplits(matchingSplits);
		comparisonData.setConflictingSplitsAB(conflictingSplits);
		comparisonData.setNotMatchingSplitsAB(notMatchingSplits);
		//System.out.println();
		
		// Compare all nodes of tree2 with tree1 and store comparison data:
		resetTopologicalData();
		processSubtree(tree2.getTree().getPaintStart(), tree1);
		comparisonData.setConflictingSplitsBA(conflictingSplits);
		comparisonData.setNotMatchingSplitsBA(notMatchingSplits);
		
		analysesData.getComparisonMap().put(new TreePair(tree1.getTreeIdentifier(), tree2.getTreeIdentifier()), comparisonData);
	}
	
	
	public void compareAll(int groupSize, TreeIterator treeIterator, AnalysesData analysesData) throws Exception {
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
					comparePair(trees.get(pos1), trees.get(pos2), analysesData);
				}
			}
			treeCount = start + trees.size();

			// Compare group with subsequent trees:
			while (treeIterator.hasNext()) {
				treeCount++;
				OSRFilterTree tree = treeIterator.next();
				getTopologicalCalculator().addSubtreeToLeafValueToIndexMap(tree.getTree().getPaintStart(), NodeNameAdapter.getSharedInstance());
				for (int pos = 0; pos < trees.size(); pos++) {  //TODO Parallelize this loop. Make sure usage of global fields is save. 
					comparePair(trees.get(pos), tree, analysesData);
				}
			}
			
			start += trees.size();
			trees.clear();
			System.gc();  //TODO Should this be done to make sure that memory is really freed up before new trees are loaded?
		}
	}
}
