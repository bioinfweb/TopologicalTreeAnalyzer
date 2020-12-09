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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import info.bioinfweb.tta.data.TreeData;
import info.bioinfweb.tta.data.TreeIdentifier;



public class TreeDataTable extends DatabaseTable<TreeIdentifier, TreeData> implements DatabaseConstants {  //TODO Decide later how user values should be modeled, if they should still be part of PairData and how their entries should be read and written.
	public TreeDataTable(Connection connection, List<TreeIdentifier> treeOrder) {
		super(connection, treeOrder, TABLE_TREE_DATA);
	}

	
	public static String createSearchExpression(List<TreeIdentifier> treeOrder, TreeIdentifier key) {
		return COLUMN_TREE_INDEX + "=" + treeOrder.indexOf(key);  //TODO Determine tree index more efficiently. Add map or use indices as identifiers in the first place?
	}
	
	
	@Override
	protected String createSearchExpression(TreeIdentifier key) {
		return createSearchExpression(getTreeOrder(), key);
	}


	@Override
	protected int getValueCount() {
		return 3;
	}


	public static void setKeyValues(PreparedStatement statement, List<TreeIdentifier> treeOrder, TreeIdentifier key) throws SQLException {
		statement.setInt(1, treeOrder.indexOf(key));  //TODO Determine tree index more efficiently. Add map or use indices as identifiers in the first place?
	}
	
	
	@Override
	protected void setValueList(TreeIdentifier key, TreeData value, PreparedStatement statement) throws SQLException {
		setKeyValues(statement, getTreeOrder(), key);
		statement.setInt(2, value.getTerminals());
		statement.setInt(3, value.getSplits());
	}


	@Override
	protected TreeData readValue(ResultSet resultSet) throws SQLException {
		return new TreeData(resultSet.getInt(COLUMN_TERMINALS), resultSet.getInt(COLUMN_SPLITS));
	}
}
