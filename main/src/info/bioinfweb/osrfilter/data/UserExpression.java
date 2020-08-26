package info.bioinfweb.osrfilter.data;


import org.nfunk.jep.Node;



public class UserExpression {
	private boolean treeTarget;
	private String expression;
	private Node root;
	
	
	public UserExpression(boolean hasTreeTarget, String expression) {
		super();
		this.treeTarget = hasTreeTarget;
		this.expression = expression;
		this.root = null;
	}


	public boolean hasTreeTarget() {
		return treeTarget;
	}


	public String getExpression() {
		return expression;
	}


	public Node getRoot() {
		return root;
	}


	public void setRoot(Node root) {
		this.root = root;
	}
}
