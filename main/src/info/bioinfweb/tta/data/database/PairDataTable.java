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
package info.bioinfweb.tta.data.database;


import java.sql.Connection;
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

	
	@Override
	protected String createSearchExpression(TreePair key) {
		return COLUMN_TREE_INDEX_A + "=" + treeOrder.indexOf(key.getTreeA()) + " AND " + COLUMN_TREE_INDEX_B + "=" + treeOrder.indexOf(key.getTreeB());  //TODO Determine tree index more efficiently. Add map or use indices as identifiers in the first place?
	}


	@Override
	protected String createValueList(TreePair key, PairData value) {
		return treeOrder.indexOf(key.getTreeA()) + ", " + treeOrder.indexOf(key.getTreeB()) + ", "  //TODO Determine tree index more efficiently. Add map or use indices as identifiers in the first place?
				+ value.getMatchingSplits() + ", "
				+ value.getConflictingSplitsAB() + ", " + value.getConflictingSplitsBA() + ", "
				+ value.getNotMatchingSplitsAB() + ", " + value.getNotMatchingSplitsBA() + ", "
				+ value.getSharedTerminals();
	}


	@Override
	protected PairData readValue(ResultSet resultSet) throws SQLException {
		PairData result = new PairData(new TreePair(treeOrder.get(resultSet.getInt(COLUMN_TREE_INDEX_A)), treeOrder.get(resultSet.getInt(COLUMN_TREE_INDEX_B))));
		result.setMatchingSplits(resultSet.getInt(COLUMN_MATCHING_SPLITS));
		result.setConflictingSplitsAB(resultSet.getInt(COLUMN_CONFLICTING_SPLITS_AB));
		result.setConflictingSplitsBA(resultSet.getInt(COLUMN_CONFLICTING_SPLITS_BA));
		result.setNotMatchingSplitsAB(resultSet.getInt(COLUMN_NON_MATCHING_SPLITS_AB));
		result.setNotMatchingSplitsBA(resultSet.getInt(COLUMN_NON_MATCHING_SPLITS_BA));
		result.setSharedTerminals(resultSet.getInt(COLUMN_SHARED_TERMINALS));
		return result;
	}
}
