package info.bioinfweb.osrfilter.data;



public class TTATree<T> {
	private TreeIdentifier treeIdentifier;
	private T tree;

	
	public TTATree(TreeIdentifier treeIdentifier, T tree) {
		super();
		if (treeIdentifier == null) {
			throw new IllegalArgumentException("treeIdentifier must not be null.");
		}
		else {
			this.treeIdentifier = treeIdentifier;
			this.tree = tree;
		}
	}


	public TreeIdentifier getTreeIdentifier() {
		return treeIdentifier;
	}


	public T getTree() {
		return tree;
	}
}
