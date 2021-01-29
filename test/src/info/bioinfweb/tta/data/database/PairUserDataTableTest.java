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


import static org.junit.Assert.assertEquals;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import info.bioinfweb.tta.data.TreeIdentifier;
import info.bioinfweb.tta.data.TreePair;
import info.bioinfweb.tta.data.UserExpression;
import info.bioinfweb.tta.data.UserExpressions;
import info.bioinfweb.tta.data.UserValues;



public class PairUserDataTableTest implements DatabaseConstants {
		private UserExpressions createExpressions() {
			UserExpressions result = new UserExpressions();
			
			result.getInputOrder().add("exp0");
			result.getInputOrder().add("exp1");
			result.getInputOrder().add("exp2");
			result.getCalculationOrder().addAll(result.getInputOrder());			
			
			result.getExpressions().put("exp0", new UserExpression(false, "m()", Double.class));
			result.getExpressions().put("exp1", new UserExpression(false, "pairUserValue(\"exp0\") + 1", Double.class));
			result.getExpressions().put("exp2", new UserExpression(true, "name()", String.class));
			
			return result;
		}
		
		
		private Connection createTestDatabase() throws SQLException {
			Connection connection = DriverManager.getConnection("jdbc:h2:mem:");
			DatabaseTools.createUserDataTables(connection, createExpressions());
			
			PreparedStatement rowStatement = connection.prepareStatement(
					"INSERT INTO " + TABLE_PAIR_USER_DATA + " VALUES (?, ?, ?, ?);");
			try {
				rowStatement.setInt(1, 0);
				rowStatement.setInt(2, 1);
				rowStatement.setDouble(3, 8.0);
				rowStatement.setDouble(4, 9.0);
				rowStatement.executeUpdate();
				
				rowStatement.setInt(1, 0);
				rowStatement.setInt(2, 2);
				rowStatement.setDouble(3, 10.0);
				rowStatement.setDouble(4, 11.0);
				rowStatement.executeUpdate();
				
				rowStatement.setInt(1, 1);
				rowStatement.setInt(2, 2);
				rowStatement.setDouble(3, 12.5);
				rowStatement.setDouble(4, 13.5);
				rowStatement.executeUpdate();
			}
			finally {
				rowStatement.close();
			}
			
			rowStatement = connection.prepareStatement(
					"INSERT INTO " + TABLE_TREE_USER_DATA + " VALUES (?, ?);");
			try {
				rowStatement.setInt(1, 0);
				rowStatement.setString(2, "abc");
				rowStatement.executeUpdate();
				
				rowStatement.setInt(1, 1);
				rowStatement.setString(2, "def");
				rowStatement.executeUpdate();
				
				rowStatement.setInt(1, 2);
				rowStatement.setString(2, "GHI");
				rowStatement.executeUpdate();
			}
			finally {
				rowStatement.close();
			}
			
			return connection;
		}
		
		
		private List<TreeIdentifier> createTreeOrder(int size) {
			File file = new File("data/someTree.tre");
			List<TreeIdentifier> result = new ArrayList<TreeIdentifier>(4);
			for (int i = 0; i < size; i++) {
				result.add(new TreeIdentifier(file, "id" + i, null));
			}
			return result;
		}
		
		
		@Test
		public void test_get() throws SQLException {
			List<TreeIdentifier> treeOrder = createTreeOrder(3);
			Connection connection = createTestDatabase();
			try {
				PairUserDataTable table = new PairUserDataTable(connection, treeOrder, createExpressions().pairUserValueNames());
				
				UserValues<TreePair> userValues = table.get(new TreePair(treeOrder.get(0), treeOrder.get(1)));
				assertEquals(treeOrder.get(0), userValues.getKey().getTreeA());
				assertEquals(treeOrder.get(1), userValues.getKey().getTreeB());
				assertEquals(2, userValues.getUserValues().size());
				assertEquals(8.0, userValues.getUserValue("exp0"));
				assertEquals(9.0, userValues.getUserValue("exp1"));
				
				userValues = table.get(new TreePair(treeOrder.get(0), treeOrder.get(2)));
				assertEquals(treeOrder.get(0), userValues.getKey().getTreeA());
				assertEquals(treeOrder.get(2), userValues.getKey().getTreeB());
				assertEquals(2, userValues.getUserValues().size());
				assertEquals(10.0, userValues.getUserValue("exp0"));
				assertEquals(11.0, userValues.getUserValue("exp1"));
				
				userValues = table.get(new TreePair(treeOrder.get(1), treeOrder.get(2)));
				assertEquals(treeOrder.get(1), userValues.getKey().getTreeA());
				assertEquals(treeOrder.get(2), userValues.getKey().getTreeB());
				assertEquals(2, userValues.getUserValues().size());
				assertEquals(12.5, userValues.getUserValue("exp0"));
				assertEquals(13.5, userValues.getUserValue("exp1"));
			}
			finally {
				connection.close();
			}
		}
		
		
		@Test
		public void test_put() throws SQLException {
			List<TreeIdentifier> treeOrder = createTreeOrder(3);
			Connection connection = createTestDatabase();
			try {
				PairUserDataTable table = new PairUserDataTable(connection, treeOrder, createExpressions().pairUserValueNames());
				
				// Load values:
				UserValues<TreePair> userValues = table.get(new TreePair(treeOrder.get(0), treeOrder.get(2)));
				assertEquals(treeOrder.get(0), userValues.getKey().getTreeA());
				assertEquals(treeOrder.get(2), userValues.getKey().getTreeB());
				assertEquals(2, userValues.getUserValues().size());
				assertEquals(10.0, userValues.getUserValue("exp0"));
				assertEquals(11.0, userValues.getUserValue("exp1"));

				// Change value:
				userValues.getUserValues().put("exp0", 15.6);
				userValues.getUserValues().put("exp1", 16.6);
				table.put(userValues);
				
			  // Reload and check:
				userValues = table.get(new TreePair(treeOrder.get(0), treeOrder.get(2)));
				assertEquals(treeOrder.get(0), userValues.getKey().getTreeA());
				assertEquals(treeOrder.get(2), userValues.getKey().getTreeB());
				assertEquals(2, userValues.getUserValues().size());
				assertEquals(15.6, userValues.getUserValue("exp0"));
				assertEquals(16.6, userValues.getUserValue("exp1"));
			}
			finally {
				connection.close();
			}
		}
}
