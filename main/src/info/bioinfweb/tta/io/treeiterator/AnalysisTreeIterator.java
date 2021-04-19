/*
 * Topological Tree Analyzer - User defined topological analyses of large sets of phylogenetic trees. 
 * Copyright (C) 2020-2021  Ben C. Stöver
 * <http://bioinfweb.info/TTA>
 * 
 * This file is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package info.bioinfweb.tta.io.treeiterator;


import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import info.bioinfweb.jphyloio.JPhyloIOEventReader;
import info.bioinfweb.treegraph.document.Node;
import info.bioinfweb.treegraph.document.Tree;
import info.bioinfweb.treegraph.document.io.jphyloio.JPhyloIOTreeReader;
import info.bioinfweb.treegraph.document.undo.edit.CollapseNodeEdit;
import info.bioinfweb.tta.data.TTATree;
import info.bioinfweb.tta.data.TreeIdentifier;



/**
 * Iterates over all trees in a set of files.
 * 
 * @author Ben St&ouml;ver
 */
public class AnalysisTreeIterator extends AbstractTreeIterator<Tree> {
	private JPhyloIOTreeReader treeReader;
	
	
	public AnalysisTreeIterator(File... files) throws IOException, Exception {
		super(files);
		treeReader = new JPhyloIOTreeReader();
	}

	
	public AnalysisTreeIterator(List<String> fileNames) throws IOException, Exception {
		super(fileNames);
		treeReader = new JPhyloIOTreeReader();
	}

	
	public AnalysisTreeIterator(String... fileNames) throws IOException, Exception {
		super(fileNames);
		treeReader = new JPhyloIOTreeReader();
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
			// Collapse first level:
			Node firstCild = tree.getPaintStart().getChildren().get(0);
			tree.getPaintStart().getChildren().remove(0);
			if (firstCild.getChildren().isEmpty()) {  // If first child was a terminal
				tree.getPaintStart().getChildren().add(firstCild);
				// If the first child is a terminal, it will be moved to the end. In the next iteration the other node (which is not a terminal of the tree has at least 4 terminals) will be first. Then this node will be collapsed.
			}
			else {  // If first child was a subtree
				tree.getPaintStart().getChildren().addAll(0, firstCild.getChildren());
			}
		}
	}
	
	
	@Override
	protected TTATree<Tree> loadTree(JPhyloIOEventReader reader, File file) throws IOException, XMLStreamException {
		// Load tree topology:
		JPhyloIOTreeReader.TreeResult treeTopology = treeReader.readTree(reader.next().asLabeledIDEvent(), reader, null);  //TODO Add a logger later.

		// Collapse unnecessary nodes under root:
		unifyTopology(treeTopology.getTree());
		//TODO Handle cases where trees are too small for comparison, i.e., trees with less than four nodes?
		//     - Note that the loop in unifyTopology() would not terminate if a tree would consist of only two nodes.
		
		return new TTATree<Tree>(new TreeIdentifier(file, treeTopology.getID(), treeTopology.getLabel()), treeTopology.getTree());
	}
}
