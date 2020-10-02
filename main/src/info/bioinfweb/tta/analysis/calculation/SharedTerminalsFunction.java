package info.bioinfweb.tta.analysis.calculation;


import info.bioinfweb.tta.analysis.UserExpressionDataProvider;



public class SharedTerminalsFunction extends TreeIndependentPairDataFunction<Double> {
	public SharedTerminalsFunction(UserExpressionDataProvider expressionData) {
		super(expressionData);
	}

	
	@Override
	public String getName() {
		return "sharedTerminals";
	}

	
	@Override
	protected Double getValue() {
		return new Double(getExpressionData().getCurrentComparisonData().getSharedTerminals());
	}
}
