package info.bioinfweb.osrfilter.analysis;


import info.bioinfweb.osrfilter.data.PairComparison;



public class UserExpressionDataProvider {
	private PairComparison currentComparison = null;  //TODO Possibly reference whole map here for phase 2.
	private String[] treeID = new String[2];
	private String[] treeName = new String[2];

	
	public PairComparison getCurrentComparison() {
		return currentComparison;
	}

	
	
	public void setCurrentComparison(PairComparison currentComparison) {
		this.currentComparison = currentComparison;
	}



	public String getTreeID(int index) {
		return treeID[index];
	}



	public void setTreeID(int index, String id) {
		treeID[index] = id;
	}



	public String getTreeName(int index) {
		return treeName[index];
	}



	public void setTreeName(int index, String name) {
		treeName[index] = name;
	}
}
