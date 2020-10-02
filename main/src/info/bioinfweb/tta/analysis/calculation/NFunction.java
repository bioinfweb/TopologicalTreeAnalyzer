package info.bioinfweb.tta.analysis.calculation;


import info.bioinfweb.tta.analysis.UserExpressionDataProvider;



public class NFunction extends TreeDependentPairDataFunction<Double> {
	public NFunction(UserExpressionDataProvider expressionData) {
		super(expressionData);
	}

	
	@Override
	public String getName() {
		return "n";
	}


	@Override
	protected Double getValueAB() {
		return new Double(getExpressionData().getCurrentComparisonData().getNotMatchingSplitsAB());
	}
	

	@Override
	protected Double getValueBA() {
		return new Double(getExpressionData().getCurrentComparisonData().getNotMatchingSplitsBA());
	}
}
