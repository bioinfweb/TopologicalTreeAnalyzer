package info.bioinfweb.tta.io.treeiterator;


import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import info.bioinfweb.jphyloio.JPhyloIOEventReader;
import info.bioinfweb.jphyloio.events.LabeledIDEvent;
import info.bioinfweb.jphyloio.utils.JPhyloIOReadingUtils;
import info.bioinfweb.treegraph.document.Tree;
import info.bioinfweb.tta.data.TTATree;
import info.bioinfweb.tta.data.TreeIdentifier;



/**
 * A tree iterator that iterates over all trees but loads only selected ones.
 * <p>
 * This class can, e.g., be used to count trees or to load a list of tree IDs and labels.
 * 
 * @author Ben St&ouml;ver
 */
public class OptionalLoadingTreeIterator extends AnalysisTreeIterator {
	public static interface TreeSelector {
		public boolean selectTree(File file, String id, String label, int indexInFile);
	}
	
	
	private TreeSelector selector;
	private int indexInFile;
	private File lastUsedFile = null;
	
	
	public OptionalLoadingTreeIterator(TreeSelector selector, File... files) throws IOException, Exception {
		super(files);
		this.selector = selector;
	}

	
	public OptionalLoadingTreeIterator(TreeSelector selector, List<String> fileNames) throws IOException, Exception {
		super(fileNames);
		this.selector = selector;
	}

	
	public OptionalLoadingTreeIterator(TreeSelector selector, String... fileNames) throws IOException, Exception {
		super(fileNames);
		this.selector = selector;
	}

	
	@Override
	protected TTATree<Tree> loadTree(JPhyloIOEventReader reader, File file) throws IOException, XMLStreamException {
		if (lastUsedFile != file) {
			indexInFile = 0;
			lastUsedFile = file;
		}
		
		TTATree<Tree> result;
		LabeledIDEvent startEvent = reader.peek().asLabeledIDEvent();
		if ((selector != null) && selector.selectTree(file, startEvent.getID(), startEvent.getLabel(), indexInFile)) {
			result = super.loadTree(reader, file);
		}
		else {
			reader.next();  // Skip start event.
			JPhyloIOReadingUtils.reachElementEnd(reader);  //TODO Is this actually significantly faster than calling super.loadTree(), i.e., is this class necessary or can AnalysisTreeIterator be used directly?
			result = new TTATree<Tree>(new TreeIdentifier(file, startEvent.getID(), startEvent.getLabel()), null);
		}
		indexInFile++;
		return result;
	}
}
