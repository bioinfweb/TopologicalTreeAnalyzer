package info.bioinfweb.osrfilter.analysis;


import org.nfunk.jep.Node;



public class UserExpression {
	private boolean treeTarget;
	private Node root;
	
	
	public UserExpression(boolean hasTreeTarget, Node root) {
		super();
		this.treeTarget = hasTreeTarget;
		this.root = root;
	}


	public boolean hasTreeTarget() {
		return treeTarget;
	}


	public Node getRoot() {
		return root;
	}
}
