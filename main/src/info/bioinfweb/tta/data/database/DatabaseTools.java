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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import info.bioinfweb.tta.data.UserExpression;
import info.bioinfweb.tta.data.UserExpressions;



public class DatabaseTools implements DatabaseConstants {
	private static void createTreeDataIndex(Statement statement, String tableName) throws SQLException {
		statement.executeUpdate("CREATE UNIQUE INDEX " + INDEX_TREE_DATA + " ON " + tableName + " (" + COLUMN_TREE_INDEX + ");");
	}
	
	
	private static void createPairDataIndex(Statement statement, String tableName) throws SQLException {
		statement.executeUpdate("CREATE UNIQUE INDEX " + INDEX_PAIR_DATA_PAIR + " ON " + tableName + " (" + COLUMN_TREE_INDEX_A + ", " + COLUMN_TREE_INDEX_B + ");");
		statement.executeUpdate("CREATE INDEX " + INDEX_PAIR_DATA_TREE_B + " ON " + tableName + " (" + COLUMN_TREE_INDEX_B + ");");
	}
	
	
	public static void createTreeDataTable(Connection connection, int treeCount, int maxTerminalCount) throws SQLException {
		Statement statement = connection.createStatement();
		try {
		  //TODO Determine necessary INT size or types based on tree count, maximum number of splits and terminals. (Significant reduction in database size possible.)
			
			statement.executeUpdate("CREATE TABLE " + TABLE_TREE_DATA + " ( "
					+ COLUMN_TREE_INDEX + " INT NOT NULL, "
          + COLUMN_TERMINALS + " INT NOT NULL, "
          + COLUMN_SPLITS + " INT NOT NULL);");
			createTreeDataIndex(statement, TABLE_TREE_DATA);
		}
		finally {
			statement.close();
		}
	}

	
	public static void createPairDataTable(Connection connection, int treeCount, int maxTerminalCount) throws SQLException {
		Statement statement = connection.createStatement();
		try {
		  //TODO Determine necessary INT size or types based on tree count, maximum number of splits and terminals. (Significant reduction in database size possible.)
			
			statement.executeUpdate("CREATE TABLE " + TABLE_PAIR_DATA + " ( "
					+ COLUMN_TREE_INDEX_A + " INT NOT NULL, "
          + COLUMN_TREE_INDEX_B + " INT NOT NULL, "
          + COLUMN_MATCHING_SPLITS + " INT NOT NULL, "
          + COLUMN_CONFLICTING_SPLITS_AB + " INT NOT NULL, "
          + COLUMN_CONFLICTING_SPLITS_BA + " INT NOT NULL, "
          + COLUMN_NON_MATCHING_SPLITS_AB + " INT NOT NULL, "
          + COLUMN_NON_MATCHING_SPLITS_BA + " INT NOT NULL, "
          + COLUMN_SHARED_TERMINALS + " INT NOT NULL);");
			createPairDataIndex(statement, TABLE_PAIR_DATA);
		}
		finally {
			statement.close();
		}
	}
	
	
	public static void createUserDataTables(Connection connection, UserExpressions expressions) throws SQLException {
		// Create tables:
		StringBuilder treeCommand = new StringBuilder();
		treeCommand.append("CREATE TABLE ");
		treeCommand.append(TABLE_TREE_USER_DATA);
		treeCommand.append(" ( ");
		treeCommand.append(COLUMN_TREE_INDEX);
		treeCommand.append(" INT NOT NULL");

		StringBuilder pairCommand = new StringBuilder();
		pairCommand.append("CREATE TABLE ");
		pairCommand.append(TABLE_PAIR_USER_DATA);
		pairCommand.append(" ( ");
		pairCommand.append(COLUMN_TREE_INDEX_A);
		pairCommand.append(" INT NOT NULL, ");
		pairCommand.append(COLUMN_TREE_INDEX_B);
		pairCommand.append(" INT NOT NULL");

		for (String name : expressions.getInputOrder()) {  //TODO Are there any legal user data names that cannot be column names? If so, either convert or disallow these.
			UserExpression expression = expressions.getExpressions().get(name);
			
			StringBuilder command;
			if (expression.hasTreeTarget()) {
				command = treeCommand;
			}
			else {
				command = pairCommand;
			}
			command.append(", ");
			command.append(COLUMN_PREFIX_USER_DATA);
			command.append(name);
			command.append(" ");
			if (Number.class.isAssignableFrom(expression.getType())) {
				command.append("DOUBLE");
			}
			else {
				command.append("VARCHAR");
			}
			//command.append(" NOT NULL");  // Some user values can currently be null in a row if not all have already been calculated. 
		}
		treeCommand.append(");");
		pairCommand.append(");");
		
		// Create indices:
		Statement statement = connection.createStatement();
		try {
			statement.executeUpdate(treeCommand.toString());
			createTreeDataIndex(statement, TABLE_TREE_USER_DATA);

			statement.executeUpdate(pairCommand.toString());
			createPairDataIndex(statement, TABLE_PAIR_USER_DATA);
			
			for (String name : expressions.getInputOrder()) {
				if (expressions.getExpressions().get(name).hasTreeTarget()) {
					statement.executeUpdate("CREATE INDEX " + INDEX_PREFIX_USER_DATA + name + " ON " + TABLE_TREE_USER_DATA + " (" + COLUMN_PREFIX_USER_DATA + name + ");");
				}
				else {
					statement.executeUpdate("CREATE INDEX " + INDEX_PREFIX_USER_DATA + name + " ON " + TABLE_PAIR_USER_DATA + " (" + COLUMN_PREFIX_USER_DATA + name + ");");
				}
			}
		}
		finally {
			statement.close();
		}
	}
}
