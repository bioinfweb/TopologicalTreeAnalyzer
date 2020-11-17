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
package info.bioinfweb.tta.io;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Set;

import info.bioinfweb.commons.SystemUtils;
import info.bioinfweb.tta.data.AnalysesData;
import info.bioinfweb.tta.data.PairComparisonData;
import info.bioinfweb.tta.data.TreeData;
import info.bioinfweb.tta.data.TreeIdentifier;
import info.bioinfweb.tta.data.TreePair;



public class TopologicalDataWriter extends AbstractTableWriter {
	public static final String TREE_INDEX_HEADING = "Index";
	
	public static final String TERMINALS_HEADING = "Terminals";
	public static final String SPLITS_HEADING = "Splits";
	
	public static final String MATCHING_SPLITS_HEADING = "Matching Splits";
	public static final String CONFLICTING_SPLITS_A_B_HEADING = "Conflicting Splits A-B";
	public static final String CONFLICTING_SPLITS_B_A_HEADING = "Conflicting Splits B-A";
	public static final String NOT_MATCHING_SPLITS_A_B_HEADING = "Not Matching Splits A-B";
	public static final String NOT_MATCHING_SPLITS_B_A_HEADING = "Not Matching Splits B-A";
	public static final String SHARED_TERMINALS_HEADING = "Shared Terminals";

	
	private void writeLineBreak(Writer writer) throws IOException {
		writer.write(SystemUtils.LINE_SEPARATOR);  //TODO Always use the same delimiter instead of OS-dependent one?
	}
	
	
	private void writeTreeDataHeadings(Writer writer) throws IOException {
		writer.write(TREE_INDEX_HEADING);
		writer.write("\t");
		writer.write(TERMINALS_HEADING);
		writer.write("\t");
		writer.write(SPLITS_HEADING);
	}
	
	
	public void writeTreeList(File file, List<TreeIdentifier> trees) throws IOException {
		FileWriter fileWriter = new FileWriter(file);
		try {
			BufferedWriter writer = new BufferedWriter(fileWriter);
			try {
				writeTreeIdentifierHeadings(writer, "\t", "");
				writeLineBreak(writer);
				for (TreeIdentifier identifier : trees) {
					writeIdentifier(writer, identifier, "\t");
					writeLineBreak(writer);
				}
			}
			finally {
				writer.close();
			}
		}
		finally {
			fileWriter.close();
		}
	}
	
	
	public void writeTreeData(File file, AnalysesData data) throws IOException {
		FileWriter fileWriter = new FileWriter(file);
		try {
			BufferedWriter writer = new BufferedWriter(fileWriter);
			try {
				writeTreeDataHeadings(writer);
				writeLineBreak(writer);
				for (int i = 0; i < data.getInputOrder().size(); i++) {
					TreeData treeData = data.getTreeMap().get(data.getInputOrder().get(i));
					if (treeData != null) {
						writer.write(Integer.toString(i));
						writer.write("\t");
						writer.write(Integer.toString(treeData.getTerminals()));
						writer.write("\t");
						writer.write(Integer.toString(treeData.getSplits()));
						writeLineBreak(writer);
					}
				}
			}
			finally {
				writer.close();
			}
		}
		finally {
			fileWriter.close();
		}
	}
	
	
	public void updateTreeData(File file, AnalysesData data, Set<TreeIdentifier> newKeys, Map<TreeIdentifier, Integer> keyToIndexMap) throws IOException {
		FileWriter fileWriter = new FileWriter(file, true);
		try {
			BufferedWriter writer = new BufferedWriter(fileWriter);
			try {
				for (TreeIdentifier treeIdentifier : newKeys) {
					TreeData treeData = data.getTreeMap().get(treeIdentifier);
					if (treeData != null) {
						writer.write(keyToIndexMap.get(treeIdentifier).toString());
						writer.write("\t");
						writer.write(Integer.toString(treeData.getTerminals()));
						writer.write("\t");
						writer.write(Integer.toString(treeData.getSplits()));
						writeLineBreak(writer);
					}
					else {
						throw new InternalError("No entry for the key " + treeIdentifier + " added during the last timeout was not found in the model.");
					}
				}
			}
			finally {
				writer.close();
			}
		}
		finally {
			fileWriter.close();
		}
	}
	
	
	private void writePairDataHeadings(Writer writer) throws IOException {
		writer.write(TREE_INDEX_HEADING + TREE_A_SUFFIX);
		writer.write("\t");
		writer.write(TREE_INDEX_HEADING + TREE_B_SUFFIX);
		writer.write("\t");
		writer.write(MATCHING_SPLITS_HEADING);
		writer.write("\t");
		writer.write(CONFLICTING_SPLITS_A_B_HEADING);
		writer.write("\t");
		writer.write(NOT_MATCHING_SPLITS_A_B_HEADING);
		writer.write("\t");
		writer.write(CONFLICTING_SPLITS_B_A_HEADING);
		writer.write("\t");
		writer.write(NOT_MATCHING_SPLITS_B_A_HEADING);
		writer.write("\t");
		writer.write(SHARED_TERMINALS_HEADING);
	}
	
	
	public void writePairData(File file, AnalysesData data) throws IOException {
		FileWriter fileWriter = new FileWriter(file);
		try {
			BufferedWriter writer = new BufferedWriter(fileWriter);
			try {
				writePairDataHeadings(writer);
				writeLineBreak(writer);
				for (int indexA = 0; indexA < data.getInputOrder().size(); indexA++) {
					for (int indexB = indexA + 1; indexB < data.getInputOrder().size(); indexB++) {
						PairComparisonData comparisonData = data.getComparison(data.getInputOrder().get(indexA), data.getInputOrder().get(indexB));
						if (comparisonData != null) {
							writer.write(Integer.toString(indexA));
							writer.write("\t");
							writer.write(Integer.toString(indexB));
							writer.write("\t");
							writer.write(Integer.toString(comparisonData.getMatchingSplits()));
							writer.write("\t");
							writer.write(Integer.toString(comparisonData.getConflictingSplitsAB()));
							writer.write("\t");
							writer.write(Integer.toString(comparisonData.getNotMatchingSplitsAB()));
							writer.write("\t");
							writer.write(Integer.toString(comparisonData.getConflictingSplitsBA()));
							writer.write("\t");
							writer.write(Integer.toString(comparisonData.getNotMatchingSplitsBA()));
							writer.write("\t");
							writer.write(Integer.toString(comparisonData.getSharedTerminals()));
							writeLineBreak(writer);
						}
					}
				}
			}
			finally {
				writer.close();
			}
		}
		finally {
			fileWriter.close();
		}
	}
	
	
	public void updatePairData(File file, AnalysesData data, Set<TreePair> newKeys, Map<TreeIdentifier, Integer> keyToIndexMap) throws IOException {
		FileWriter fileWriter = new FileWriter(file, true);
		try {
			BufferedWriter writer = new BufferedWriter(fileWriter);
			try {
				for (TreePair treePair : newKeys) {
					PairComparisonData comparisonData = data.getComparisonMap().get(treePair);
					if (comparisonData != null) {
						writer.write(keyToIndexMap.get(treePair.getTreeA()).toString());
						writer.write("\t");
						writer.write(keyToIndexMap.get(treePair.getTreeB()).toString());
						writer.write("\t");
						writer.write(Integer.toString(comparisonData.getMatchingSplits()));
						writer.write("\t");
						writer.write(Integer.toString(comparisonData.getConflictingSplitsAB()));
						writer.write("\t");
						writer.write(Integer.toString(comparisonData.getNotMatchingSplitsAB()));
						writer.write("\t");
						writer.write(Integer.toString(comparisonData.getConflictingSplitsBA()));
						writer.write("\t");
						writer.write(Integer.toString(comparisonData.getNotMatchingSplitsBA()));
						writer.write("\t");
						writer.write(Integer.toString(comparisonData.getSharedTerminals()));
						writeLineBreak(writer);
					}
					else {
						throw new InternalError("No entry for the key " + treePair + " added during the last timeout was not found in the model.");
					}
				}
			}
			finally {
				writer.close();
			}
		}
		finally {
			fileWriter.close();
		}
	}
}
