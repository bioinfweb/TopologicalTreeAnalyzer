package info.bioinfweb.osrfilter.analysis;


import info.bioinfweb.osrfilter.data.AnalysesData;
import info.bioinfweb.osrfilter.data.PairComparisonData;
import info.bioinfweb.osrfilter.data.TreeData;
import info.bioinfweb.osrfilter.data.TreeIdentifier;
import info.bioinfweb.osrfilter.data.TreePair;



public class UserExpressionDataProvider {
	private boolean treeExpression;
	private AnalysesData analysesData;
	private TreeIdentifier[] treeIdentifiers = new TreeIdentifier[2];
	private TreePair currentTreePair;

	
	public UserExpressionDataProvider() {
		super();
	}


	public boolean isTreeExpression() {
		return treeExpression;
	}


	public void setTreeExpression(boolean treeExpression) {
		this.treeExpression = treeExpression;
	}


	public AnalysesData getAnalysesData() {
		return analysesData;
	}


	public void setAnalysesData(AnalysesData analysesData) {
		this.analysesData = analysesData;
	}


	public TreeIdentifier getTreeIdentifier(int index) {
		return treeIdentifiers[index];
	}


	public void setTreeIdentifier(int index, TreeIdentifier identifier) {
		treeIdentifiers[index] = identifier;
		currentTreePair = new TreePair(treeIdentifiers[0], treeIdentifiers[1]);
	}
	
	
	public PairComparisonData getCurrentComparisonData() {
		return analysesData.getComparisonMap().get(currentTreePair);
	}
	
	
	public TreeData getCurrentTreeData(int index) {
		return analysesData.getTreeMap().get(getTreeIdentifier(index));
	}
}
