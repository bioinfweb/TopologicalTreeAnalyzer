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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.bioinfweb.tta.data.TreeIdentifier;



public abstract class UserDataTable<K> extends DatabaseTable<K, Map<String, Object>> implements DatabaseConstants {
	private List<String> userValues;
	
	
	public UserDataTable(Connection connection, List<TreeIdentifier> treeOrder, String tableName, List<String> userValues) {
		super(connection, treeOrder, tableName);
		this.userValues = userValues;
	}


	@Override
	protected Map<String, Object> readValue(ResultSet resultSet) throws SQLException {
		Map<String, Object> result = new HashMap<String, Object>();
		ResultSetMetaData metaData = resultSet.getMetaData();
		for (int column = 0; column < metaData.getColumnCount(); column++) {
			String columnName = metaData.getColumnName(column);
			if (columnName.startsWith(COLUMN_PREFIX_USER_DATA)) {
				result.put(columnName.substring(COLUMN_PREFIX_USER_DATA.length()), resultSet.getObject(column));  //TODO Will this be converted to Double and String correctly or are additional steps needed?
			}
		}
		return result;
	}

	
	@Override
	protected int getValueCount() {
		return getKeyColumnCount() + userValues.size();
	}

	
	protected abstract int getKeyColumnCount();
	
	
	protected abstract void setKeyValues(PreparedStatement statement, K key) throws SQLException;
	

	@Override
	protected void setValueList(K key, Map<String, Object> value, PreparedStatement statement) throws SQLException {
		setKeyValues(statement, key);
		int firstIndex = getKeyColumnCount() + 1;
		
		for (String name : userValues) {
			statement.setObject(firstIndex, value.get(name), Types.OTHER);
			firstIndex++;
		}
	}
}
