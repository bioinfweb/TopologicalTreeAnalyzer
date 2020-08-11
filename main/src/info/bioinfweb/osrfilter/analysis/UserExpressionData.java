package info.bioinfweb.osrfilter.analysis;


import info.bioinfweb.osrfilter.data.OSRFilterTree;
import info.bioinfweb.osrfilter.data.PairComparison;



public class UserExpressionData {
	private PairComparison currentComparison;  //TODO Possibly reference whole map here for phase 2.
	private OSRFilterTree currentTreeA;
	private OSRFilterTree currentTreeB;

	
	public PairComparison getCurrentComparison() {
		return currentComparison;
	}

	
	
	public void setCurrentComparison(PairComparison currentComparison) {
		this.currentComparison = currentComparison;
	}



	public OSRFilterTree getCurrentTreeA() {
		return currentTreeA;
	}



	public void setCurrentTreeA(OSRFilterTree currentTreeA) {
		this.currentTreeA = currentTreeA;
	}



	public OSRFilterTree getCurrentTreeB() {
		return currentTreeB;
	}



	public void setCurrentTreeB(OSRFilterTree currentTreeB) {
		this.currentTreeB = currentTreeB;
	}
}
