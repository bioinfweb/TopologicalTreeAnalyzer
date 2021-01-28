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


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import info.bioinfweb.tta.data.AnalysesData;
import info.bioinfweb.tta.data.PairData;
import info.bioinfweb.tta.data.TreeData;
import info.bioinfweb.tta.data.TreeIdentifier;
import info.bioinfweb.tta.data.TreePair;
import info.bioinfweb.tta.data.UserValues;
import info.bioinfweb.tta.data.database.DatabaseConstants;



public class UserExpressionDataProvider {
	private static class TreeProperties {
		public TreeData currentTreeData;
		public UserValues<TreeIdentifier> currentTreeUserData;
		//public IteratingFunctionResultMap currentIterationValues = new IteratingFunctionResultMap();
	}
	
	
	private boolean treeExpression;
	private TreeProperties[] currentTreeProperties;
	private PairData currentPairData;
	private UserValues<TreePair> currentPairUserData;
	//private IteratingFunctionResultMap currentPairIterationValues = new IteratingFunctionResultMap();
	private AnalysesData analysesData;  // Temporary until more efficient calculation of iterating functions is implemented.	

	
	public UserExpressionDataProvider() {
		super();
		
		currentTreeProperties = new TreeProperties[2];
		currentTreeProperties[0] = new TreeProperties();
		currentTreeProperties[1] = new TreeProperties();
		
		//currentPairIterationValues = new IteratingFunctionResultMap();
	}

	
	public boolean isTreeExpression() {
		return treeExpression;
	}


	public void setTreeExpression(boolean treeExpression) {
		this.treeExpression = treeExpression;
	}
	

	public TreeData getCurrentTreeData(int index) {
		return currentTreeProperties[index].currentTreeData;
	}
	
	
	public void setCurrentTreeData(int index, TreeData data) {
		currentTreeProperties[index].currentTreeData = data;
	}
	

	public UserValues<TreeIdentifier> getCurrentTreeUserData(int index) {
		return currentTreeProperties[index].currentTreeUserData;
	}

	
	public void setCurrentTreeUserData(int index, UserValues<TreeIdentifier> values) {
		currentTreeProperties[index].currentTreeUserData = values;
	}

	
//	public IteratingFunctionResultMap getCurrentTreeIterationValues(int index) {
//		return currentTreeProperties[index].currentIterationValues;
//	}

	
	public PairData getCurrentPairData() {
		return currentPairData;
	}
	
	
	public void setCurrentPairData(PairData pairData) {
		currentPairData = pairData;
	}
	
	
	public UserValues<TreePair> getCurrentPairUserData() {
		return currentPairUserData;
	}

	
	public void setCurrentPairUserData(UserValues<TreePair> values) {
		currentPairUserData = values;
	}

	
//	public IteratingFunctionResultMap getCurrentTreeIterationValues() {
//		return currentPairIterationValues;
//	}
	
	
	public AnalysesData getAnalysesData() {
		return analysesData;
	}


	public void setAnalysesData(AnalysesData analysesData) {
		this.analysesData = analysesData;
	}

	
	public Iterator<Object> getPairUserValues(CharSequence userValueName, TreeIdentifier currentTree) {  // Temporary method until more efficient calculation of iterating functions is implemented.
		if (analysesData == null) {  // for expression checking
			List<Object> list = new ArrayList<Object>();
			list.add(getCurrentPairUserData().getUserValues().get(userValueName));  //TODO Check if null?
			return list.iterator();
		}
		else {
			final Iterator<TreeIdentifier> treeIterator = analysesData.getInputOrder().iterator();
			return new Iterator<Object>() {
				@Override
				public boolean hasNext() {
					return treeIterator.hasNext();
				}

				
				@Override
				public Object next() {
					try {
						return analysesData.getPairUserDataValue(currentTree, treeIterator.next()).getUserValues().get(userValueName);
					} 
					catch (SQLException e) {
						throw new RuntimeException(e);  // Temporary workaround for temporary method.
					}
				}
			};
		}
	}
}
