package info.bioinfweb.osrfilter.io.filter.treeiterator;


import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import info.bioinfweb.jphyloio.JPhyloIOEventReader;
import info.bioinfweb.jphyloio.dataadapters.implementations.store.StoreReader;
import info.bioinfweb.jphyloio.dataadapters.implementations.store.StoreTreeNetworkDataAdapter;



public class FilterTreeIterator extends AbstractTreeIterator<StoreTreeNetworkDataAdapter> {
	public FilterTreeIterator(File... files) throws IOException, Exception {
		super(files);
	}

	
	public FilterTreeIterator(List<String> fileNames) throws IOException, Exception {
		super(fileNames);
	}

	
	public FilterTreeIterator(String... fileNames) throws IOException, Exception {
		super(fileNames);
	}


	@Override
	protected StoreTreeNetworkDataAdapter loadTree(JPhyloIOEventReader reader, File file) 
			throws IOException, XMLStreamException {
		
		return StoreReader.readTreeNetwork(reader); 
	}
}
