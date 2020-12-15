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
package info.bioinfweb.tta.analysis;


import java.util.HashMap;
import java.util.Map;

import info.bioinfweb.tta.data.PairData;
import info.bioinfweb.tta.data.TreeData;



public class UserExpressionDataProvider {
	private static class TreeProperties {
		public TreeData currentTreeData;
		public Map<String, Object> currentTreeUserData = new HashMap<String, Object>();
		public IteratingFunctionResultMap currentIterationValues = new IteratingFunctionResultMap();
		
		public void clear() {
			currentTreeUserData.clear();
			currentIterationValues.clear();
		}
	}
	
	
	private boolean treeExpression;
	private TreeProperties[] currentTreeProperties;
	private PairData currentPairData;
	private Map<String, Object> currentPairUserData;
	private IteratingFunctionResultMap currentPairIterationValues = new IteratingFunctionResultMap();

	
	public UserExpressionDataProvider() {
		super();
		
		currentTreeProperties = new TreeProperties[2];
		currentTreeProperties[0] = new TreeProperties();
		currentTreeProperties[1] = new TreeProperties();
		
		currentPairUserData = new HashMap<>();
		currentPairIterationValues = new IteratingFunctionResultMap();
	}

	
	public void setTreeData(TreeData data) {
		treeExpression = true;
		currentTreeProperties[0].currentTreeData = data;
		currentTreeProperties[1].currentTreeData = null;
		clearUserData();
	}
	

	public void setPairData(PairData pairData, TreeData treeDataA, TreeData treeDataB) {
		treeExpression = false;
		currentPairData = pairData;
		currentTreeProperties[0].currentTreeData = treeDataA;
		currentTreeProperties[1].currentTreeData = treeDataB;
		clearUserData();
	}
	
	
	public void clearUserData() {
		currentTreeProperties[0].clear();
		currentTreeProperties[1].clear();
		currentPairUserData.clear();
		currentPairIterationValues.clear();
	}
	

	public boolean isTreeExpression() {
		return treeExpression;
	}


	public TreeData getCurrentTreeData(int index) {
		return currentTreeProperties[index].currentTreeData;
	}
	
	
	public Map<String, Object> getCurrentTreeUserData(int index) {
		return currentTreeProperties[index].currentTreeUserData;
	}

	
	public IteratingFunctionResultMap getCurrentTreeIterationValues(int index) {
		return currentTreeProperties[index].currentIterationValues;
	}

	
	public PairData getCurrentPairData() {
		return currentPairData;
	}
	
	
	public Map<String, Object> getCurrentPairUserData() {
		return currentPairUserData;
	}

	
	public IteratingFunctionResultMap getCurrentTreeIterationValues() {
		return currentPairIterationValues;
	}
}
