package info.bioinfweb.osrfilter.analysis.calculation;


import info.bioinfweb.osrfilter.analysis.UserExpressionData;



public class SplitsFunction extends TreeDependentFunction<Double> {
	public SplitsFunction(UserExpressionData expressionData) {
		super(expressionData);
	}


	@Override
	public String getName() {
		return "splits";
	}


	@Override
	protected Double getValueAB() {
		return new Double(getExpressionData().getCurrentComparison().getSplitsA());
	}


	@Override
	protected Double getValueBA() {
		return new Double(getExpressionData().getCurrentComparison().getSplitsB());
	}
}
