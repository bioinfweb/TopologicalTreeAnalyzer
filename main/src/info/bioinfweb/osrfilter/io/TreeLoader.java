package info.bioinfweb.osrfilter.io;


import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import info.bioinfweb.jphyloio.JPhyloIOEventReader;
import info.bioinfweb.jphyloio.events.LabeledIDEvent;
import info.bioinfweb.osrfilter.data.SplitsTree;
import info.bioinfweb.treegraph.document.Node;
import info.bioinfweb.treegraph.document.Tree;
import info.bioinfweb.treegraph.document.io.jphyloio.JPhyloIOTreeReader;
import info.bioinfweb.treegraph.document.nodebranchdata.NodeBranchDataAdapter;
import info.bioinfweb.treegraph.document.topologicalcalculation.TopologicalCalculator;
import info.bioinfweb.treegraph.document.undo.edit.CollapseNodeEdit;



public class TreeLoader {
	private JPhyloIOTreeReader treeReader = new JPhyloIOTreeReader();
	private TopologicalCalculator topologicalCalculator;
	
	
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
	
	
	private void collectLeafSets(Node root, SplitsTree splitsTree) {
		for (Node child : root.getChildren()) {
			splitsTree.getSplits().add(topologicalCalculator.getLeafSet(child));  // The root of the tree will not be collected.
			collectLeafSets(child, splitsTree);
		}
	}
	
	
	public SplitsTree loadTree(LabeledIDEvent startEvent, JPhyloIOEventReader reader, TopologicalCalculator topologicalCalculator, 
			NodeBranchDataAdapter leafAdapter) throws IOException, XMLStreamException {

		// Load tree topology:
		JPhyloIOTreeReader.TreeResult treeTopology = treeReader.readTree(startEvent, reader, null);  //TODO Add a logger later.
		this.topologicalCalculator = topologicalCalculator;

		// Collapse unnecessary nodes under root:
		unifyTopology(treeTopology.getTree());
		//TODO Handle cases where trees are too small for comparison, i.e., trees with less than four nodes?
		
		// Create leaf sets:
		topologicalCalculator.addLeafSets(treeTopology.getTree().getPaintStart(), leafAdapter);
		
		// Store leaf sets:
		SplitsTree result = new SplitsTree();
		result.setTerminalSet(topologicalCalculator.getLeafSet(treeTopology.getTree().getPaintStart()));  // The leaf set of the root should contain all terminals.
		collectLeafSets(treeTopology.getTree().getPaintStart(), result);
		
		return result;
	}
}
