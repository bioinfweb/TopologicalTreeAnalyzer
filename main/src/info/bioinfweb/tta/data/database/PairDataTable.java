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
package info.bioinfweb.tta.data.database;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import info.bioinfweb.tta.data.PairData;
import info.bioinfweb.tta.data.TreeIdentifier;
import info.bioinfweb.tta.data.TreePair;



public class PairDataTable extends DatabaseTable<TreePair, PairData> implements DatabaseConstants {  //TODO Decide later how user values should be modeled, if they should still be part of PairData and how their entries should be read and written.
	public PairDataTable(Connection connection, List<TreeIdentifier> treeOrder) {
		super(connection, treeOrder, TABLE_PAIR_DATA);
	}

	
	public static String createSearchExpression(List<TreeIdentifier> treeOrder, TreePair key) {
		return COLUMN_TREE_INDEX_A + "=" + treeOrder.indexOf(key.getTreeA()) + " AND " + COLUMN_TREE_INDEX_B + "=" + treeOrder.indexOf(key.getTreeB());  //TODO Determine tree index more efficiently. Add map or use indices as identifiers in the first place?
	}


	@Override
	protected String createSearchExpression(TreePair key) {
		return createSearchExpression(getTreeOrder(), key);
	}


	@Override
	protected int getKeyColumnCount() {
		return 1;
	}


	@Override
	protected int getValueCount() {
		return 8;
	}


	public static void setKeyValues(PreparedStatement statement, List<TreeIdentifier> treeOrder, TreePair key) throws SQLException {
		statement.setInt(1, treeOrder.indexOf(key.getTreeA()));  //TODO Determine tree index more efficiently. Add map or use indices as identifiers in the first place?
		statement.setInt(2, treeOrder.indexOf(key.getTreeB()));
	}
	
	
	@Override
	protected void setValueList(TreePair key, PairData value, PreparedStatement statement) throws SQLException {
		setKeyValues(statement, getTreeOrder(), key);
		statement.setInt(3, value.getMatchingSplits());
		statement.setInt(4, value.getConflictingSplitsAB());
		statement.setInt(5, value.getConflictingSplitsBA());
		statement.setInt(6, value.getNotMatchingSplitsAB());
		statement.setInt(7, value.getNotMatchingSplitsBA());
		statement.setInt(8, value.getSharedTerminals());
	}


	@Override
	protected PairData readValue(ResultSet resultSet) throws SQLException {
		PairData result = new PairData(new TreePair(getTreeOrder().get(resultSet.getInt(COLUMN_TREE_INDEX_A)), getTreeOrder().get(resultSet.getInt(COLUMN_TREE_INDEX_B))));
		result.setMatchingSplits(resultSet.getInt(COLUMN_MATCHING_SPLITS));
		result.setConflictingSplitsAB(resultSet.getInt(COLUMN_CONFLICTING_SPLITS_AB));
		result.setConflictingSplitsBA(resultSet.getInt(COLUMN_CONFLICTING_SPLITS_BA));
		result.setNotMatchingSplitsAB(resultSet.getInt(COLUMN_NON_MATCHING_SPLITS_AB));
		result.setNotMatchingSplitsBA(resultSet.getInt(COLUMN_NON_MATCHING_SPLITS_BA));
		result.setSharedTerminals(resultSet.getInt(COLUMN_SHARED_TERMINALS));
		return result;
	}
}
