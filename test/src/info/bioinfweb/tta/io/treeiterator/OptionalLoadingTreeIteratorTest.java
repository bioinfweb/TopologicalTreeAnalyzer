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


import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import info.bioinfweb.treegraph.document.Tree;
import info.bioinfweb.tta.data.TTATree;
import info.bioinfweb.tta.io.treeiterator.OptionalLoadingTreeIterator;



public class OptionalLoadingTreeIteratorTest {
	private void checkNextTree(OptionalLoadingTreeIterator iterator, boolean expectedTreeLoaded) throws IOException, Exception {
		assertTrue(iterator.hasNext());
		TTATree<Tree> tree = iterator.next();
		if (expectedTreeLoaded) {
			assertNotNull(tree.getTree());
		}
		else {
			assertNull(tree.getTree());
		}
	}
	
	
	@Test
	public void testReading() throws IOException, Exception {
		OptionalLoadingTreeIterator.TreeSelector selector = new OptionalLoadingTreeIterator.TreeSelector() {
			@Override
			public boolean selectTree(File file, String id, String label, int indexInFile) {
				return file.getAbsolutePath().endsWith("Tree1.tre") && (indexInFile == 1);
			}
		};
		OptionalLoadingTreeIterator iterator = new OptionalLoadingTreeIterator(selector, new File("data/Tree1.tre"), new File("data/Tree2.tre"));
		
		iterator.reset();
		checkNextTree(iterator, false);  // Tree1.0
		checkNextTree(iterator, true);  // Tree1.1
		for (int i = 1; i <= 4; i++) {  // Tree1.2, Tree2.0, Tree2.1, Tree2.2
			checkNextTree(iterator, false);
		}
		assertFalse(iterator.hasNext());
	}
}
