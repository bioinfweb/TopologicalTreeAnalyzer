package info.bioinfweb.osrfilter.io.treeiterator;


import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import info.bioinfweb.jphyloio.dataadapters.implementations.store.StoreTreeNetworkDataAdapter;
import info.bioinfweb.osrfilter.data.TTATree;
import info.bioinfweb.osrfilter.data.TreeIdentifier;
import info.bioinfweb.osrfilter.io.filter.TreeFilterSet;



public class FilterTreeIteratorTest {
	@Test
	public void testReading() throws IOException, Exception {
		File inputFile = new File("data/NeXMLTrees.xml");
		
		TreeFilterSet set = new TreeFilterSet("data/output/FilteredOutput.nwk");
		set.getTrees().add(new TreeIdentifier(inputFile, "tree1", null));
		set.getTrees().add(new TreeIdentifier(inputFile, "tree3", null));
		
		FilterTreeIterator iterator = new FilterTreeIterator(set, inputFile);
		
		assertTrue(iterator.hasNext());
		TTATree<StoreTreeNetworkDataAdapter> tree = iterator.next();
		assertEquals(8, tree.getTree().getNodes(null).getCount(null));
		assertEquals(7, tree.getTree().getEdges(null).getCount(null));
		assertEquals("First tree", tree.getTreeIdentifier().getName());
		
		assertTrue(iterator.hasNext());
		tree = iterator.next();
		assertEquals(6, tree.getTree().getNodes(null).getCount(null));
		assertEquals(5, tree.getTree().getEdges(null).getCount(null));
		assertEquals("Third tree", tree.getTreeIdentifier().getName());
		
		assertFalse(iterator.hasNext());
	}
}
