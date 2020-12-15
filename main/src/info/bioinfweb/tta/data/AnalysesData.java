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


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import info.bioinfweb.tta.data.database.PairDataTable;
import info.bioinfweb.tta.data.database.PairUserDataTable;
import info.bioinfweb.tta.data.database.TreeDataTable;
import info.bioinfweb.tta.data.database.TreeUserDataTable;



public class AnalysesData {
	private List<TreeIdentifier> inputOrder = new ArrayList<TreeIdentifier>();
	private Connection topologicalDataConnection;
	private Connection userDataConnection;
	private TreeDataTable treeData;
	private PairDataTable pairData;
	private TreeUserDataTable treeUserData;
	private PairUserDataTable pairUserData;
	
	
	public AnalysesData(String topologicalDataURL, String userDataURL, List<TreeIdentifier> inputOrder, List<String> userValues) throws SQLException {
		super();
		this.inputOrder = Collections.unmodifiableList(inputOrder);
		
		topologicalDataConnection = DriverManager.getConnection(topologicalDataURL);
		treeData = new TreeDataTable(topologicalDataConnection, this.inputOrder);
		pairData = new PairDataTable(topologicalDataConnection, this.inputOrder);
		
		userDataConnection = DriverManager.getConnection(userDataURL);
		treeUserData = new TreeUserDataTable(userDataConnection, this.inputOrder, userValues);
		pairUserData = new PairUserDataTable(userDataConnection, this.inputOrder, userValues);
	}


	public int getTreeCount() {
		return inputOrder.size();
	}


	public List<TreeIdentifier> getInputOrder() {
		return inputOrder;
	}


	public TreeDataTable getTreeData() {
		return treeData;
	}


	public PairDataTable getPairData() {
		return pairData;
	}


	public TreeUserDataTable getTreeUserData() {
		return treeUserData;
	}


	public PairUserDataTable getPairUserData() {
		return pairUserData;
	}


	public PairData getComparison(TreeIdentifier treeA, TreeIdentifier treeB) throws SQLException {
		PairData result = getPairData().get(new TreePair(treeA, treeB));
		if (result == null) {
			result = getPairData().get(new TreePair(treeB, treeA));
		}
		return result;
	}
	
	
	public void close() throws SQLException {
		try {
			topologicalDataConnection.close();
		}
		finally {
			userDataConnection.close();  // Make sure that this connection is closed even if closing the other one throws an exception.
		}
	}
}
