package info.bioinfweb.osrfilter.analysis.calculation;


import info.bioinfweb.osrfilter.analysis.UserExpressionData;



public class CFunction extends TreeDependentDoubleFunction {
	public CFunction(UserExpressionData expressionData) {
		super(expressionData);
	}


	@Override
	public String getName() {
		return "c";
	}


	@Override
	protected double getValueAB() {
		return getExpressionData().getCurrentComparison().getConflictingSplitsAB();
	}


	@Override
	protected double getValueBA() {
		return getExpressionData().getCurrentComparison().getConflictingSplitsBA();
	}
}
