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
					new File("D:\\Users\\Ben Stöver\\Dokumente\\Arbeit\\Projekte\\TTA\\Tests\\data\\Cloutier_CNEE_TNT_boot_on_opt\\TopologicalPairData.txt"));
					//new File("D:\\LocalUserData\\bstoe_01\\Projekte\\TTA\\Testdaten\\2020-12-01\\Cloutier_CNEE_TNT_boot_on_opt\\TopologicalPairData.txt"));
			
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
				
				System.out.print("Creating database finished.");
				PairDataIteratorPerformanceTest.printTime(start);
			}
			finally {
				iterator.close();
			}
		}
		finally {
			rowStatement.close();
		}
	}
	
	
	public static void main(String[] args) throws SQLException, IOException {
		Connection conn = DriverManager.getConnection("jdbc:h2:./data/h2/test");
		try {
			fillDatabase(conn);
		}
		finally {
			conn.close();
		}
	}
}
