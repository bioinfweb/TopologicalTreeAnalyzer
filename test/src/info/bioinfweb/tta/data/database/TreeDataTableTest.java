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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import info.bioinfweb.tta.data.TreeData;
import info.bioinfweb.tta.data.TreeIdentifier;



public class TreeDataTableTest implements DatabaseConstants {
	private void addEntry(PreparedStatement rowStatement, int tree, int terminals, int splits) throws SQLException {
		
		rowStatement.setInt(1, tree);
		rowStatement.setInt(2, terminals); 
		rowStatement.setInt(3, splits);
		rowStatement.executeUpdate();
	}
	
	
	private Connection createTestDatabase() throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:h2:mem:");
		DatabaseTools.createTreeDataTable(connection, 8, 16);
		
		PreparedStatement rowStatement = connection.prepareStatement(
				"INSERT INTO " + TABLE_TREE_DATA + " VALUES (?, ?, ?);");
		addEntry(rowStatement, 0,  8, 6);
		addEntry(rowStatement, 1, 10, 7);
		addEntry(rowStatement, 2,  9, 8);
		addEntry(rowStatement, 3,  9, 5);
		addEntry(rowStatement, 4, 10, 9);
		addEntry(rowStatement, 5,  9, 4);

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
	
	
	private static void assertTreeData(List<TreeIdentifier> treeOrder, int terminals, int splits, TreeData data) {
		assertNotNull(data);
		assertEquals(terminals, data.getTerminals());
		assertEquals(splits, data.getSplits());
	}
	
	
	@Test
	public void test_get() throws SQLException {
		List<TreeIdentifier> treeOrder = createTreeOrder(6);
		Connection connection = createTestDatabase();
		try {
			TreeDataTable table = new TreeDataTable(connection, treeOrder);
			assertTreeData(treeOrder, 8, 6, table.get(treeOrder.get(0)));
			assertTreeData(treeOrder, 10, 7, table.get(treeOrder.get(1)));
			assertTreeData(treeOrder, 9, 8, table.get(treeOrder.get(2)));
			assertTreeData(treeOrder, 9, 5, table.get(treeOrder.get(3)));
			assertTreeData(treeOrder, 10, 9, table.get(treeOrder.get(4)));
			assertTreeData(treeOrder, 9, 4, table.get(treeOrder.get(5)));
		}
		finally {
			connection.close();
		}
	}
	
	
	@Test
	public void test_put() throws SQLException {
		List<TreeIdentifier> treeOrder = createTreeOrder(7);
		Connection connection = createTestDatabase();
		try {
			TreeDataTable table = new TreeDataTable(connection, treeOrder);
			assertTreeData(treeOrder, 10, 7, table.get(treeOrder.get(1)));  // Check before replacement.
			
		  // Insert new:
			table.put(treeOrder.get(6), new TreeData(11, 8));
			assertTreeData(treeOrder, 11, 8, table.get(treeOrder.get(6)));
			
		  // Replace:
			table.put(treeOrder.get(1), new TreeData(11, 9));
			assertTreeData(treeOrder, 11, 9, table.get(treeOrder.get(1)));
		}
		finally {
			connection.close();
		}
	}
	
	
	@Test
	public void test_iterator() throws SQLException {
		List<TreeIdentifier> treeOrder = createTreeOrder(6);
		Connection connection = createTestDatabase();
		try {
			TreeDataTable table = new TreeDataTable(connection, treeOrder);
			DatabaseIterator<TreeIdentifier, TreeData> iterator = table.valueIterator(null, COLUMN_TREE_INDEX + " ASC");
			
			assertTrue(iterator.hasNext());
			assertTreeData(treeOrder, 8, 6, iterator.next());
			assertTrue(iterator.hasNext());
			assertTreeData(treeOrder, 10, 7, iterator.next());
			assertTrue(iterator.hasNext());
			assertTreeData(treeOrder, 9, 8, iterator.next());
			assertTrue(iterator.hasNext());
			assertTreeData(treeOrder, 9, 5, iterator.next());
			assertTrue(iterator.hasNext());
			assertTreeData(treeOrder, 10, 9, iterator.next());
			assertTrue(iterator.hasNext());
			assertTreeData(treeOrder, 9, 4, iterator.next());
			assertFalse(iterator.hasNext());
		}
		finally {
			connection.close();
		}
	}
}
