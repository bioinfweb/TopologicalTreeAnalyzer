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


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import info.bioinfweb.tta.data.AnalysesData;
import info.bioinfweb.tta.data.PairComparisonData;
import info.bioinfweb.tta.data.TreeData;
import info.bioinfweb.tta.data.TreeIdentifier;
import info.bioinfweb.tta.data.TreePair;



public class TopologicalDataReader {
	private static <D> void readTable(File file, D data, int expectedCount, BiConsumer<String[], D> consumer) throws IOException {
		FileReader fileReader = new FileReader(file);
		try {
			BufferedReader reader = new BufferedReader(fileReader);
			try {
				String line = reader.readLine();  // Skip heading line.
				line = reader.readLine();
				while (line != null) {
					String[] values = line.split("\\t");
					if (values.length != expectedCount) {
						throw new IOException("Invalid table structure found in file \"" + file.getAbsolutePath() + "\".");
					}
					
					consumer.accept(values, data);
					line = reader.readLine();
				}
			}
			finally {
				reader.close();
			}
		}
		finally {
			fileReader.close();
		}
	}
	
	
	private static TreeIdentifier getIdentifier(String index, AnalysesData analysesData) {
		return analysesData.getInputOrder().get(Integer.parseInt(index));
	}
	
	
	public static AnalysesData readData(String filePrefix) throws IOException {
		AnalysesData result = new AnalysesData();
		TopologicalDataFileNames fileNames = new TopologicalDataFileNames(filePrefix);
		
		// Read input order:
		readTable(fileNames.getTreeListFile(), result.getInputOrder(), 3, 
				(String[] values, List<TreeIdentifier> order) -> {
					String name = null;
					if (values[2].length() > 0) {
						name = values[2];
					}
					order.add(new TreeIdentifier(new File(values[0]), values[1], name));
				});
		
		// Read tree data:
		readTable(fileNames.getTreeDataFile(), result.getTreeMap(), 3, 
				(String[] values, Map<TreeIdentifier, TreeData> treeMap) -> {
					treeMap.put(getIdentifier(values[0], result), new TreeData(Integer.parseInt(values[1]), Integer.parseInt(values[2])));
				});
		
		// Read pair data:
		readTable(fileNames.getTreeDataFile(), result.getComparisonMap(), 8, 
				(String[] values, Map<TreePair, PairComparisonData> comparisonMap) -> {
					comparisonMap.put(new TreePair(getIdentifier(values[0], result), getIdentifier(values[1], result)),
							new PairComparisonData(Integer.parseInt(values[2]), Integer.parseInt(values[3]), Integer.parseInt(values[4]), 
									Integer.parseInt(values[5]), Integer.parseInt(values[6]), Integer.parseInt(values[7])));
				});
		
		return result;
	}
}
