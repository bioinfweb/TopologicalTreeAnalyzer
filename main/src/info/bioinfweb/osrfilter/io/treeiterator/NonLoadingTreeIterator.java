package info.bioinfweb.osrfilter.io.treeiterator;


import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import info.bioinfweb.jphyloio.JPhyloIOEventReader;
import info.bioinfweb.jphyloio.events.LabeledIDEvent;
import info.bioinfweb.jphyloio.utils.JPhyloIOReadingUtils;
import info.bioinfweb.osrfilter.data.TTATree;
import info.bioinfweb.osrfilter.data.TreeIdentifier;



/**
 * A tree iterator that iterates over all trees without loading them.
 * <p>
 * This class can, e.g., be used to count trees or to load a list of tree IDs and labels.
 * 
 * @author Ben St&ouml;ver
 */
public class NonLoadingTreeIterator extends AbstractTreeIterator<Object> {
	public NonLoadingTreeIterator(File... files) throws IOException, Exception {
		super(files);
	}

	
	public NonLoadingTreeIterator(List<String> fileNames) throws IOException, Exception {
		super(fileNames);
	}

	
	public NonLoadingTreeIterator(String... fileNames) throws IOException, Exception {
		super(fileNames);
	}

	
	@Override
	protected TTATree<Object> loadTree(JPhyloIOEventReader reader, File file) throws IOException, XMLStreamException {
		LabeledIDEvent startEvent = reader.next().asLabeledIDEvent();
		JPhyloIOReadingUtils.reachElementEnd(reader);
		return new TTATree<Object>(new TreeIdentifier(file, startEvent.getID(), startEvent.getLabel()), null);
	}
}
