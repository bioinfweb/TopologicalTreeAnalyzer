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


import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import info.bioinfweb.tta.data.AnalysesData;
import info.bioinfweb.tta.data.TreeData;
import info.bioinfweb.tta.data.TreeIdentifier;
import info.bioinfweb.tta.data.TreePair;



public class TopologicalDataXXXTest {
	//TODO Refactor test when future usage of the respective classes is clear.
//	@Test
//	public void testWriteUpdateRead() throws IOException {
//		final String prefix = "data/topologicalData/";
//		
//		// Create data object with input order:
//		AnalysesData writtenData = new AnalysesData();
//		final File file1 = new File(prefix + "trees 1.tre");  // Test whitespace in file names.
//		final File file2 = new File(prefix + "trees2.tre");
//		writtenData.getInputOrder().add(new TreeIdentifier(file1, "tree0", null));
//		writtenData.getInputOrder().add(new TreeIdentifier(file1, "tree1", "name 1"));
//		writtenData.getInputOrder().add(new TreeIdentifier(file1, "tree2", null));
//		writtenData.getInputOrder().add(new TreeIdentifier(file2, "tree0", "name 0"));
//		writtenData.getInputOrder().add(new TreeIdentifier(file2, "tree1", null));
//
//		TopologicalDataWritingManager manager = new TopologicalDataWritingManager(writtenData, prefix, 0);
//		
//		// Add initial data:
//		writtenData.getTreeMap().put(writtenData.getInputOrder().get(0), new TreeData(5, 4));
//		writtenData.getTreeMap().put(writtenData.getInputOrder().get(1), new TreeData(5, 3));
//		writtenData.getTreeMap().put(writtenData.getInputOrder().get(2), new TreeData(6, 5));
//		
//		writtenData.getComparisonMap().put(new TreePair(writtenData.getInputOrder().get(0), writtenData.getInputOrder().get(1)), new PairComparisonData(3, 0, 1, 0, 0, 5));
//		writtenData.getComparisonMap().put(new TreePair(writtenData.getInputOrder().get(0), writtenData.getInputOrder().get(2)), new PairComparisonData(3, 0, 1, 0, 0, 5));
//		writtenData.getComparisonMap().put(new TreePair(writtenData.getInputOrder().get(1), writtenData.getInputOrder().get(2)), new PairComparisonData(4, 0, 0, 0, 1, 5));
//		
//		manager.writeNewData();
//		
//		// Add more data:
//		writtenData.getTreeMap().put(writtenData.getInputOrder().get(3), new TreeData(5, 4));
//		writtenData.getTreeMap().put(writtenData.getInputOrder().get(4), new TreeData(6, 4));
//		
//		writtenData.getComparisonMap().put(new TreePair(writtenData.getInputOrder().get(0), writtenData.getInputOrder().get(4)), new PairComparisonData(3, 1, 0, 1, 0, 5));
//
//		manager.writeNewData();
//
//		// Read data:
//		AnalysesData readData = new AnalysesData();
//		TopologicalDataReader.readData(prefix, readData);
//		
//		// Compare data:
//		assertEquals(writtenData.getInputOrder(), readData.getInputOrder());
//		assertEquals(writtenData.getTreeMap(), readData.getTreeMap());
//		assertEquals(writtenData.getComparisonMap(), readData.getComparisonMap());
//
//		// Delete temporary files:
//		TopologicalDataFileNames fileNames = new TopologicalDataFileNames(prefix);
//		fileNames.getTreeListFile().delete();
//		fileNames.getTreeDataFile().delete();
//		fileNames.getPairDataFile().delete();
//	}
}
