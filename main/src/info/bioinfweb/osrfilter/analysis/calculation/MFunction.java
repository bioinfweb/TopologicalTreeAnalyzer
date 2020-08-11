package info.bioinfweb.osrfilter.analysis.calculation;


import info.bioinfweb.osrfilter.analysis.UserExpressionData;



public class MFunction extends TreeIndependentFunction<Double> {
	public MFunction(UserExpressionData expressionData) {
		super(expressionData);
	}

	
	@Override
	public String getName() {
		return "m";
	}

	
	@Override
	protected Double getValue() {
		return new Double(getExpressionData().getCurrentComparison().getMatchingSplits());
	}
}