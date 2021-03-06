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

import info.bioinfweb.tta.data.TreeIdentifier;
import info.bioinfweb.tta.data.TreeOrder;
import info.bioinfweb.tta.data.TreePair;



public class PairUserDataTable extends UserDataTable<TreePair> {
	public PairUserDataTable(Connection connection, TreeOrder treeOrder, List<String> userValues) {
		super(connection, treeOrder, TABLE_PAIR_USER_DATA, userValues);
	}

	
	@Override
	protected TreePair readKey(ResultSet resultSet) throws SQLException {
		return new TreePair(getTreeOrder().identifierByIndex(resultSet.getInt(COLUMN_TREE_INDEX_A)), 
				getTreeOrder().identifierByIndex(resultSet.getInt(COLUMN_TREE_INDEX_B)));
	}


	@Override
	protected String createSearchExpression(TreePair key) {
		return PairDataTable.createSearchExpression(getTreeOrder(), key);
	}


	@Override
	protected int getKeyColumnCount() {
		return 2;
	}

	
	@Override
	protected void setKeyValues(PreparedStatement statement, TreePair key) throws SQLException {
		PairDataTable.setKeyValues(statement, getTreeOrder(), key);
	}
}
