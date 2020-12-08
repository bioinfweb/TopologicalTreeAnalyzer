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
import java.sql.SQLException;
import java.sql.Statement;



public class DatabaseTools implements DatabaseConstants {
	public static void createTreeDataTable(Connection connection, int treeCount, int maxTerminalCount) throws SQLException {
		Statement statement = connection.createStatement();
		try {
		  //TODO Determine necessary INT size or types based on tree count, maximum number of splits and terminals. (Significant reduction in database size possible.)
			
			statement.executeUpdate("CREATE TABLE " + TABLE_TREE_DATA + " ( "
					+ COLUMN_TREE_INDEX + " INT NOT NULL, "
          + COLUMN_TERMINALS + " INT NOT NULL, "
          + COLUMN_SPLITS + " INT NOT NULL);");
			statement.executeUpdate("CREATE UNIQUE INDEX " + INDEX_TREE_DATA + " ON " + TABLE_TREE_DATA + " (" + COLUMN_TREE_INDEX + ");");
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
			statement.executeUpdate("CREATE UNIQUE INDEX " + INDEX_PAIR_DATA_PAIR + " ON " + TABLE_PAIR_DATA + " (" + COLUMN_TREE_INDEX_A + ", " + COLUMN_TREE_INDEX_B + ");");
			statement.executeUpdate("CREATE INDEX " + INDEX_PAIR_DATA_TREE_B + " ON " + TABLE_PAIR_DATA + " (" + COLUMN_TREE_INDEX_B + ");");
		}
		finally {
			statement.close();
		}
	}
}
