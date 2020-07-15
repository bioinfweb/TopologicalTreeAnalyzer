package info.bioinfweb.osrfilter.io;


import java.io.File;
import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import info.bioinfweb.jphyloio.JPhyloIOEventReader;
import info.bioinfweb.jphyloio.events.LabeledIDEvent;
import info.bioinfweb.osrfilter.data.OSRFilterTree;
import info.bioinfweb.osrfilter.data.TreeIdentifier;
import info.bioinfweb.treegraph.document.Node;
import info.bioinfweb.treegraph.document.Tree;
import info.bioinfweb.treegraph.document.io.jphyloio.JPhyloIOTreeReader;
import info.bioinfweb.treegraph.document.undo.edit.CollapseNodeEdit;



public class TreeLoader {
	private JPhyloIOTreeReader treeReader = new JPhyloIOTreeReader();
	
	
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
	
	
	public OSRFilterTree loadTree(LabeledIDEvent startEvent, JPhyloIOEventReader reader, File file) throws IOException, XMLStreamException {
		// Load tree topology:
		JPhyloIOTreeReader.TreeResult treeTopology = treeReader.readTree(startEvent, reader, null);  //TODO Add a logger later.

		// Collapse unnecessary nodes under root:
		unifyTopology(treeTopology.getTree());
		//TODO Handle cases where trees are too small for comparison, i.e., trees with less than four nodes?
		
		return new OSRFilterTree(new TreeIdentifier(file, treeTopology.getID()), treeTopology.getTree());
	}
}
