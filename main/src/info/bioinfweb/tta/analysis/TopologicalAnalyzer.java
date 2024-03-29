/*
 * Topological Tree Analyzer - User defined topological analyses of large sets of phylogenetic trees. 
 * Copyright (C) 2020-2021  Ben C. Stöver
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


import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
import info.bioinfweb.tta.data.PairData;
import info.bioinfweb.tta.data.TTATree;
import info.bioinfweb.tta.data.TreeData;
import info.bioinfweb.tta.data.TreeIdentifier;
import info.bioinfweb.tta.data.TreePair;
import info.bioinfweb.tta.data.UserExpressions;
import info.bioinfweb.tta.data.database.DatabaseTools;
import info.bioinfweb.tta.data.parameters.AnalysisParameters;
import info.bioinfweb.tta.data.parameters.RuntimeParameters;
import info.bioinfweb.tta.exception.AnalysisException;
import info.bioinfweb.tta.io.TopologicalDataFileNames;
import info.bioinfweb.tta.io.treeiterator.AnalysisTreeIterator;
import info.bioinfweb.tta.io.treeiterator.TreeSelector;



public class TopologicalAnalyzer {
	public static final String KEY_LEAF_REFERENCE = TopologicalAnalyzer.class.getName() + ".LeafSet";
	
	
	private TopologicalCalculator topologicalCalculator;
	private LeafSet sharedTerminals;
	private int matchingSplits;
	private int conflictingSplits;
	private int notMatchingSplits;
	private int pairCount;
	private int pairsProcessed;
	private ProgressMonitor progressMonitor;

	
	
	public TopologicalAnalyzer(CompareTextElementDataParameters compareParameters) {
		super();
		topologicalCalculator = new TopologicalCalculator(false, KEY_LEAF_REFERENCE, compareParameters);
	}

	
	private TopologicalCalculator getTopologicalCalculator() {
		return topologicalCalculator;
	}


	private boolean checkTopologicalDataFiles(File outputDirectory) {
		TopologicalDataFileNames fileNames = new TopologicalDataFileNames(outputDirectory.getAbsolutePath() + File.separator);
		if (fileNames.getTreeListFile().exists() || fileNames.getTreeDataFile().exists() || fileNames.getPairDataFile().exists()) {
			if (fileNames.getTreeListFile().exists() && fileNames.getTreeDataFile().exists() && fileNames.getPairDataFile().exists()) {
				return true;
			}
			else {
				throw new AnalysisException("The output directory \"" + outputDirectory.getAbsolutePath() + "\" contains one or more topological data files but not all of them.");
			}
		}
		else {
			return false;
		}
	}
	
	
	private TTATree<Tree> loadTreeListAndReference(List<TreeIdentifier> treeList, TreeSelector selector, TopologicalCalculator calculator, String... inputFiles) 
			throws IOException, Exception {
		
		TTATree<Tree> result = null;
		AnalysisTreeIterator treeIterator = new AnalysisTreeIterator(inputFiles);
		while (treeIterator.hasNext()) {
			TTATree<Tree> tree = treeIterator.next();
			treeList.add(tree.getTreeIdentifier());
			calculator.addSubtreeToLeafValueToIndexMap(tree.getTree().getPaintStart(), NodeNameAdapter.getSharedInstance());
			if ((selector != null) && (result == null)  // Only the first loaded tree is returned. (Could be more than one if labels are used for identification.)
					&& selector.selectTree(tree.getTreeIdentifier().getFile(), tree.getTreeIdentifier().getID(), tree.getTreeIdentifier().getName(), tree.getTreeIdentifier().getIndexInFile())) {
				
				result = tree;
			}
		}
		return result;
	}
	
	
	private TTATree<Tree> checkInputTrees(String[] inputFiles, File outputDirectory, TreeSelector selector, List<TreeIdentifier> inputTrees) throws Exception {
		TTATree<Tree> referenceTree = loadTreeListAndReference(inputTrees, selector, getTopologicalCalculator(), inputFiles);
		if ((selector != null) && (referenceTree == null)) {
			throw new AnalysisException("No reference tree matching the specified criteria could be found in the input files.");
		}
		
//		if (checkTopologicalDataFiles(outputDirectory)) {
//			TopologicalDataReader.readData(outputDirectory.getAbsolutePath() + File.separator, analysesData);
//			if (!analysesData.getInputOrder().equals(inputTrees)) {
//				throw new AnalysisException("The specified input files do not match the file list found in the topological data files or are in another order. "
//						+ "(You can delete the topological data files if you want start a new analysis.)");
//			}
//		}
//		else {
//			analysesData.getInputOrder().addAll(inputTrees);
//		}
		return referenceTree;
	}
	
	
	private boolean hasTwoOrMoreSharedTerminalsOnBothSides(Node node) {
		LeafSet leafSet = getTopologicalCalculator().getLeafSet(node);
		return (leafSet.and(sharedTerminals).childCount() >= 2) && (leafSet.complement().and(sharedTerminals).childCount() >= 2);  // Note that the and() operation must not be performed before the complement() operation.
	}
	
	
	private boolean hasConflict(Node searchRoot, LeafSet conflictNodeLeafSet) {
		return !getTopologicalCalculator().findAllConflicts(searchRoot, conflictNodeLeafSet, sharedTerminals).isEmpty();
	}
	
	
	/**
	 * Finds the support or conflict values in the source document.
	 * 
	 * @param sourceRoot the root of the subtree to add support values to (a node of the target document)
	 */
	private void processSubtree(Node targetRoot, TTATree<Tree> otherTree, LeafSet parentLeafSet) {
		LeafSet leafSet = getTopologicalCalculator().getLeafSet(targetRoot);
		LeafSet sharedTerminalsLeafSet = leafSet.and(sharedTerminals);
		
		if (targetRoot.hasParent() && hasTwoOrMoreSharedTerminalsOnBothSides(targetRoot) && !sharedTerminalsLeafSet.equals(parentLeafSet)) {  // The root branch is not matched. Branches leading to only one shared terminal are not matched. 
			List<NodeInfo> bestSourceNodes = getTopologicalCalculator().findNodeWithAllLeaves(otherTree.getTree(), leafSet, sharedTerminals);  // An empty list should never be returned here, since two shared terminals were ensured to be present.
			
			if (bestSourceNodes.get(0).getAdditionalCount() == 0) {  // Exact match found.
				//System.out.println("match " + targetRoot.getUniqueName() + " " + bestSourceNodes.get(0).getNode().getUniqueName());
				matchingSplits++;
			}
			else if (hasConflict(bestSourceNodes.get(0).getNode(), leafSet)) {
				conflictingSplits++;
			}
			else {
				notMatchingSplits++;
			}
		}
		
		for (Node child : targetRoot.getChildren()) {
			processSubtree(child, otherTree, sharedTerminalsLeafSet);
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
	
	
	private void comparePair(TTATree<Tree> tree1, TTATree<Tree> tree2, AnalysesData analysesData) throws IOException, SQLException {
		sharedTerminals = getTopologicalCalculator().getLeafSet(tree1.getTree().getPaintStart()).and(
				getTopologicalCalculator().getLeafSet(tree2.getTree().getPaintStart()));
		
//		printTree(tree1.getTree().getPaintStart(), "  ");
//		System.out.println();
//		printTree(tree2.getTree().getPaintStart(), "  ");
//		System.out.println();
		
		// Compare all nodes of tree1 with tree2:
		resetTopologicalData();
		processSubtree(tree1.getTree().getPaintStart(), tree2, null);
		
		// Store tree data:
		if (!analysesData.getTreeData().containsKey(tree1.getTreeIdentifier())) {  // Avoid storing the data multiple times.
			TreeData treeData = new TreeData(tree1.getTreeIdentifier());
			treeData.setTerminals(getTopologicalCalculator().getLeafSet(tree1.getTree().getPaintStart()).childCount());
			treeData.setSplits(matchingSplits + conflictingSplits + notMatchingSplits);
			analysesData.getTreeData().put(treeData);
		}
		
		// Store comparison data;
		PairData comparisonData = new PairData(new TreePair(tree1.getTreeIdentifier(), tree2.getTreeIdentifier()));
		comparisonData.setSharedTerminals(sharedTerminals.childCount());
		comparisonData.setMatchingSplits(matchingSplits);
		comparisonData.setConflictingSplitsAB(conflictingSplits);
		comparisonData.setNotMatchingSplitsAB(notMatchingSplits);
		
		// Compare all nodes of tree2 with tree1 and store comparison data:
		resetTopologicalData();
		processSubtree(tree2.getTree().getPaintStart(), tree1, null);
		comparisonData.setConflictingSplitsBA(conflictingSplits);
		comparisonData.setNotMatchingSplitsBA(notMatchingSplits);
		
		// Store tree data:
		if (!analysesData.getTreeData().containsKey(tree2.getTreeIdentifier())) {  // This is only actually required to store the data of the last tree (which is not covered in the previous loop).
			TreeData treeData = new TreeData(tree2.getTreeIdentifier());
			treeData.setTerminals(getTopologicalCalculator().getLeafSet(tree2.getTree().getPaintStart()).childCount());
			treeData.setSplits(matchingSplits + conflictingSplits + notMatchingSplits);
			analysesData.getTreeData().put(treeData);
		}
		
		analysesData.getPairData().put(comparisonData);
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
		//writingManager.writeNewData();  // Will only write if the minimal time span is already reached.
		pairsProcessed++;
		progressMonitor.setProgressValue((double)pairsProcessed / (double)(pairCount));
	}
	
	
	private void processPair(TTATree<Tree> tree1, TTATree<Tree> tree2, AnalysesData analysesData) throws IOException, SQLException {
		if (analysesData.getComparison(tree1.getTreeIdentifier(), tree2.getTreeIdentifier()) == null) {  // Check if this comparison was already present in the loaded topological data
			comparePair(tree1, tree2, analysesData);
		}
		reportAndSaveProgress();
	}
	
	
	private void compareWithReference(TTATree<Tree> referenceTree, String[] inputFiles, AnalysesData analysesData, 
			ProgressMonitor progressMonitor) throws Exception {
		
		this.progressMonitor = progressMonitor;
		
		progressMonitor.setProgressValue(0.0);
		
		AnalysisTreeIterator treeIterator = new AnalysisTreeIterator(inputFiles);
		pairCount = analysesData.getTreeCount() - 1;
		pairsProcessed = 0;
		addLeafSetsToTree(referenceTree);
		while (treeIterator.hasNext()) {
			TTATree<Tree> tree = treeIterator.next();
			addLeafSetsToTree(tree);
			if (!referenceTree.getTreeIdentifier().equals(tree.getTreeIdentifier())) {
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
		return 0.9 * maxMemory > runtime.totalMemory() - runtime.freeMemory();  //TODO Replace "0.95 *" by subtracting the maximum expected tree size + a buffer.
	}
	
	
	private void addLeafSetsToTree(TTATree<Tree> tree) {
		getTopologicalCalculator().addLeafSets(tree.getTree().getPaintStart(), NodeNameAdapter.getSharedInstance());
	}
	
	
	private void compareAll(String[] inputFiles, AnalysesData analysesData, RuntimeParameters runtimeParameters,	
			ProgressMonitor progressMonitor) throws Exception {
		
		this.progressMonitor = progressMonitor;

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
				trees.add(tree);
				addLeafSetsToTree(tree);
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
				addLeafSetsToTree(tree);
				for (int pos = 0; pos < trees.size(); pos++) {  //TODO Parallelize this loop. Make sure usage of global fields is save. 
					processPair(trees.get(pos), tree, analysesData);
				}
			}
			
			start += trees.size();
			trees.clear();
			System.gc();  //TODO Should this be done to make sure that memory is really freed up before new trees are loaded or should we rely on the automatic invocation? Should it also be done after each subsequent tree iteration?
		}
	}
	
	
	private AnalysesData createAnalysesData(File outputDirectory, List<TreeIdentifier> inputOrder, UserExpressions userExpressions) throws SQLException {  //TODO Refactor for later version to check for and possibly reuse existing databases.
		String topologicalDataURL = AnalysisManager.createDatabaseURL(outputDirectory, AnalysisManager.TOPOLOGICAL_DATA_FILE_PREFIX);

		Connection topologicalDataConnection = DriverManager.getConnection(topologicalDataURL);
		try {
			DatabaseTools.createTreeDataTable(topologicalDataConnection, inputOrder.size(), -1);  //TODO Specify maxTerminals when needed by the method.
			DatabaseTools.createPairDataTable(topologicalDataConnection, inputOrder.size(), -1);  //TODO Specify maxTerminals when needed by the method.
		}
		finally {
			topologicalDataConnection.close();
		}
		
		return new AnalysesData(topologicalDataURL, AnalysisManager.createDatabaseURL(outputDirectory, AnalysisManager.USER_DATA_FILE_PREFIX), inputOrder, 
				userExpressions.treeUserValueNames(), userExpressions.pairUserValueNames());
	}
	
	
	public AnalysesData performAnalysis(String[] inputFiles, File outputDirectory, TreeSelector treeSelector, AnalysisParameters parameters, 
			ProgressMonitor progressMonitor) throws Exception {

		List<TreeIdentifier> inputTrees = new ArrayList<>();
		TTATree<Tree> referenceTree = checkInputTrees(inputFiles, outputDirectory, treeSelector, inputTrees);
		AnalysesData result = createAnalysesData(outputDirectory, inputTrees, parameters.getUserExpressions());
		
		if (referenceTree != null) {
			result.setReferenceTree(referenceTree.getTreeIdentifier());
			compareWithReference(referenceTree, inputFiles, result, progressMonitor);
		}
		else {
			compareAll(inputFiles, result, parameters.getRuntimeParameters(), progressMonitor);
		}
		return result;
	}
}
