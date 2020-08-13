package info.bioinfweb.osrfilter.analysis;


import java.util.HashSet;
import java.util.Set;

import info.bioinfweb.osrfilter.data.OSRFilterTree;
import info.bioinfweb.osrfilter.data.PairComparison;



public class UserExpressionData {
	private PairComparison currentComparison = null;  //TODO Possibly reference whole map here for phase 2.
	private OSRFilterTree currentTreeA = null;  //TODO Only reference ID and label since whole tree will not be available anymore.
	private OSRFilterTree currentTreeB = null;
	private Set<CharSequence> references = new HashSet<CharSequence>();

	
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



	public Set<CharSequence> getReferences() {
		return references;
	}
}
