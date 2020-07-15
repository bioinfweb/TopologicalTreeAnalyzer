package info.bioinfweb.osrfilter.io;


import java.io.File;
import java.io.IOException;

import info.bioinfweb.jphyloio.JPhyloIOEventReader;
import info.bioinfweb.jphyloio.ReadWriteParameterMap;
import info.bioinfweb.jphyloio.events.type.EventContentType;
import info.bioinfweb.jphyloio.events.type.EventTopologyType;
import info.bioinfweb.jphyloio.events.type.EventType;
import info.bioinfweb.jphyloio.factory.JPhyloIOReaderWriterFactory;
import info.bioinfweb.osrfilter.data.OSRFilterTree;



/**
 * Iterates over all trees in a set of files.
 * 
 * @author Ben St&ouml;ver
 */
public class TreeIterator {
	private static final EventType TREE_START = new EventType(EventContentType.TREE, EventTopologyType.START);
	
	
	private OSRFilterTree nextTree;
	private File[] files;
	private int filePos = 0;
	private JPhyloIOReaderWriterFactory factory = new JPhyloIOReaderWriterFactory();
	private TreeLoader treeLoader = new TreeLoader();
	private JPhyloIOEventReader reader = null;

	
	public TreeIterator(File... files) throws IOException, Exception {
		super();
		this.files = files;
		readNext();
	}
	
	
	private void closeReader() throws IOException {
		if (reader != null) {
			reader.close();
		}
	}
	
	
	private boolean moveBeforeNextTreeStart() throws ClassCastException, IOException {
		while (reader.hasNextEvent() && !TREE_START.equals(reader.peek().getType())) {
			reader.next();
		}
		return reader.hasNextEvent();
	}
	
	
	private void readNext() throws IOException, Exception {
		while (((reader == null) || !moveBeforeNextTreeStart()) && (filePos < files.length)) {
			closeReader();
			reader = factory.guessReader(files[filePos], new ReadWriteParameterMap());
			filePos++;
		}  // Loop to skip possible files with no trees. 
		
		if ((reader != null) && reader.hasNextEvent()) {
			nextTree = treeLoader.loadTree(reader.next().asLabeledIDEvent(), reader, files[filePos - 1]);
		}
		else {
			nextTree = null;  // No more trees to read.
			closeReader();
		}
	}
	

	public boolean hasNext() {
		return nextTree != null;
	}

	
	public OSRFilterTree next() throws IOException, Exception {
		OSRFilterTree result = nextTree;
		readNext();
		return result;
	}
	
	
	public void reset() throws Exception {
		closeReader();
		reader = null;
		filePos = 0;
		readNext();  // Otherwise possible previous tree would be returned first.
	}
}
