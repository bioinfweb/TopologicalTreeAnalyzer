package info.bioinfweb.osrfilter.io.treeiterator;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import info.bioinfweb.jphyloio.JPhyloIOEventReader;
import info.bioinfweb.jphyloio.dataadapters.implementations.store.StoreReader;
import info.bioinfweb.jphyloio.events.LabeledIDEvent;
import info.bioinfweb.osrfilter.data.TTATree;
import info.bioinfweb.osrfilter.data.TreeIdentifier;
import info.bioinfweb.osrfilter.io.filter.TreeFilterSet;
import info.bioinfweb.osrfilter.io.filter.document.PrefixEventReplacer;
import info.bioinfweb.osrfilter.io.filter.document.UniqueIDTreeAdapterDecorator;



public class FilterTreeIterator extends AbstractTreeIterator<UniqueIDTreeAdapterDecorator> {
	public static final String FILE_PREFIX = "f";
	
	
	private TreeFilterSet filterSet;
	private Map<File, String> fileIDPrefixes;
	
	
	private void initPrefixMap() {
		fileIDPrefixes = new HashMap<File, String>(files.length);
		for (int i = 0; i < files.length; i++) {
			fileIDPrefixes.put(files[i], FILE_PREFIX + i);
		}
	}
	
	
	public FilterTreeIterator(TreeFilterSet filterSet, File... files) throws IOException, Exception {
		super(files);
		this.filterSet = filterSet;
		initPrefixMap();
	}

	
	public FilterTreeIterator(TreeFilterSet filterSet, List<String> fileNames) throws IOException, Exception {
		super(fileNames);
		this.filterSet = filterSet;
		initPrefixMap();
	}

	
	public FilterTreeIterator(TreeFilterSet filterSet, String... fileNames) throws IOException, Exception {
		super(fileNames);
		this.filterSet = filterSet;
		initPrefixMap();
	}


	@Override
	protected TTATree<UniqueIDTreeAdapterDecorator> loadTree(JPhyloIOEventReader reader, File file) 
			throws IOException, XMLStreamException {
		
		UniqueIDTreeAdapterDecorator adapter = new UniqueIDTreeAdapterDecorator(StoreReader.readTreeNetwork(reader), 
				new PrefixEventReplacer(fileIDPrefixes.get(file)));
		LabeledIDEvent startEvent = adapter.getUnderlyingAdapter().getStartEvent(null);  // Using underlying adapter to get unmodified ID.
		return new TTATree<UniqueIDTreeAdapterDecorator>(new TreeIdentifier(file, startEvent.getID(), startEvent.getLabel()), adapter);
	}


	@Override
	protected void readNext() throws IOException, Exception {
		do {
			super.readNext();
		} while (hasNext() && !filterSet.getTrees().contains(nextTree.getTreeIdentifier()));
	}
}
