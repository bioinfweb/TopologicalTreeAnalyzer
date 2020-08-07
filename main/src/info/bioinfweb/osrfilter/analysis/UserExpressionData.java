package info.bioinfweb.osrfilter.analysis;


import info.bioinfweb.osrfilter.data.PairComparison;



public class UserExpressionData {
	private PairComparison currentComparison;  //TODO Possibly reference whole map here for phase 2.

	
	public PairComparison getCurrentComparison() {
		return currentComparison;
	}

	
	
	public void setCurrentComparison(PairComparison currentComparison) {
		this.currentComparison = currentComparison;
	}
}
