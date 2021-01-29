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


import static org.junit.Assert.*;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import info.bioinfweb.tta.data.PairData;
import info.bioinfweb.tta.data.TreeIdentifier;
import info.bioinfweb.tta.data.TreeOrder;
import info.bioinfweb.tta.data.TreePair;



public class PairDataTableTest implements DatabaseConstants {
	private void addEntry(PreparedStatement rowStatement, int treeA, int treeB, int matches, int conflictsAB, int conflictsBA, 
			int nonMatchesAB, int nonMatchesBA, int sharedTerminals) throws SQLException {
		
		rowStatement.setInt(1, treeA);
		rowStatement.setInt(2, treeB); 
		rowStatement.setInt(3, matches);
		rowStatement.setInt(4, conflictsAB);
		rowStatement.setInt(5, conflictsBA);
		rowStatement.setInt(6, nonMatchesAB);
		rowStatement.setInt(7, nonMatchesBA);
		rowStatement.setInt(8, sharedTerminals);
		rowStatement.executeUpdate();
	}
	
	
	private Connection createTestDatabase() throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:h2:mem:");
		DatabaseTools.createPairDataTable(connection, 8, 16);
		
		PreparedStatement rowStatement = connection.prepareStatement(
				"INSERT INTO " + TABLE_PAIR_DATA + " VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
		addEntry(rowStatement, 0, 1,  8, 2, 2, 0, 1, 5);
		addEntry(rowStatement, 0, 2, 10, 0, 0, 0, 0, 5);
		addEntry(rowStatement, 0, 3,  9, 1, 0, 1, 0, 6);
		addEntry(rowStatement, 1, 2,  9, 0, 1, 0, 0, 5);
		addEntry(rowStatement, 1, 3, 10, 1, 0, 0, 0, 6);
		addEntry(rowStatement, 2, 3,  9, 0, 0, 0, 0, 5);

		return connection;
	}
	
	
	private Connection createLargeTestDatabase(int size) throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:h2:mem:");
		DatabaseTools.createPairDataTable(connection, 8, 16);

		PreparedStatement rowStatement = connection.prepareStatement(
				"INSERT INTO " + TABLE_PAIR_DATA + " VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
		for (int i = 0; i < size; i++) {
			addEntry(rowStatement, 0, i,  8, 2, 2, 0, 1, 5);
		}

		return connection;
	}
	
	
	private TreeOrder createTreeOrder(int size) {
		File file = new File("data/someTree.tre");
		List<TreeIdentifier> result = new ArrayList<TreeIdentifier>(4);
		for (int i = 0; i < size; i++) {
			result.add(new TreeIdentifier(file, "id" + i, null));
		}
		return new TreeOrder(result);
	}
	
	
	private static void assertPairData(TreeOrder treeOrder, 
			int treeA, int treeB, int matches, int conflictsAB, int conflictsBA, int nonMatchesAB, int nonMatchesBA, int sharedTerminals, PairData data) {
		
		assertNotNull(data);
		assertNotNull(data.getPair());
		assertEquals(treeOrder.identifierByIndex(treeA), data.getPair().getTreeA());
		assertEquals(treeOrder.identifierByIndex(treeB), data.getPair().getTreeB());
		assertEquals(matches, data.getMatchingSplits());
		assertEquals(conflictsAB, data.getConflictingSplitsAB());
		assertEquals(conflictsBA, data.getConflictingSplitsBA());
		assertEquals(nonMatchesAB, data.getNotMatchingSplitsAB());
		assertEquals(nonMatchesBA, data.getNotMatchingSplitsBA());
		assertEquals(sharedTerminals, data.getSharedTerminals());
	}
	
	
	@Test
	public void test_get() throws SQLException {
		TreeOrder treeOrder = createTreeOrder(5);
		Connection connection = createTestDatabase();
		try {
			PairDataTable table = new PairDataTable(connection, treeOrder);
			assertPairData(treeOrder, 0, 1,  8, 2, 2, 0, 1, 5, table.get(new TreePair(treeOrder.identifierByIndex(0), treeOrder.identifierByIndex(1))));
			assertPairData(treeOrder, 0, 2, 10, 0, 0, 0, 0, 5, table.get(new TreePair(treeOrder.identifierByIndex(0), treeOrder.identifierByIndex(2))));
			assertPairData(treeOrder, 0, 3,  9, 1, 0, 1, 0, 6, table.get(new TreePair(treeOrder.identifierByIndex(0), treeOrder.identifierByIndex(3))));
			assertPairData(treeOrder, 1, 2,  9, 0, 1, 0, 0, 5, table.get(new TreePair(treeOrder.identifierByIndex(1), treeOrder.identifierByIndex(2))));
			assertPairData(treeOrder, 1, 3, 10, 1, 0, 0, 0, 6, table.get(new TreePair(treeOrder.identifierByIndex(1), treeOrder.identifierByIndex(3))));
			assertPairData(treeOrder, 2, 3,  9, 0, 0, 0, 0, 5, table.get(new TreePair(treeOrder.identifierByIndex(2), treeOrder.identifierByIndex(3))));
		}
		finally {
			connection.close();
		}
	}
	
	
	@Test
	public void test_put() throws SQLException {
		TreeOrder treeOrder = createTreeOrder(5);
		Connection connection = createTestDatabase();
		try {
			PairDataTable table = new PairDataTable(connection, treeOrder);
			assertPairData(treeOrder, 0, 2, 10, 0, 0, 0, 0, 5, table.get(new TreePair(treeOrder.identifierByIndex(0), treeOrder.identifierByIndex(2))));  // Check before replacement.
			
		  // Insert new:
			TreePair pair = new TreePair(treeOrder.identifierByIndex(0), treeOrder.identifierByIndex(4));
			table.put(new PairData(pair, 7, 2, 1, 3, 0, 5));
			assertPairData(treeOrder, 0, 4, 7, 2, 3, 1, 0, 5, table.get(new TreePair(treeOrder.identifierByIndex(0), treeOrder.identifierByIndex(4))));
			
		  // Replace:
			pair = new TreePair(treeOrder.identifierByIndex(0), treeOrder.identifierByIndex(2));
			table.put(new PairData(pair, 10, 1, 0, 0, 1, 6));
			assertPairData(treeOrder, 0, 2, 10, 1, 0, 0, 1, 6, table.get(new TreePair(treeOrder.identifierByIndex(0), treeOrder.identifierByIndex(2))));
		}
		finally {
			connection.close();
		}
	}
	
	
	@Test
	public void test_iterator() throws SQLException {
		TreeOrder treeOrder = createTreeOrder(5);
		Connection connection = createTestDatabase();
		try {
			PairDataTable table = new PairDataTable(connection, treeOrder);
			DatabaseIterator<TreePair, PairData> iterator = table.valueIterator(null, COLUMN_TREE_INDEX_A + ", " + COLUMN_TREE_INDEX_B + " ASC");
			
			assertTrue(iterator.hasNext());
			assertPairData(treeOrder, 0, 1,  8, 2, 2, 0, 1, 5, iterator.next());
			assertTrue(iterator.hasNext());
			assertPairData(treeOrder, 0, 2, 10, 0, 0, 0, 0, 5, iterator.next());
			assertTrue(iterator.hasNext());
			assertPairData(treeOrder, 0, 3,  9, 1, 0, 1, 0, 6, iterator.next());
			assertTrue(iterator.hasNext());
			assertPairData(treeOrder, 1, 2,  9, 0, 1, 0, 0, 5, iterator.next());
			assertTrue(iterator.hasNext());
			assertPairData(treeOrder, 1, 3, 10, 1, 0, 0, 0, 6, iterator.next());
			assertTrue(iterator.hasNext());
			assertPairData(treeOrder, 2, 3,  9, 0, 0, 0, 0, 5, iterator.next());
			assertFalse(iterator.hasNext());
		}
		finally {
			connection.close();
		}
	}
	
	
	@Test
	public void test_iterator_limit() throws SQLException {
		TreeOrder treeOrder = createTreeOrder(5);
		Connection connection = createTestDatabase();
		try {
			PairDataTable table = new PairDataTable(connection, treeOrder);
			DatabaseIterator<TreePair, PairData> iterator = table.valueIterator(2, 2, null, COLUMN_TREE_INDEX_A + ", " + COLUMN_TREE_INDEX_B + " ASC");
			
			assertTrue(iterator.hasNext());
			assertPairData(treeOrder, 0, 3,  9, 1, 0, 1, 0, 6, iterator.next());
			assertTrue(iterator.hasNext());
			assertPairData(treeOrder, 1, 2,  9, 0, 1, 0, 0, 5, iterator.next());
			assertFalse(iterator.hasNext());
		}
		finally {
			connection.close();
		}
	}
	
	
	@Test
	public void test_iterator_condition() throws SQLException {
		TreeOrder treeOrder = createTreeOrder(5);
		Connection connection = createTestDatabase();
		try {
			PairDataTable table = new PairDataTable(connection, treeOrder);
			DatabaseIterator<TreePair, PairData> iterator = table.valueIterator(COLUMN_TREE_INDEX_A + "=1 OR " + COLUMN_TREE_INDEX_B + "=1", 
					COLUMN_TREE_INDEX_A + ", " + COLUMN_TREE_INDEX_B + " ASC");
			
			assertTrue(iterator.hasNext());
			assertPairData(treeOrder, 0, 1,  8, 2, 2, 0, 1, 5, iterator.next());
			assertTrue(iterator.hasNext());
			assertPairData(treeOrder, 1, 2,  9, 0, 1, 0, 0, 5, iterator.next());
			assertTrue(iterator.hasNext());
			assertPairData(treeOrder, 1, 3, 10, 1, 0, 0, 0, 6, iterator.next());
			assertFalse(iterator.hasNext());
		}
		finally {
			connection.close();
		}
	}
	
	
	public void testIterator(int tableSize) throws SQLException {
		TreeOrder treeOrder = createTreeOrder(tableSize);
		Connection connection = createLargeTestDatabase(tableSize);
		try {
			PairDataTable table = new PairDataTable(connection, treeOrder);
			DatabaseIterator<TreePair, PairData> iterator = table.valueIterator(null, COLUMN_TREE_INDEX_A + ", " + COLUMN_TREE_INDEX_B + " ASC");
			
			for (int i = 0; i < tableSize; i++) {
				assertTrue(iterator.hasNext());
				assertPairData(treeOrder, 0, i, 8, 2, 2, 0, 1, 5, iterator.next());
			}
			assertFalse(iterator.hasNext());
		}
		finally {
			connection.close();
		}
	}
	
	
	@Test
	public void test_iterator_multipleResultSets() throws SQLException {
		testIterator(2 * DatabaseIterator.MAX_ITERATOR_GROUP_SIZE + 5);
	}
	
	
	@Test
	public void test_iterator_exactlyOneSet() throws SQLException {
		testIterator(DatabaseIterator.MAX_ITERATOR_GROUP_SIZE);
	}
}
