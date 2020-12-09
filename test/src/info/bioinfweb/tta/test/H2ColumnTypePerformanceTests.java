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


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import info.bioinfweb.tta.io.PairDataIteratorPerformanceTest;



public class H2ColumnTypePerformanceTests {
	public static final int ROW_COUNT = 1000000;
	
	
	private static void createVarCharTable() throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:h2:./data/h2/test_typeVarChar");
		try {
			System.out.println("Creating VARCHAR table:");
			Statement tableStatement = connection.createStatement();
			try {
				tableStatement.executeUpdate("CREATE TABLE stringTable ( "
						+ "id INT NOT NULL, "
	          + "text VARCHAR NOT NULL);");
				tableStatement.executeUpdate("CREATE PRIMARY KEY ON stringTable (id);");
			}
			finally {
				tableStatement.close();
			}
			
			fillDatabase(connection);
			System.out.println();
		}
		finally {
			connection.close();
		}
	}
	
	
	private static void createClobTable() throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:h2:./data/h2/test_typeClob");
		try {
			System.out.println("Creating CLOB table:");
			Statement tableStatement = connection.createStatement();
			try {
				tableStatement.executeUpdate("CREATE TABLE stringTable ( "
						+ "id INT NOT NULL, "
	          + "text CLOB NOT NULL);");
				tableStatement.executeUpdate("CREATE PRIMARY KEY ON stringTable (id);");
			}
			finally {
				tableStatement.close();
			}
			
			fillDatabase(connection);
			System.out.println();
		}
		finally {
			connection.close();
		}
	}
	
	
	private static void fillDatabase(Connection connection) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(
				"INSERT INTO stringTable VALUES (?, ?);");
		
		try {
			long start = System.currentTimeMillis();
			for (int i = 0; i < ROW_COUNT; i++) {
				statement.setInt(1, i);
				if (i % 10000 == 0) {
					statement.setString(2, "Longer string: abcdefghijklmnopqrstuvwxyz01234567890abcdefghijklmnopqrstuvwxyz01234567890abcdefghijklmnopqrstuvwxyz01234567890abcdefghijklmnopqrstuvwxyz01234567890abcdefghijklmnopqrstuvwxyz01234567890abcdefghijklmnopqrstuvwxyz01234567890abcdefghijklmnopqrstuvwxyz01234567890abcdefghijklmnopqrstuvwxyz01234567890");
				}
				else {
					statement.setString(2, "some text");
				}
				statement.executeUpdate();
			}
			
			System.out.print("Creating database finished. ");
			PairDataIteratorPerformanceTest.printTime(start);
		}
		finally {
			statement.close();
		}
	}
	

	private static void createDoubleTable() throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:h2:./data/h2/test_typeDouble");
		try {
			System.out.println("Creating DOUBLE table:");
			Statement tableStatement = connection.createStatement();
			try {
				tableStatement.executeUpdate("CREATE TABLE doubleTable ( "
						+ "id INT NOT NULL, "
	          + "number DOUBLE NOT NULL);");
				tableStatement.executeUpdate("CREATE PRIMARY KEY ON doubleTable (id);");
			}
			finally {
				tableStatement.close();
			}
			
			PreparedStatement statement = connection.prepareStatement(
					"INSERT INTO doubleTable VALUES (?, ?);");
			
			try {
				long start = System.currentTimeMillis();
				for (int i = 0; i < ROW_COUNT; i++) {
					statement.setInt(1, i);
					statement.setDouble(2, 18.3);
					statement.executeUpdate();
				}
				
				System.out.print("Creating database finished. ");
				PairDataIteratorPerformanceTest.printTime(start);
			}
			finally {
				statement.close();
			}
			System.out.println();
		}
		finally {
			connection.close();
		}
	}
	
	
	private static void createOtherTable() throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:h2:./data/h2/test_typeOther");
		try {
			System.out.println("Creating OTHER table:");
			Statement tableStatement = connection.createStatement();
			try {
				tableStatement.executeUpdate("CREATE TABLE mixedTable ( "
						+ "id INT NOT NULL, "
	          + "value OTHER NOT NULL);");
				tableStatement.executeUpdate("CREATE PRIMARY KEY ON mixedTable (id);");
			}
			finally {
				tableStatement.close();
			}
			
			PreparedStatement statement = connection.prepareStatement(
					"INSERT INTO mixedTable VALUES (?, ?);");
			
			try {
				long start = System.currentTimeMillis();
				for (int i = 0; i < ROW_COUNT; i++) {
					statement.setInt(1, i);
					if (i % 10000 == 0) {
						statement.setObject(2, "Longer string: abcdefghijklmnopqrstuvwxyz01234567890abcdefghijklmnopqrstuvwxyz01234567890abcdefghijklmnopqrstuvwxyz01234567890abcdefghijklmnopqrstuvwxyz01234567890abcdefghijklmnopqrstuvwxyz01234567890abcdefghijklmnopqrstuvwxyz01234567890abcdefghijklmnopqrstuvwxyz01234567890abcdefghijklmnopqrstuvwxyz01234567890", Types.OTHER);
					}
					else if (i % 10 == 0) {
						statement.setObject(2, new Double(18.3), Types.OTHER);
					}
					else {
						statement.setObject(2, "some text", Types.OTHER);
					}
					statement.executeUpdate();
				}
				
				System.out.print("Creating database finished. ");
				PairDataIteratorPerformanceTest.printTime(start);
			}
			finally {
				statement.close();
			}
			System.out.println();
		}
		finally {
			connection.close();
		}
	}
	
	
	public static void main(String[] args) throws SQLException {
		createVarCharTable();
		createClobTable();
		createDoubleTable();
		createOtherTable();
	}
}
