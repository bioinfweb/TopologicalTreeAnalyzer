package info.bioinfweb.tta.analysis.calculation;


import info.bioinfweb.tta.analysis.UserExpressionDataProvider;



public class MFunction extends TreeIndependentPairDataFunction<Double> {
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
