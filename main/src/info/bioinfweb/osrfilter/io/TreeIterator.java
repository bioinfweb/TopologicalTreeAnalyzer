package info.bioinfweb.osrfilter.io;


import java.util.Iterator;

import info.bioinfweb.jphyloio.JPhyloIOEventReader;
import info.bioinfweb.osrfilter.data.SplitsTree;



/**
 * Iterates over all trees in a set of files.
 * 
 * @author Ben St&ouml;ver
 */
public class TreeIterator implements Iterator<SplitsTree>{
	private JPhyloIOEventReader reader;

	
	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public SplitsTree next() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public void reset() {
		
	}
}
