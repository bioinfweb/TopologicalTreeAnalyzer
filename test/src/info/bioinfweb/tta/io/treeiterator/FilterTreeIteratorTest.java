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
package info.bioinfweb.tta.io.treeiterator;


import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import info.bioinfweb.jphyloio.formats.JPhyloIOFormatIDs;
import info.bioinfweb.tta.data.TTATree;
import info.bioinfweb.tta.data.TreeIdentifier;
import info.bioinfweb.tta.io.filter.TreeFilterSet;
import info.bioinfweb.tta.io.filter.document.UniqueIDTreeAdapterDecorator;
import info.bioinfweb.tta.io.treeiterator.FilterTreeIterator;



public class FilterTreeIteratorTest {
	@Test
	public void testReading() throws IOException, Exception {
		File inputFile = new File("data/NeXMLTrees.xml");
		
		TreeFilterSet set = new TreeFilterSet("data/output/FilteredOutput.nwk", JPhyloIOFormatIDs.NEWICK_FORMAT_ID);
		set.getTrees().add(new TreeIdentifier(inputFile, "tree1", null));
		set.getTrees().add(new TreeIdentifier(inputFile, "tree3", null));
		
		FilterTreeIterator iterator = new FilterTreeIterator(set, inputFile);
		
		assertTrue(iterator.hasNext());
		TTATree<UniqueIDTreeAdapterDecorator> tree = iterator.next();
		assertEquals("First tree", tree.getTreeIdentifier().getName());
		assertEquals(8, tree.getTree().getNodes(null).getCount(null));
		assertEquals(7, tree.getTree().getEdges(null).getCount(null));
		
		assertTrue(iterator.hasNext());
		tree = iterator.next();
		assertEquals("Third tree", tree.getTreeIdentifier().getName());
		assertEquals(6, tree.getTree().getNodes(null).getCount(null));
		assertEquals(5, tree.getTree().getEdges(null).getCount(null));
		
		assertFalse(iterator.hasNext());
	}
}
