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
package info.bioinfweb.tta.analysis;


import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import info.bioinfweb.tta.data.TreeIdentifier;
import info.bioinfweb.tta.data.TreeOrder;
import info.bioinfweb.tta.data.TreePair;



public class PairIteratorTest {
	private TreeOrder createList() {
		final File file = new File("");
		
		List<TreeIdentifier> result = new ArrayList<TreeIdentifier>();
		result.add(new TreeIdentifier(file, "id0", null));
		result.add(new TreeIdentifier(file, "id1", null));
		result.add(new TreeIdentifier(file, "id2", null));
		result.add(new TreeIdentifier(file, "id3", null));
		return new TreeOrder(result);
	}
	
	
	private void assertNextPair(String expectedIDA, String expectedIDB, PairIterator iterator) {
		assertTrue(iterator.hasNext());
		TreePair pair = iterator.next();
		assertEquals(expectedIDA, pair.getTreeA().getID());
		assertEquals(expectedIDB, pair.getTreeB().getID());
	}
	
	
	@Test
	public void testWithoutReference() {
		PairIterator iterator = new PairIterator(createList());
		assertNextPair("id0", "id1", iterator);
		assertNextPair("id0", "id2", iterator);
		assertNextPair("id0", "id3", iterator);
		assertNextPair("id1", "id2", iterator);
		assertNextPair("id1", "id3", iterator);
		assertNextPair("id2", "id3", iterator);
		assertFalse(iterator.hasNext());
	}
	
	
	@Test
	public void testWithReference() {
		TreeOrder list = createList();
		PairIterator iterator = new PairIterator(list, list.identifierByIndex(2));
		assertNextPair("id2", "id0", iterator);
		assertNextPair("id2", "id1", iterator);
		assertNextPair("id2", "id3", iterator);
		assertFalse(iterator.hasNext());
	}
	
	
	@Test
	public void testEmptyList() {
		PairIterator iterator = new PairIterator(new TreeOrder(new ArrayList<>()));
		assertFalse(iterator.hasNext());
	}
	
	
	@Test
	public void testListWithOneElement() {
		List<TreeIdentifier> list = new ArrayList<TreeIdentifier>();
		list.add(new TreeIdentifier(new File(""), "id0", null));
		PairIterator iterator = new PairIterator(new TreeOrder(list));
		assertFalse(iterator.hasNext());
	}
}
