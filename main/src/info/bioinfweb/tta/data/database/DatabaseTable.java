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
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.List;

import info.bioinfweb.commons.text.StringUtils;
import info.bioinfweb.tta.data.TreeIdentifier;



public abstract class DatabaseTable<K, V> {
	protected Connection connection;
	private List<TreeIdentifier> treeOrder;
	protected String tableName;
	
	
	public DatabaseTable(Connection connection, List<TreeIdentifier> treeOrder, String tableName) {
		super();
		if (connection == null) {
			throw new IllegalArgumentException("connection must not be null.");
		}
		else if (treeOrder == null) {
			throw new IllegalArgumentException("treeOrder must not be null.");
		}
		else if (tableName == null) {
			throw new IllegalArgumentException("tableName must not be null.");
		}
		else if ("".contentEquals(tableName)) {
			throw new IllegalArgumentException("tableName must not be an empty string.");
		}
		else {
			this.connection = connection;
			this.treeOrder = treeOrder;
			this.tableName = tableName;
		}
	}

	
	public List<TreeIdentifier> getTreeOrder() {
		return treeOrder;
	}


	protected abstract String createSearchExpression(K key);
	

	protected abstract V readValue(ResultSet resultSet) throws SQLException;
	
	
	protected abstract int getValueCount();
	
	
	protected abstract void setValueList(K key, V value, PreparedStatement statement) throws SQLException;
	
	
	private ResultSet createResultSet(Statement statement, K key) throws SQLException {
		return statement.executeQuery("SELECT * FROM " + tableName + " WHERE " + createSearchExpression(key) + ";");
	}
	
	
	public V get(K key) throws SQLException {
		V result = null;
		Statement statement = connection.createStatement();
		try {
			ResultSet resultSet = createResultSet(statement, key);
			try {
				if (resultSet.next()) {
					result = readValue(resultSet);
				}
			}
			finally {
				resultSet.close();
			}
		}
		finally {
			statement.close();
		}
		return result;
	}
	
	
	public boolean containsKey(K key) throws SQLException {
		boolean result = false;
		Statement statement = connection.createStatement();
		try {
			ResultSet resultSet = createResultSet(statement, key);
			try {
				result = resultSet.next();
			}
			finally {
				resultSet.close();
			}
		}
		finally {
			statement.close();
		}
		return result;
	}
	
	
	public void put(K key, V value) throws SQLException {
		PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO " + tableName + " VALUES (?" + StringUtils.repeat(", ?", getValueCount() - 1) + ");");
		try {
			setValueList(key, value, insertStatement);
			try {
				insertStatement.executeUpdate();
			}
			catch (SQLIntegrityConstraintViolationException e) {  // Delete previous value if keys need to be unique.
				Statement deleteStatement = connection.createStatement();
				try {
					deleteStatement.execute("DELETE FROM " + tableName + " WHERE " + createSearchExpression(key) + ";");
				}
				finally {
					deleteStatement.close();
				}
				insertStatement.executeUpdate();
			}
		}
		finally {
			insertStatement.close();
		}
	}
	
	
	public DatabaseIterator<K, V> valueIterator() throws SQLException {
		return valueIterator(0, -1, null, null);
	}
	
	
	public DatabaseIterator<K, V> valueIterator(long start) throws SQLException {
		return valueIterator(start, -1, null, null);
	}
	
	
	public DatabaseIterator<K, V> valueIterator(String condition, String orderColumns) throws SQLException {
		return valueIterator(0, -1, condition, orderColumns);
	}
	
	
	public DatabaseIterator<K, V> valueIterator(long start, long maxLength, String condition, String orderColumns) throws SQLException {
		return new DatabaseIterator<K, V>(this, start, maxLength, condition, orderColumns);
	}
}
