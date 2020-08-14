package info.bioinfweb.osrfilter.analysis.calculation;


import info.bioinfweb.osrfilter.analysis.UserExpressionDataProvider;



public class MFunction extends TreeIndependentFunction<Double> {
	public MFunction(UserExpressionDataProvider expressionData) {
		super(expressionData);
	}

	
	@Override
	public String getName() {
		return "m";
	}

	
	@Override
	protected Double getValue() {
		return new Double(getExpressionData().getCurrentComparisonData().getMatchingSplits());
	}
}
