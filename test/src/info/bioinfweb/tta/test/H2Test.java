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
package info.bioinfweb.tta.test;


import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import info.bioinfweb.tta.data.PairData;
import info.bioinfweb.tta.io.PairDataIteratorPerformanceTest;
import info.bioinfweb.tta.io.data.PairDataIterator;



public class H2Test {
	private static void fillDatabase(Connection conn) throws IOException, SQLException {
		Statement tableStatement = conn.createStatement();
		try {
			tableStatement.executeUpdate("CREATE TABLE pairData ( "  //TODO Determine necessary INT size based on tree count, maximum number of splits and terminals.
					+ "treeA INT NOT NULL, "
          + "treeB INT NOT NULL, "
          + "matchingSplits INT NOT NULL, "
          + "conflictingSplitsAB INT NOT NULL, "
          + "conflictingSplitsBA INT NOT NULL, "
          + "nonMatchingSplitsAB INT NOT NULL, "
          + "nonMatchingSplitsBA INT NOT NULL, "
          + "sharedTerminals INT NOT NULL);");
			tableStatement.executeUpdate("CREATE UNIQUE INDEX trees ON pairData (treeA, treeB);");
		}
		finally {
			tableStatement.close();
		}
		
		PreparedStatement rowStatement = conn.prepareStatement(
				"INSERT INTO pairData VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
		
		try {
			PairDataIterator iterator = new PairDataIterator(
					new File("D:\\LocalUserData\\bstoe_01\\Projekte\\TTA\\Testdaten\\2020-12-01\\Cloutier_CNEE_TNT_boot_on_opt\\TopologicalPairData.txt"));
					//new File("D:\\Users\\Ben Stöver\\Dokumente\\Arbeit\\Projekte\\TTA\\Tests\\data\\Cloutier_CNEE_TNT_boot_on_opt\\TopologicalPairData.txt"));
			
			try {
				long start = System.currentTimeMillis();
				int pos = 0;
				while (iterator.hasNext()) {
					PairData data = iterator.next();

//					rowStatement.setInt(1, data.getPair().getTreeA().getIndexInFile());
//					rowStatement.setInt(2, data.getPair().getTreeB().getIndexInFile());
					rowStatement.setInt(1, pos);
					rowStatement.setInt(2, 0);
					rowStatement.setInt(3, data.getMatchingSplits());
					rowStatement.setInt(4, data.getConflictingSplitsAB());
					rowStatement.setInt(5, data.getConflictingSplitsBA());
					rowStatement.setInt(6, data.getNotMatchingSplitsAB());
					rowStatement.setInt(7, data.getNotMatchingSplitsBA());
					rowStatement.setInt(8, data.getSharedTerminals());
					rowStatement.executeUpdate();

					pos++;
					if (pos % 1000000 == 0) {
						System.out.print(pos  + " elements written. ");
						PairDataIteratorPerformanceTest.printTime(start);
					}
				}
				
				System.out.print("Creating database finished. ");
				PairDataIteratorPerformanceTest.printTime(start);
			}
			finally {
				iterator.close();
			}
		}
		finally {
			rowStatement.close();
		}
		// Takes about 20 min on office desktop, 6 min on home desktop. Uses one core of CPU at 100% which is the limiting factor.
	}
	
	
	private static void createAnotherTable(Connection conn) throws IOException, SQLException {
		Statement tableStatement = conn.createStatement();
		try {
			long start = System.currentTimeMillis();
			tableStatement.executeUpdate("CREATE TABLE userPairData ( "
					+ "treeA INT NOT NULL, "
          + "treeB INT NOT NULL, "
          + "value1 INT NOT NULL, "
          + "value2 INT NOT NULL);");
			System.out.print("Adding table done. ");
			PairDataIteratorPerformanceTest.printTime(start);

			tableStatement.executeUpdate("CREATE UNIQUE INDEX trees ON pairData (treeA, treeB);");
			System.out.print("Adding userIndex done. ");
			PairDataIteratorPerformanceTest.printTime(start);
		}
		finally {
			tableStatement.close();
		}
		// Does not take a significant amount of time in an existing very large database file.
	}
	
	
	private static void addColumn(Connection conn) throws SQLException {
		Statement statement = conn.createStatement();
		try {
			long start = System.currentTimeMillis();
			statement.executeUpdate("ALTER TABLE pairData ADD userValue1 INT NOT NULL;");
			System.out.print("Adding column finished. ");
			PairDataIteratorPerformanceTest.printTime(start);
		}
		finally {
			statement.close();
		}
		// Requires rewriting the whole database file. Duration is similar to creating the database.
	}
	
	
	private static void testSequentialAccess(Connection conn) throws SQLException {
		final int blockSize = 100000;
		
		PreparedStatement statement = conn.prepareStatement("SELECT * FROM pairData LIMIT ?, " + blockSize + ";");
		try {
			long start = System.currentTimeMillis();
			int pos = 0;

			statement.setInt(1, pos);
			ResultSet resultSet = statement.executeQuery();
			try {
				while (resultSet.next()) {
					resultSet.getInt(3);
					pos++;
					while (resultSet.next()) {
						resultSet.getInt(3);
						pos++;
					}
					System.out.print("Finished block. " + pos + " ");
					PairDataIteratorPerformanceTest.printTime(start);
					resultSet.close();
					statement.setInt(1, pos);
					resultSet = statement.executeQuery();
				}
			}
			finally {
				resultSet.close();
			}
			System.out.print("Test finished. ");
			PairDataIteratorPerformanceTest.printTime(start);
		}
		finally {
			statement.close();
		}
		// Seems to take as long as creating the database. No limit leads to the creation of the result set taking as long. Reading with a limit of one takes much longer than loading in larger blocks.
	}
	
	
	public static void main(String[] args) throws SQLException, IOException {
		Connection conn = DriverManager.getConnection("jdbc:h2:./data/h2/test");
		try {
			//fillDatabase(conn);
			//addColumn(conn);
			testSequentialAccess(conn);
			//createAnotherTable(conn);
		}
		finally {
			conn.close();
		}
	}
}
