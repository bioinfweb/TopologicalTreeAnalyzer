package info.bioinfweb.osrfilter.io.filter.treeiterator;


import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import info.bioinfweb.jphyloio.JPhyloIOEventReader;
import info.bioinfweb.jphyloio.dataadapters.implementations.store.StoreReader;
import info.bioinfweb.jphyloio.dataadapters.implementations.store.StoreTreeNetworkDataAdapter;
import info.bioinfweb.jphyloio.events.LabeledIDEvent;
import info.bioinfweb.osrfilter.data.TTATree;
import info.bioinfweb.osrfilter.data.TreeIdentifier;



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
	protected TTATree<StoreTreeNetworkDataAdapter> loadTree(JPhyloIOEventReader reader, File file) 
			throws IOException, XMLStreamException {
		
		StoreTreeNetworkDataAdapter adapter = StoreReader.readTreeNetwork(reader);
		LabeledIDEvent startEvent = adapter.getStartEvent(null);
		return new TTATree<StoreTreeNetworkDataAdapter>(new TreeIdentifier(file, startEvent.getID(), startEvent.getLabel()), adapter); 
	}
}
