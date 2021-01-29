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
package info.bioinfweb.tta.io;


import java.io.File;



public class TopologicalDataFileNames {
	public static final String TREE_LIST_FILE_NAME = "InputTrees.txt";
	public static final String TREE_DATA_FILE_NAME = "TopologicalTreeData.txt";
	public static final String PAIR_DATA_FILE_NAME = "TopologicalPairData.txt";

	
	private File treeListFile;
	private File treeDataFile;
	private File pairDataFile;
	
	
	public TopologicalDataFileNames(String outputFilePrefix) {
		super();
		
		treeListFile = new File(outputFilePrefix + TREE_LIST_FILE_NAME);
		treeDataFile = new File(outputFilePrefix + TREE_DATA_FILE_NAME);
		pairDataFile = new File(outputFilePrefix + PAIR_DATA_FILE_NAME);
	}


	public File getTreeListFile() {
		return treeListFile;
	}


	public File getTreeDataFile() {
		return treeDataFile;
	}


	public File getPairDataFile() {
		return pairDataFile;
	}
}
