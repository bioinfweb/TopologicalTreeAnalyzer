package info.bioinfweb.osrfilter.io;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;



public class TreeIteratorTest {
	@Test
	public void testReading() throws IOException, Exception {
		TreeIterator iterator = new TreeIterator(new File("data/Tree1.tre"), new File("data/Tree2.tre"));
		
		iterator.reset();
		for (int i = 1; i <= 6; i++) {
			assertTrue(iterator.hasNext());
			System.out.println(iterator.next().getTreeIdentifier());
		}
		assertFalse(iterator.hasNext());
	}
}
