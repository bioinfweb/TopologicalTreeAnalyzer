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


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import info.bioinfweb.tta.data.AnalysesData;
import info.bioinfweb.tta.data.PairComparisonData;
import info.bioinfweb.tta.data.TreeData;
import info.bioinfweb.tta.data.TreeIdentifier;
import info.bioinfweb.tta.data.TreePair;
import javafx.collections.MapChangeListener;



public class TopologicalDataManager {
	public static final String TREE_LIST_FILE_NAME = "InputTrees.txt";
	public static final String TREE_DATA_FILE_NAME = "TopologicalTreeData.txt";
	public static final String PAIR_DATA_FILE_NAME = "TopologicalPairData.txt";
	
	
	private static final class NewElementsListener<K, V> implements MapChangeListener<K, V> {
		private Set<K> set;
		
		public NewElementsListener(Set<K> set) {
			super();
			this.set = set;
		}

		@Override
		public void onChanged(Change<? extends K, ? extends V> change) {
			if (change.wasRemoved()) {
				throw new InternalError("Removing elements not expected.");
			}
			else if (change.wasAdded()) {
				set.add(change.getKey());
			}
		}
	}
	

	private AnalysesData analysesData;
	private File treeListFile;
	private File treeDataFile;
	private File pairDataFile;
	private TopologicalDataWriter writer;
	private boolean filesCreated;

	private Map<TreeIdentifier, Integer> treeIdentifierToFileIndexMap;
	private MapChangeListener<TreeIdentifier, TreeData> treeMapListener;
	private MapChangeListener<TreePair, PairComparisonData> pairMapListener;
	private Set<TreeIdentifier> newTreeData; 
	private Set<TreePair> newPairData; 
	
	
	public TopologicalDataManager(AnalysesData analysesData, String outputFilePrefix) {
		super();
	
		if (analysesData == null) {
			throw new IllegalArgumentException("analysesData must not be null.");
		}
		else if (outputFilePrefix == null) {
			throw new IllegalArgumentException("outputFilePrefix must not be null.");
		}
		else {
			this.analysesData = analysesData;
			writer = new TopologicalDataWriter();
			filesCreated = false;
			createTreeIdentifierToFileIndexMap();

			treeListFile = new File(outputFilePrefix + TREE_LIST_FILE_NAME);
			treeDataFile = new File(outputFilePrefix + TREE_DATA_FILE_NAME);
			pairDataFile = new File(outputFilePrefix + PAIR_DATA_FILE_NAME);
			
			newTreeData = new HashSet<>();
			treeMapListener = new NewElementsListener<TreeIdentifier, TreeData>(newTreeData);
			analysesData.getTreeMap().addListener(treeMapListener);
			
			newPairData = new HashSet<>();
			pairMapListener = new NewElementsListener<TreePair, PairComparisonData>(newPairData);
			analysesData.getComparisonMap().addListener(pairMapListener);
		}
	}
	
	
	private void createTreeIdentifierToFileIndexMap() {
		treeIdentifierToFileIndexMap = new HashMap<>();
		for (int i = 0; i < analysesData.getInputOrder().size(); i++) {
			treeIdentifierToFileIndexMap.put(analysesData.getInputOrder().get(i), i);
		}
	}
	
	
	public void unregister() {
		analysesData.getTreeMap().removeListener(treeMapListener);
		analysesData.getComparisonMap().removeListener(pairMapListener);
	}
	
	
	public void writeNewData() throws IOException {
		if (!filesCreated) {
			writer.writeTreeList(treeListFile, analysesData.getInputOrder());
			writer.writeTreeData(treeDataFile, analysesData);
			writer.writePairData(pairDataFile, analysesData);
			filesCreated = true;
		}
		else {
			writer.updateTreeData(treeDataFile, analysesData, newTreeData, treeIdentifierToFileIndexMap);
			writer.updatePairData(pairDataFile, analysesData, newPairData, treeIdentifierToFileIndexMap);
		}
		newTreeData.clear();
		newPairData.clear();
	}
}
