package info.bioinfweb.osrfilter.analysis.calculation;


import info.bioinfweb.osrfilter.analysis.UserExpressionData;



public class SharedTerminalsFunction extends TreeIndependentFunction<Double> {
	public SharedTerminalsFunction(UserExpressionData expressionData) {
		super(expressionData);
	}

	
	@Override
	public String getName() {
		return "sharedTerminals";
	}

	
	@Override
	protected Double getValue() {
		return new Double(getExpressionData().getCurrentComparison().getSharedTerminals());
	}
}
