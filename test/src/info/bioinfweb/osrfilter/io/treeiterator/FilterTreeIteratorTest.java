package info.bioinfweb.osrfilter.io.treeiterator;


import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

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
		assertEquals("First tree", iterator.next().getTreeIdentifier().getName());
		assertTrue(iterator.hasNext());
		assertEquals("Third tree", iterator.next().getTreeIdentifier().getName());
		assertFalse(iterator.hasNext());
	}
}
