package info.bioinfweb.osrfilter.analysis.calculation;


import org.nfunk.jep.function.PostfixMathCommandI;

import info.bioinfweb.osrfilter.data.PairComparison;



public abstract class AbstractFunction implements PostfixMathCommandI {
	private int curNumberOfParameters = 1;
	private PairComparison currentComparison;  //TODO Possibly reference whole map here for phase 2.

  
	public abstract String getName();
	
	
	@Override
	public boolean checkNumberOfParameters(int parameterCount) {
		return (getNumberOfParameters() == -1) || (parameterCount == getNumberOfParameters());
	}

	
	@Override
	public void setCurNumberOfParameters(int n) {
		curNumberOfParameters = n;
	}


	protected int getCurNumberOfParameters() {
		return curNumberOfParameters;
	}


	public PairComparison getCurrentComparison() {
		return currentComparison;
	}


	public void setCurrentComparison(PairComparison currentComparison) {
		this.currentComparison = currentComparison;
	}
}
