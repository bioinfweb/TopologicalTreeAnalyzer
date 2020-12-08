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
package info.bioinfweb.tta.data;


import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;



public class AnalysesData {
	private List<TreeIdentifier> inputOrder = new ArrayList<TreeIdentifier>();
	private ObservableMap<TreePair, PairComparisonData> comparisonMap = FXCollections.observableHashMap();  //TODO Replace this map by H2 table.
	private ObservableMap<TreeIdentifier, TreeData> treeMap = FXCollections.observableHashMap();  //TODO Replace this map by H2 table.
	
	
	public int getTreeCount() {
		return inputOrder.size();
	}


	public List<TreeIdentifier> getInputOrder() {
		return inputOrder;
	}


	public ObservableMap<TreePair, PairComparisonData> getComparisonMap() {
		return comparisonMap;
	}
	
	
	public PairComparisonData getComparison(TreeIdentifier treeA, TreeIdentifier treeB) {
		PairComparisonData result = getComparisonMap().get(new TreePair(treeA, treeB));
		if (result == null) {
			result = getComparisonMap().get(new TreePair(treeB, treeA));
		}
		return result;
	}
	
	
	public ObservableMap<TreeIdentifier, TreeData> getTreeMap() {
		return treeMap;
	}
	
	
	public void clear() {
		comparisonMap.clear();
		treeMap.clear();
	}
}
