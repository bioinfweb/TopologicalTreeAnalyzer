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



public class TopologicalDataWritingManager {
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
	private TopologicalDataFileNames fileNames;
	private long timeout;
	private long lastSave;
	private TopologicalDataWriter writer;
	private boolean filesCreated;

	private Map<TreeIdentifier, Integer> treeIdentifierToFileIndexMap;
	private MapChangeListener<TreeIdentifier, TreeData> treeMapListener;
	private MapChangeListener<TreePair, PairComparisonData> pairMapListener;
	private Set<TreeIdentifier> newTreeData; 
	private Set<TreePair> newPairData; 
	
	
	public TopologicalDataWritingManager(AnalysesData analysesData, String outputFilePrefix, long timeout) {
		super();
	
		if (analysesData == null) {
			throw new IllegalArgumentException("analysesData must not be null.");
		}
		else if (outputFilePrefix == null) {
			throw new IllegalArgumentException("outputFilePrefix must not be null.");
		}
		else {
			this.analysesData = analysesData;
			this.timeout = timeout;
			fileNames = new TopologicalDataFileNames(outputFilePrefix);
			writer = new TopologicalDataWriter();
			filesCreated = false;
			createTreeIdentifierToFileIndexMap();

			newTreeData = new HashSet<>();
			treeMapListener = new NewElementsListener<TreeIdentifier, TreeData>(newTreeData);
			analysesData.getTreeMap().addListener(treeMapListener);
			
			newPairData = new HashSet<>();
			pairMapListener = new NewElementsListener<TreePair, PairComparisonData>(newPairData);
			analysesData.getComparisonMap().addListener(pairMapListener);
		}
	}
	
	
	public long getTimeout() {
		return timeout;
	}


	public void setTimeout(long timeout) {
		this.timeout = timeout;
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
	
	
	public boolean writeNewData() throws IOException {
		if (!filesCreated) {
			writer.writeTreeList(fileNames.getTreeListFile(), analysesData.getInputOrder());
			writer.writeTreeData(fileNames.getTreeDataFile(), analysesData);
			writer.writePairData(fileNames.getPairDataFile(), analysesData);
			filesCreated = true;
		}
		else if (System.currentTimeMillis() - lastSave >= timeout) {
			writer.updateTreeData(fileNames.getTreeDataFile(), analysesData, newTreeData, treeIdentifierToFileIndexMap);
			writer.updatePairData(fileNames.getPairDataFile(), analysesData, newPairData, treeIdentifierToFileIndexMap);
		}
		else {
			return false;  // If no data was written.
		}
		
		// If data was written:
		newTreeData.clear();
		newPairData.clear();
		lastSave = System.currentTimeMillis();
		return true;
	}
}
