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
import java.util.Map;

import info.bioinfweb.tta.data.TreeIdentifier;



public class UserDataTable<K> extends DatabaseTable<K, Map<String, Object>> {
	public UserDataTable(Connection connection, List<TreeIdentifier> treeOrder, String tableName) {
		super(connection, treeOrder, tableName);
		// TODO table name probably set by subclasses for tree and pair user data. (possibly local subclasses) 
	}


	@Override
	protected String createSearchExpression(K key) {
		// TODO Delegate to methods implemented in TreeDataTable and PairDataTable.
		return null;
	}

	
	@Override
	protected Map<String, Object> readValue(ResultSet resultSet) throws SQLException {
		// TODO Implement depending on list of user expressions and their return types. (Can the type vary between rows for the same expression?)
		return null;
	}

	
	@Override
	protected String createValueList(K key, Map<String, Object> value) {
		// TODO Implement depending on list of user expressions and their return types. (Can the type vary between rows for the same expression?)
		return null;
	}
}
