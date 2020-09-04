package info.bioinfweb.osrfilter.io.treeiterator;


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
import info.bioinfweb.osrfilter.io.filter.TreeFilterSet;



public class FilterTreeIterator extends AbstractTreeIterator<StoreTreeNetworkDataAdapter> {
	private TreeFilterSet filterSet;
	
	
	public FilterTreeIterator(TreeFilterSet filterSet, File... files) throws IOException, Exception {
		super(files);
		this.filterSet = filterSet;
	}

	
	public FilterTreeIterator(TreeFilterSet filterSet, List<String> fileNames) throws IOException, Exception {
		super(fileNames);
		this.filterSet = filterSet;
	}

	
	public FilterTreeIterator(TreeFilterSet filterSet, String... fileNames) throws IOException, Exception {
		super(fileNames);
		this.filterSet = filterSet;
	}


	@Override
	protected TTATree<StoreTreeNetworkDataAdapter> loadTree(JPhyloIOEventReader reader, File file) 
			throws IOException, XMLStreamException {
		
		StoreTreeNetworkDataAdapter adapter = StoreReader.readTreeNetwork(reader);
		LabeledIDEvent startEvent = adapter.getStartEvent(null);
		return new TTATree<StoreTreeNetworkDataAdapter>(new TreeIdentifier(file, startEvent.getID(), startEvent.getLabel()), adapter); 
	}


	@Override
	protected void readNext() throws IOException, Exception {
		do {
			super.readNext();
		} while (hasNext() && !filterSet.getTrees().contains(nextTree.getTreeIdentifier()));
	}
}
