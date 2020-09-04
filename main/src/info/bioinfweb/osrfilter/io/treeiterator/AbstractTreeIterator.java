package info.bioinfweb.osrfilter.io.treeiterator;


import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import info.bioinfweb.jphyloio.JPhyloIOEventReader;
import info.bioinfweb.jphyloio.ReadWriteParameterMap;
import info.bioinfweb.jphyloio.events.type.EventContentType;
import info.bioinfweb.jphyloio.events.type.EventTopologyType;
import info.bioinfweb.jphyloio.events.type.EventType;
import info.bioinfweb.jphyloio.factory.JPhyloIOReaderWriterFactory;
import info.bioinfweb.osrfilter.data.TTATree;



public abstract class AbstractTreeIterator<T> {
	private static final EventType TREE_START = new EventType(EventContentType.TREE, EventTopologyType.START);
	
	
	protected TTATree<T> nextTree;
	private File[] files;
	private int filePos = 0;
	private JPhyloIOReaderWriterFactory factory = new JPhyloIOReaderWriterFactory();
	private JPhyloIOEventReader reader = null;
	private boolean isUnusedInstance = true;

	
	public AbstractTreeIterator(File... files) throws IOException, Exception {
		super();
		this.files = files;
	}
	

	public AbstractTreeIterator(List<String> fileNames) throws IOException, Exception {
		this(fileNames.toArray(new String[fileNames.size()]));
	}
	
	
	public AbstractTreeIterator(String... fileNames) throws IOException, Exception {
		super();
		files = new File[fileNames.length];
		for (int i = 0; i < fileNames.length; i++) {
			files[i] = new File(fileNames[i]);
		}
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
	
	
	protected abstract TTATree<T> loadTree(JPhyloIOEventReader reader, File file) throws IOException, XMLStreamException;

	
	private void ensureFirstElement() throws IOException, Exception {
		if (isUnusedInstance) {
			isUnusedInstance = false;
			readNext();
		}
	}
	
	
	protected void readNext() throws IOException, Exception {
		while (((reader == null) || !moveBeforeNextTreeStart()) && (filePos < files.length)) {
			closeReader();
			reader = factory.guessReader(files[filePos], new ReadWriteParameterMap());
			filePos++;
		}  // Loop to skip possible files with no trees. 
		
		if ((reader != null) && reader.hasNextEvent()) {
			nextTree = loadTree(reader, files[filePos - 1]);
		}
		else {
			nextTree = null;  // No more trees to read.
			closeReader();
		}
	}
	

	public boolean hasNext() throws IOException, Exception {
		ensureFirstElement();
		return nextTree != null;
	}

	
	public TTATree<T> next() throws IOException, Exception {
		ensureFirstElement();
		TTATree<T> result = nextTree;
		readNext();
		return result;
	}
	
	
	public void reset() throws Exception {
		closeReader();
		reader = null;
		filePos = 0;
		ensureFirstElement();
	}
}
