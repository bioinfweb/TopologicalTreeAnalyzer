package info.bioinfweb.osrfilter.analysis.calculation;


import info.bioinfweb.osrfilter.analysis.UserExpressionDataProvider;



public class CFunction extends TreeDependentPairDataFunction<Double> {
	public CFunction(UserExpressionDataProvider expressionData) {
		super(expressionData);
	}


	@Override
	public String getName() {
		return "c";
	}


	@Override
	protected Double getValueAB() {
		return new Double(getExpressionData().getCurrentComparisonData().getConflictingSplitsAB());
	}


	@Override
	protected Double getValueBA() {
		return new Double(getExpressionData().getCurrentComparisonData().getConflictingSplitsBA());
	}
}
