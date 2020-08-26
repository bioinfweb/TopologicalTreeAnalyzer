package info.bioinfweb.osrfilter.analysis;


import org.nfunk.jep.Node;



public class UserExpression {
	private boolean treeTarget;
	private String expression;
	private Node root;
	
	
	public UserExpression(boolean hasTreeTarget, String expression, Node root) {
		super();
		this.treeTarget = hasTreeTarget;
		this.expression = expression;
		this.root = root;
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
}
