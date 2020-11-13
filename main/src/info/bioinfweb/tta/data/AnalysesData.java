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
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class AnalysesData {
	private List<TreeIdentifier> inputOrder = new ArrayList<TreeIdentifier>();
	private Map<TreePair, PairComparisonData> comparisonMap = new HashMap<TreePair, PairComparisonData>();
	private Map<TreeIdentifier, TreeData> treeMap = new HashMap<TreeIdentifier, TreeData>();
	
	
	public int getTreeCount() {
		return inputOrder.size();
	}


	public List<TreeIdentifier> getInputOrder() {
		return inputOrder;
	}


	public Map<TreePair, PairComparisonData> getComparisonMap() {
		return comparisonMap;
	}
	
	
	public PairComparisonData getComparison(TreeIdentifier treeA, TreeIdentifier treeB) {
		PairComparisonData result = getComparisonMap().get(new TreePair(treeA, treeB));
		if (result == null) {
			result = getComparisonMap().get(new TreePair(treeB, treeA));
		}
		return result;
	}
	
	
	public Map<TreeIdentifier, TreeData> getTreeMap() {
		return treeMap;
	}
	
	
	public void clear() {
		comparisonMap.clear();
		treeMap.clear();
	}
}
