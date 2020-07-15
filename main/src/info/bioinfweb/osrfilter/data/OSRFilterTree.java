package info.bioinfweb.osrfilter.data;


import info.bioinfweb.treegraph.document.Tree;



public class OSRFilterTree {
	private TreeIdentifier treeIdentifier;
	private Tree tree;

	
	public OSRFilterTree(TreeIdentifier treeIdentifier, Tree tree) {
		super();
		this.treeIdentifier = treeIdentifier;
		this.tree = tree;
	}


	public TreeIdentifier getTreeIdentifier() {
		return treeIdentifier;
	}


	public Tree getTree() {
		return tree;
	}
}
