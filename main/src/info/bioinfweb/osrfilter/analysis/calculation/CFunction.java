package info.bioinfweb.osrfilter.analysis.calculation;


import info.bioinfweb.osrfilter.analysis.UserExpressionDataProvider;



public class CFunction extends TreeDependentFunction<Double> {
	public CFunction(UserExpressionDataProvider expressionData) {
		super(expressionData);
	}


	@Override
	public String getName() {
		return "c";
	}


	@Override
	protected Double getValueAB() {
		return new Double(getExpressionData().getCurrentComparison().getConflictingSplitsAB());
	}


	@Override
	protected Double getValueBA() {
		return new Double(getExpressionData().getCurrentComparison().getConflictingSplitsBA());
	}
}
