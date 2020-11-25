/*
 * Topological Tree Analyzer - User defined topological analyses of large sets of phylogenetic trees. 
 * Copyright (C) 2020  Ben C. Stöver
 * <http://bioinfweb.info/TTA>
 * 
 * This file is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package info.bioinfweb.tta.analysis;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import info.bioinfweb.commons.Math2;
import info.bioinfweb.commons.progress.ProgressMonitor;
import info.bioinfweb.treegraph.document.Node;
import info.bioinfweb.treegraph.document.Tree;
import info.bioinfweb.treegraph.document.nodebranchdata.NodeNameAdapter;
import info.bioinfweb.treegraph.document.topologicalcalculation.LeafSet;
import info.bioinfweb.treegraph.document.topologicalcalculation.NodeInfo;
import info.bioinfweb.treegraph.document.topologicalcalculation.TopologicalCalculator;
import info.bioinfweb.treegraph.document.undo.CompareTextElementDataParameters;
import info.bioinfweb.tta.data.AnalysesData;
import info.bioinfweb.tta.data.PairComparisonData;
import info.bioinfweb.tta.data.TTATree;
import info.bioinfweb.tta.data.TreeData;
import info.bioinfweb.tta.data.TreePair;
import info.bioinfweb.tta.data.parameters.RuntimeParameters;
import info.bioinfweb.tta.io.TopologicalDataWritingManager;
import info.bioinfweb.tta.io.treeiterator.AnalysisTreeIterator;



public class TopologicalAnalyzer {
	public static final String KEY_LEAF_REFERENCE = TopologicalAnalyzer.class.getName() + ".LeafSet";
	
	
	private TopologicalCalculator topologicalCalculator;
	private LeafSet sharedTerminals;
	private int matchingSplits;
	private int conflictingSplits;
	private int notMatchingSplits;
	private int pairCount;
	private int pairsProcessed;
	private TopologicalDataWritingManager writingManager;
	private ProgressMonitor progressMonitor;

	
	
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
	private void processSubtree(Node targetRoot, TTATree<Tree> otherTree) {
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
	
	
	private void comparePair(TTATree<Tree> tree1, TTATree<Tree> tree2, AnalysesData analysesData) throws IOException {
		getTopologicalCalculator().addLeafSets(tree1.getTree().getPaintStart(), NodeNameAdapter.getSharedInstance());  // Only leaves present in both trees will be considered, since
		getTopologicalCalculator().addLeafSets(tree2.getTree().getPaintStart(), NodeNameAdapter.getSharedInstance());  // filterIndexMapBySubtree() was called in the constructor.
		// (Adding these leave sets must happen after filterIndexMapBySubtree(), since this methods may change indices of terminals.)
		
		sharedTerminals = getTopologicalCalculator().getLeafSet(tree1.getTree().getPaintStart()).and(
				getTopologicalCalculator().getLeafSet(tree2.getTree().getPaintStart()));
		
		// Compare all nodes of tree1 with tree2:
		resetTopologicalData();
		processSubtree(tree1.getTree().getPaintStart(), tree2);

		// Store tree data:
		if (!analysesData.getTreeMap().containsKey(tree1.getTreeIdentifier())) {  // Avoid storing the data multiple times.
			TreeData treeData = new TreeData();
			treeData.setTerminals(getTopologicalCalculator().getLeafSet(tree1.getTree().getPaintStart()).childCount());
			treeData.setSplits(matchingSplits + conflictingSplits + notMatchingSplits);
			analysesData.getTreeMap().put(tree1.getTreeIdentifier(), treeData);
		}
		
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
		
		// Store tree data:
		if (!analysesData.getTreeMap().containsKey(tree2.getTreeIdentifier())) {  // This is only actually required to store the data of the last tree (which is not covered in the previous loop).
			TreeData treeData = new TreeData();
			treeData.setTerminals(getTopologicalCalculator().getLeafSet(tree2.getTree().getPaintStart()).childCount());
			treeData.setSplits(matchingSplits + conflictingSplits + notMatchingSplits);
			analysesData.getTreeMap().put(tree2.getTreeIdentifier(), treeData);
		}
		
		analysesData.getComparisonMap().put(new TreePair(tree1.getTreeIdentifier(), tree2.getTreeIdentifier()), comparisonData);
	}
	
	
	private int numberOfPairs(int numberOfTrees) {
		if (numberOfTrees >= 2) {
			return Math2.sum1ToN(numberOfTrees - 1);
		}
		else {
			return 0;
		}
	}
	
	
	private void reportAndSaveProgress() throws IOException {  //TODO Make this method synchronized when parallelization is implemented,
		writingManager.writeNewData();  // Will only write if the minimal time span is already reached.
		pairsProcessed++;
		progressMonitor.setProgressValue((double)pairsProcessed / (double)(pairCount));
	}
	
	
	private void processPair(TTATree<Tree> tree1, TTATree<Tree> tree2, AnalysesData analysesData) throws IOException {
		if (analysesData.getComparison(tree1.getTreeIdentifier(), tree2.getTreeIdentifier()) == null) {  // Check if this comparison was already present in the loaded topological data
			comparePair(tree1, tree2, analysesData);
		}
		reportAndSaveProgress();
	}
	
	
	public void compareWithReference(TTATree<Tree> referenceTree, String[] inputFiles, AnalysesData analysesData, 
			TopologicalDataWritingManager writingManager, ProgressMonitor progressMonitor) throws Exception {
		
		this.progressMonitor = progressMonitor;
		this.writingManager = writingManager;
		
		progressMonitor.setProgressValue(0.0);
		getTopologicalCalculator().addSubtreeToLeafValueToIndexMap(referenceTree.getTree().getPaintStart(), NodeNameAdapter.getSharedInstance());
		
		AnalysisTreeIterator treeIterator = new AnalysisTreeIterator(inputFiles);
		pairCount = analysesData.getTreeCount() - 1;
		pairsProcessed = 0;
		while (treeIterator.hasNext()) {
			TTATree<Tree> tree = treeIterator.next();
			if (!referenceTree.getTreeIdentifier().equals(tree.getTreeIdentifier())) {
				getTopologicalCalculator().addSubtreeToLeafValueToIndexMap(tree.getTree().getPaintStart(), NodeNameAdapter.getSharedInstance());
				processPair(referenceTree, tree, analysesData);
			}
		}
	}
	
	
	private boolean moreMemoryAvailable(long maxMemory) {
		Runtime runtime = Runtime.getRuntime();
		if (maxMemory == RuntimeParameters.MAXIMUM) {
			maxMemory = runtime.maxMemory();
		}
		else {
			maxMemory = Math.min(maxMemory, runtime.maxMemory());  //TODO Also specify minimal amount to avoid aborting when loading a single tree.
		}
		return 0.95 * maxMemory > runtime.totalMemory() - runtime.freeMemory();  //TODO Replace "0.95 *" by subtracting the maximum expected tree size + a buffer.
	}
	
	
	public void compareAll(RuntimeParameters runtimeParameters, String[] inputFiles, AnalysesData analysesData,	
			TopologicalDataWritingManager writingManager, ProgressMonitor progressMonitor) throws Exception {
		
		this.progressMonitor = progressMonitor;
		this.writingManager = writingManager;

		progressMonitor.setProgressValue(0.0);
		int start = 0;
		pairCount = numberOfPairs(analysesData.getTreeCount());
		pairsProcessed = 0;
		
		AnalysisTreeIterator treeIterator = new AnalysisTreeIterator(inputFiles);
		List<TTATree<Tree>> trees = new ArrayList<TTATree<Tree>>();
		while (start < analysesData.getTreeCount()) {
			treeIterator.reset();
			
			// Skip previously processed trees:
			for (int pos = 0; pos < start; pos++) {
				treeIterator.next();  // This many trees must be present, otherwise start would not have been set to the current value. (In the first run this loop is not entered. Therefore, no input order writing is necessary here.)
			}
			
			// Load current group:
			while (treeIterator.hasNext() && moreMemoryAvailable(runtimeParameters.getMemory())) {
				TTATree<Tree> tree = treeIterator.next();
				getTopologicalCalculator().addSubtreeToLeafValueToIndexMap(tree.getTree().getPaintStart(), NodeNameAdapter.getSharedInstance());
				trees.add(tree);
			}
			
			// Compare loaded group:
			for (int pos1 = 0; pos1 < trees.size(); pos1++) {  //TODO Parallelize this loop.
				for (int pos2 = pos1 + 1; pos2 < trees.size(); pos2++) {
					processPair(trees.get(pos1), trees.get(pos2), analysesData);
				}
			}

			// Compare group with subsequent trees:
			while (treeIterator.hasNext()) {
				TTATree<Tree> tree = treeIterator.next();
				getTopologicalCalculator().addSubtreeToLeafValueToIndexMap(tree.getTree().getPaintStart(), NodeNameAdapter.getSharedInstance());
				for (int pos = 0; pos < trees.size(); pos++) {  //TODO Parallelize this loop. Make sure usage of global fields is save. 
					processPair(trees.get(pos), tree, analysesData);
				}
			}
			
			start += trees.size();
			trees.clear();
			System.gc();  //TODO Should this be done to make sure that memory is really freed up before new trees are loaded?
		}
	}
}
