package info.bioinfweb.osrfilter.io.treeiterator;


import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import info.bioinfweb.osrfilter.data.TTATree;
import info.bioinfweb.treegraph.document.Tree;



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
