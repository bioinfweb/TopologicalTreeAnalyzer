package info.bioinfweb.osrfilter.analysis.calculation;


import info.bioinfweb.osrfilter.analysis.UserExpressionData;



public class NFunction extends TreeDependentFunction<Double> {
	public NFunction(UserExpressionData expressionData) {
		super(expressionData);
	}

	
	@Override
	public String getName() {
		return "n";
	}


	@Override
	protected Double getValueAB() {
		return new Double(getExpressionData().getCurrentComparison().getNotMatchingSplitsAB());
	}
	

	@Override
	protected Double getValueBA() {
		return new Double(getExpressionData().getCurrentComparison().getNotMatchingSplitsBA());
	}
}
