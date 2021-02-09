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


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import info.bioinfweb.tta.data.DatabaseValue;



public class DatabaseIterator<K, V extends DatabaseValue<K>> implements DatabaseConstants {
	public static final int MAX_ITERATOR_GROUP_SIZE = 1024;


	private DatabaseTable<K, V> table;
	private long start;
	private long maxLength;
	private long pos;
	private String condition;
	private String orderColumns;
	private PreparedStatement statement;
	private ResultSet resultSet;
	private V nextValue;
	

	public DatabaseIterator(DatabaseTable<K, V> table, long start, long maxLength, String condition, String orderColumns) throws SQLException  {
		super();
		if (table == null) {
			throw new IllegalArgumentException("table must not be null.");
		}
		else if (start < 0) {
			throw new IllegalArgumentException("start must not be below zero.");
		}
		else {
			this.table = table;
			this.start = start;
			pos = start;
			this.maxLength = maxLength;
			if (condition == null) {
				this.condition = "";
			}
			else {
				this.condition = " WHERE " + condition;
			}
			if (orderColumns ==  null) {
				this.orderColumns = "";
			}
			else {
				this.orderColumns = " ORDER BY " + orderColumns;
			}
			ensureNextValue();
		}
	}


	private boolean endReached() throws SQLException {
		return ((maxLength > 0) && (pos >= start + maxLength)) || ((statement != null) && statement.isClosed());
	}
	
	
	private void ensureNextValue() throws SQLException  {
		nextValue = null;
		if (!endReached()) {
			if (statement == null) {  // First run
				statement = table.connection.prepareStatement(
						"SELECT * FROM " + table.tableName + condition + orderColumns + " LIMIT ?, " + Math.min(maxLength, MAX_ITERATOR_GROUP_SIZE) + ";");
			}
			
			if ((resultSet == null) || !resultSet.next()) {  // First run or end of the result set
				if (resultSet != null) {
					resultSet.close();  // Close previous result set
				}
				statement.setLong(1, pos);
				resultSet = statement.executeQuery();
				if (!resultSet.next()) {  // No more elements to read in the table
					close();  // Make sure no further reading attempts are made.
				}
			}
			
			if (!endReached()) {
				nextValue = table.readValue(resultSet);
				pos++;
			}
		}
	}
	
	
	public boolean hasNext() {
		return nextValue != null;
	}
	
	
	public V next() throws SQLException {
		if (nextValue == null) {
			throw new NoSuchElementException();
		}
		else {
			V result = nextValue;
			ensureNextValue();
			return result;
		}
	}


	public void close() throws SQLException {
		if (statement != null) {
			statement.close();  // Also closes a possible ResultSet.
		}
	}
}
