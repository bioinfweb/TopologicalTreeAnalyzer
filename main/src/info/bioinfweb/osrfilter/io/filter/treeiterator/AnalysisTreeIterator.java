package info.bioinfweb.osrfilter.io.filter.treeiterator;


import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import info.bioinfweb.jphyloio.JPhyloIOEventReader;
import info.bioinfweb.osrfilter.data.TTATree;
import info.bioinfweb.osrfilter.data.TreeIdentifier;
import info.bioinfweb.treegraph.document.Node;
import info.bioinfweb.treegraph.document.Tree;
import info.bioinfweb.treegraph.document.io.jphyloio.JPhyloIOTreeReader;
import info.bioinfweb.treegraph.document.undo.edit.CollapseNodeEdit;



/**
 * Iterates over all trees in a set of files.
 * 
 * @author Ben St&ouml;ver
 */
public class AnalysisTreeIterator extends AbstractTreeIterator<Tree> {
	private JPhyloIOTreeReader treeReader;
	
	
	public AnalysisTreeIterator(File... files) throws IOException, Exception {
		super(files);
	}

	
	public AnalysisTreeIterator(List<String> fileNames) throws IOException, Exception {
		super(fileNames);
	}

	
	public AnalysisTreeIterator(String... fileNames) throws IOException, Exception {
		super(fileNames);
	}

	
	private JPhyloIOTreeReader getTreeReader() {
		if (treeReader == null) {  // Creation must be done here instead of in the constructor since the super-constructor already uses this instance.
			treeReader = new JPhyloIOTreeReader();
		}
		return treeReader;
	}
	

	/**
	 * Collapses the first child of the root node as long as two or less children are present. This makes sure that all modeled nodes
	 * are actual topological nodes of the tree is considered unrooted.
	 * <p>
	 * Note that this implementation does not edit legends or handle branch lengths, as {@link CollapseNodeEdit} does.
	 * 
	 * @param tree the tree to be edited
	 */
	private void unifyTopology(Tree tree) {
		while (!tree.getPaintStart().getChildren().isEmpty() && (tree.getPaintStart().getChildren().size() <= 2)) {
			// Collapse first child:
			Node firstCild = tree.getPaintStart().getChildren().get(0);
			tree.getPaintStart().getChildren().remove(0);
			tree.getPaintStart().getChildren().addAll(0, firstCild.getChildren());
		}
	}
	
	
	@Override
	protected TTATree<Tree> loadTree(JPhyloIOEventReader reader, File file) throws IOException, XMLStreamException {
		// Load tree topology:
		JPhyloIOTreeReader.TreeResult treeTopology = getTreeReader().readTree(reader.next().asLabeledIDEvent(), reader, null);  //TODO Add a logger later.

		// Collapse unnecessary nodes under root:
		unifyTopology(treeTopology.getTree());
		//TODO Handle cases where trees are too small for comparison, i.e., trees with less than four nodes?
		
		return new TTATree<Tree>(new TreeIdentifier(file, treeTopology.getID(), treeTopology.getLabel()), treeTopology.getTree());
	}
}
