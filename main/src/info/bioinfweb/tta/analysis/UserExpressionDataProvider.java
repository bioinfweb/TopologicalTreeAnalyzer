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

import info.bioinfweb.tta.data.AnalysesData;
import info.bioinfweb.tta.data.PairData;
import info.bioinfweb.tta.data.TreeData;
import info.bioinfweb.tta.data.TreeIdentifier;
import info.bioinfweb.tta.data.TreePair;
import info.bioinfweb.tta.data.UserValues;



public class UserExpressionDataProvider {
	private boolean treeExpression;
	private AnalysesData analysesData;
	private TreeIdentifier[] treeIdentifiers = new TreeIdentifier[2];
	private TreePair currentTreePair;

	
	public UserExpressionDataProvider() {
		super();
	}


	public boolean isTreeExpression() {
		return treeExpression;
	}


	public void setTreeExpression(boolean treeExpression) {
		this.treeExpression = treeExpression;
	}


	public AnalysesData getAnalysesData() {
		return analysesData;
	}


	public void setAnalysesData(AnalysesData analysesData) {
		this.analysesData = analysesData;
	}


	public TreeIdentifier getTreeIdentifier(int index) {
		return treeIdentifiers[index];
	}


	public void setTreeIdentifier(int index, TreeIdentifier identifier) {
		treeIdentifiers[index] = identifier;
		currentTreePair = new TreePair(treeIdentifiers[0], treeIdentifiers[1]);
	}
	
	
	public TreeData getCurrentTreeData(int index) throws SQLException {
		return analysesData.getTreeData().get(getTreeIdentifier(index));
	}
	
	
	public UserValues<TreeIdentifier> getCurrentTreeUserData(int index) throws SQLException {
		return analysesData.getTreeUserData().get(getTreeIdentifier(index));
	}

	
	public PairData getCurrentPairData() throws SQLException {
		return analysesData.getPairData().get(currentTreePair);
	}
	
	
	public UserValues<TreePair> getCurrentPairUserData() throws SQLException {
		return analysesData.getPairUserData().get(currentTreePair);
	}
}
