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


import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

import info.bioinfweb.commons.progress.VoidProgressMonitor;
import info.bioinfweb.treegraph.document.undo.CompareTextElementDataParameters;
import info.bioinfweb.tta.data.AnalysesData;
import info.bioinfweb.tta.data.PairData;
import info.bioinfweb.tta.data.TreeIdentifier;
import info.bioinfweb.tta.data.TreePair;
import info.bioinfweb.tta.data.UserExpressions;
import info.bioinfweb.tta.data.UserValues;
import info.bioinfweb.tta.data.database.DatabaseIterator;
import info.bioinfweb.tta.data.parameters.AnalysisParameters;
import info.bioinfweb.tta.data.parameters.ReferenceTreeDefinition;
import info.bioinfweb.tta.data.parameters.RuntimeParameters;
import info.bioinfweb.tta.io.TopologicalDataFileNames;
import info.bioinfweb.tta.io.treeiterator.TreeSelector;



public class TopologicalAnalyzerTest {
	public static final String DATABASE_FOLDER = "data" + File.separator + "topologicalData";
	

	private static AnalysesData performCompareAll(long maxThreads, long maxMemory, ReferenceTreeDefinition referenceTreeDefinition, UserExpressions userExpressions, String... fileNames) throws IOException, Exception {
		File outputDirectory = new File(DATABASE_FOLDER); 

		TopologicalDataFileNames dataFiles = new TopologicalDataFileNames(outputDirectory.getAbsolutePath() + File.separator);
		if (dataFiles.getTreeListFile().exists() || dataFiles.getTreeDataFile().exists() || dataFiles.getPairDataFile().exists()) {
			throw new InternalError("A data file is still present.");
		}
		
		TreeSelector selector = null;
		if (referenceTreeDefinition != null) {
			selector = referenceTreeDefinition.createTreeSelector(new File("").getAbsoluteFile());  // No actual base directory required since all calls are made with absolute paths.
		}
		AnalysisParameters parameters = new AnalysisParameters();
		parameters.getRuntimeParameters().setMemory(maxMemory);
		parameters.getRuntimeParameters().setThreads(maxThreads);
		if (userExpressions != null) {
			parameters.setUserExpressions(userExpressions);
		}
		AnalysesData result = new TopologicalAnalyzer(new CompareTextElementDataParameters()).
				performAnalysis(fileNames, outputDirectory, selector, parameters, new VoidProgressMonitor());
		
		dataFiles.getTreeListFile().delete();
		dataFiles.getTreeDataFile().delete();
		dataFiles.getPairDataFile().delete();
		
		return result;
	}

	
	public static AnalysesData performCompareAll(String... fileNames) throws IOException, Exception {
		return performCompareAll(RuntimeParameters.MAXIMUM, RuntimeParameters.MAXIMUM, null, null, fileNames);
	}
	
	
	public static AnalysesData performCompareAll(UserExpressions userExpressions, String... fileNames) throws IOException, Exception {
		return performCompareAll(RuntimeParameters.MAXIMUM, RuntimeParameters.MAXIMUM, null, userExpressions, fileNames);
	}
	
	
	public static AnalysesData performCompareWithReference(ReferenceTreeDefinition referenceTreeDefinition, String... fileNames) throws IOException, Exception {
		return performCompareAll(RuntimeParameters.MAXIMUM, RuntimeParameters.MAXIMUM, referenceTreeDefinition, null, fileNames);
	}

	
	public static void assertTreeComparison(PairData comparison, int expectedMatchingSplits, int expectedConflictingSplitsAB, 
			int expectedNotMatchingSplitsAB, int expectedConflictingSplitsBA, int expectedNotMatchingSplitsBA, int expectedSharedTerminal) {
		
		assertEquals(expectedMatchingSplits, comparison.getMatchingSplits());
		assertEquals(expectedConflictingSplitsAB, comparison.getConflictingSplitsAB());
		assertEquals(expectedNotMatchingSplitsAB, comparison.getNotMatchingSplitsAB());
		assertEquals(expectedConflictingSplitsBA, comparison.getConflictingSplitsBA());
		assertEquals(expectedNotMatchingSplitsBA, comparison.getNotMatchingSplitsBA());
		assertEquals(expectedSharedTerminal, comparison.getSharedTerminals());
	}
	
	
	public static void deleteDatabaseFiles() {
		new File(DATABASE_FOLDER + File.separator + AnalysisManager.TOPOLOGICAL_DATA_FILE_PREFIX + AnalysisManager.DATABASE_FILE_EXTENSION).delete();
		new File(DATABASE_FOLDER + File.separator + AnalysisManager.TOPOLOGICAL_DATA_FILE_PREFIX + ".trace.db").delete();
		new File(DATABASE_FOLDER + File.separator + AnalysisManager.USER_DATA_FILE_PREFIX + AnalysisManager.DATABASE_FILE_EXTENSION).delete();
		new File(DATABASE_FOLDER + File.separator + AnalysisManager.USER_DATA_FILE_PREFIX + ".trace.db").delete();
	}
	
	
	@SuppressWarnings("unused")
	private void assertNextTreeComparison(Iterator<PairData> iterator, int expectedMatchingSplits, int expectedConflictingSplitsAB, 
			int expectedNotMatchingSplitsAB,	int expectedConflictingSplitsBA, int expectedNotMatchingSplitsBA, int expectedSharedTerminal) {
		
		assertTrue(iterator.hasNext());
		assertTreeComparison(iterator.next(), expectedMatchingSplits, expectedConflictingSplitsAB, expectedNotMatchingSplitsAB, 
				expectedConflictingSplitsBA, expectedNotMatchingSplitsBA, expectedSharedTerminal);
	}
	
	
	private void testCompareAllTwoTrees(String treeFile1, String treeFile2, int expectedMatchingSplits, int expectedConflictingSplitsAB, 
			int expectedNotMatchingSplitsAB, int expectedConflictingSplitsBA, int expectedNotMatchingSplitsBA, int expectedSharedTerminal) throws Exception {
		
		AnalysesData data = performCompareAll(treeFile1, treeFile2);
		try {
			DatabaseIterator<TreePair, PairData> iterator = data.getPairData().valueIterator();
			assertTrue(iterator.hasNext());
			assertTreeComparison(iterator.next(), expectedMatchingSplits, expectedConflictingSplitsAB, expectedNotMatchingSplitsAB, expectedConflictingSplitsBA, 
					expectedNotMatchingSplitsBA, expectedSharedTerminal);
			assertFalse(iterator.hasNext());
		}
		finally {
			data.close();
			deleteDatabaseFiles();
		}
	}
	
	
	@Test
	public void test_compareAll_asymmetricPair() throws IOException, Exception {
		testCompareAllTwoTrees("data/PolytomyWithSubtree.tre", "data/PolytomyOnlyLeaves.tre", 0, 1, 1, 2, 0, 6);
	}

	
	@Test
	public void test_compareAll_asymmetricPairWithAdditionalLeaves() throws IOException, Exception {
		testCompareAllTwoTrees("data/PolytomyWithSubtreeWithAdditionalLeaves.tre", "data/PolytomyOnlyLeavesWithAdditionalLeaves.tre", 0, 1, 1, 2, 0, 6);
	}

	
	@Test
	public void test_compareAll_identicalInSharedLeafSet1() throws IOException, Exception {
		testCompareAllTwoTrees("data/PolytomyWithSubtree.tre", "data/PolytomyWithSubtreeWithAdditionalLeaves.tre", 2, 0, 0, 0, 0, 6);
	}

	
	@Test
	public void test_compareAll_identicalInSharedLeafSet2() throws IOException, Exception {
		testCompareAllTwoTrees("data/PolytomyOnlyLeaves.tre", "data/PolytomyOnlyLeavesWithAdditionalLeaves.tre", 2, 0, 0, 0, 0, 6);
	}

	
	@Test
	public void test_compareAll_rootSubtreeCount_2SubtreesNoLeavesVS3SubtreesNoLeaves() throws IOException, Exception {
		testCompareAllTwoTrees("data/RootWith2SubtreesNoLeaves.tre", "data/RootWith3SubtreesNoLeaves.tre", 3, 0, 0, 0, 0, 6);
	}

	
	@Test
	public void test_compareAll_rootSubtreeCount_3SubtreesNoLeavesVS2Subtrees2Leaves() throws IOException, Exception {
		testCompareAllTwoTrees("data/RootWith3SubtreesNoLeaves.tre", "data/RootWith2Subtrees2Leaves.tre", 2, 0, 1, 0, 0, 6);  // "RootWith2Subtrees2Leaves" has an actual polytomy on its root and is therefore not identical with the other tree.
	}

	
	@Test
	public void test_compareAll_rootSubtreeCount_2SubtreesNoLeavesVS2Subtrees2Leaves() throws IOException, Exception {
		testCompareAllTwoTrees("data/RootWith2SubtreesNoLeaves.tre", "data/RootWith2Subtrees2Leaves.tre", 2, 0, 1, 0, 0, 6);  // "RootWith2Subtrees2Leaves" has an actual polytomy on its root and is therefore not identical with the other tree.
	}

	
//TODO Refactor this method to test multiple groups based on memory usage.
//	@Test
//	public void test_compareAll_multipleGroups() throws IOException, Exception {
//		File file = new File("data/SixTrees.nex");
//		AnalysesData analysesData = performCompareAll(4, file.getAbsolutePath());
//		
//		assertEquals(6, analysesData.getTreeCount());
//		assertEquals(6, analysesData.getInputOrder().size());
//		Iterator<TreeIdentifier> iterator = analysesData.getInputOrder().iterator();
//		assertEquals("tree0", iterator.next().getName());
//		assertEquals("tree1", iterator.next().getName());
//		assertEquals("tree2", iterator.next().getName());
//		assertEquals("tree3", iterator.next().getName());
//		assertEquals("tree4", iterator.next().getName());
//		assertEquals("tree5", iterator.next().getName());
//		
//		Map<TreePair, PairComparisonData> map = analysesData.getComparisonMap(); 
//		assertEquals(15, map.size());
//		
//		assertTreeComparison(searchComparisonByNames("tree0", "tree1", map), 0, 2, 0, 1, 1, 6);
//		assertTreeComparison(searchComparisonByNames("tree0", "tree2", map), 0, 2, 0, 2, 1, 6);
//		assertTreeComparison(searchComparisonByNames("tree0", "tree3", map), 0, 2, 0, 1, 1, 5);
//		assertTreeComparison(searchComparisonByNames("tree0", "tree4", map), 0, 2, 0, 1, 1, 5);
//		assertTreeComparison(searchComparisonByNames("tree0", "tree5", map), 0, 2, 0, 1, 1, 5);
//		
//		assertTreeComparison(searchComparisonByNames("tree1", "tree2", map), 2, 0, 0, 0, 1, 6);
//		assertTreeComparison(searchComparisonByNames("tree1", "tree3", map), 2, 0, 0, 0, 0, 5);
//		assertTreeComparison(searchComparisonByNames("tree1", "tree4", map), 2, 0, 0, 0, 0, 5);
//		assertTreeComparison(searchComparisonByNames("tree1", "tree5", map), 2, 0, 0, 0, 0, 5);
//
//		assertTreeComparison(searchComparisonByNames("tree2", "tree3", map), 2, 0, 0, 0, 0, 5);
//		assertTreeComparison(searchComparisonByNames("tree2", "tree4", map), 2, 0, 0, 0, 0, 5);
//		assertTreeComparison(searchComparisonByNames("tree2", "tree5", map), 2, 0, 0, 0, 0, 5);
//
//		assertTreeComparison(searchComparisonByNames("tree3", "tree4", map), 2, 0, 0, 0, 0, 5);
//		assertTreeComparison(searchComparisonByNames("tree3", "tree5", map), 2, 0, 0, 0, 0, 5);
//
//		assertTreeComparison(searchComparisonByNames("tree4", "tree5", map), 2, 0, 0, 0, 0, 5);
//	}

	
//	private void testCompareWithReference(File file, ReferenceTreeDefinition referenceTreeDefinition) throws IOException, Exception {
//		AnalysesData analysesData = performCompareWithReference(referenceTreeDefinition, file.getAbsolutePath());
//		
//		assertEquals(3, analysesData.getTreeCount());
//		assertEquals(3, analysesData.getInputOrder().size());
//		Iterator<TreeIdentifier> iterator = analysesData.getInputOrder().iterator();
//		assertEquals("tree1", iterator.next().getID());
//		assertEquals("tree2", iterator.next().getID());
//		assertEquals("tree3", iterator.next().getID());
//		
//		Map<TreePair, PairComparisonData> map = analysesData.getComparisonMap(); 
//		assertEquals(2, map.size());
//		
//		assertNotNull(map.get(new TreePair(new TreeIdentifier(file, "tree2", null), new TreeIdentifier(file, "tree1", null))));
//		assertNotNull(map.get(new TreePair(new TreeIdentifier(file, "tree2", null), new TreeIdentifier(file, "tree3", null))));
//		assertNull(map.get(new TreePair(new TreeIdentifier(file, "tree2", null), new TreeIdentifier(file, "tree2", null))));
//		assertNull(map.get(new TreePair(new TreeIdentifier(file, "tree1", null), new TreeIdentifier(file, "tree3", null))));
//	}

	
//	@Test
//	public void test_compareWithReference_ID() throws IOException, Exception {
//		File file = new File("data/NeXMLTrees.xml");
//		testCompareWithReference(file, new ReferenceTreeDefinition.IDReferenceTreeDefinition(file.getAbsolutePath(), "tree2"));
//	}
//
//	
//	@Test
//	public void test_compareWithReference_Name() throws IOException, Exception {
//		File file = new File("data/NeXMLTrees.xml");
//		testCompareWithReference(file, new ReferenceTreeDefinition.NameReferenceTreeDefinition(file.getAbsolutePath(), "Second tree"));
//	}
//
//	
//	@Test
//	public void test_compareWithReference_Index() throws IOException, Exception {
//		File file = new File("data/NeXMLTrees.xml");
//		testCompareWithReference(file, new ReferenceTreeDefinition.IndexReferenceTreeDefinition(file.getAbsolutePath(), 1));
//	}
}
