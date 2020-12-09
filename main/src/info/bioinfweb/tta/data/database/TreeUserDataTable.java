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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import info.bioinfweb.tta.data.TreeIdentifier;



public class TreeUserDataTable extends UserDataTable<TreeIdentifier> {
	public TreeUserDataTable(Connection connection, List<TreeIdentifier> treeOrder, List<String> userValues) {
		super(connection, treeOrder, TABLE_TREE_USER_DATA, userValues);
	}

	
	@Override
	protected String createSearchExpression(TreeIdentifier key) {
		return TreeDataTable.createSearchExpression(treeOrder, key);
	}


	@Override
	protected int getKeyColumnCount() {
		return 1;
	}

	
	@Override
	protected void setKeyValues(PreparedStatement statement, TreeIdentifier key) throws SQLException {
		TreeDataTable.setKeyValues(statement, treeOrder, key);
	}
}
