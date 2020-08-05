package info.bioinfweb.osrfilter.data;


import info.bioinfweb.treegraph.document.Tree;



public class OSRFilterTree {
	private TreeIdentifier treeIdentifier;
	private Tree tree;

	
	public OSRFilterTree(TreeIdentifier treeIdentifier, Tree tree) {
		super();
		if (treeIdentifier == null) {
			throw new IllegalArgumentException("treeIdentifier must not be null.");
		}
		else if (tree == null) {
			throw new IllegalArgumentException("tree must not be null.");
		}
		else {
			this.treeIdentifier = treeIdentifier;
			this.tree = tree;
		}
	}


	public TreeIdentifier getTreeIdentifier() {
		return treeIdentifier;
	}


	public Tree getTree() {
		return tree;
	}
}
