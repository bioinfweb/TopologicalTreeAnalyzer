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
package info.bioinfweb.tta.io.treeiterator;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import info.bioinfweb.tta.io.treeiterator.AnalysisTreeIterator;



public class AnalysisIteratorTest {
	@Test
	public void testReading() throws IOException, Exception {
		AnalysisTreeIterator iterator = new AnalysisTreeIterator(new File("data/Tree1.tre"), new File("data/Tree2.tre"));
		
		iterator.reset();
		for (int i = 1; i <= 6; i++) {
			assertTrue(iterator.hasNext());
			System.out.println(iterator.next().getTreeIdentifier());
		}
		assertFalse(iterator.hasNext());
	}
}
