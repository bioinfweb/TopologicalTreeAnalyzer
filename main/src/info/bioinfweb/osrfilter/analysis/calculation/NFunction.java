package info.bioinfweb.osrfilter.analysis.calculation;


import info.bioinfweb.osrfilter.analysis.UserExpressionData;



public class NFunction extends TreeDependentDoubleFunction{
	public NFunction(UserExpressionData expressionData) {
		super(expressionData);
	}

	
	@Override
	public String getName() {
		return "n";
	}


	@Override
	protected double getValueAB() {
		return getExpressionData().getCurrentComparison().getNotMatchingSplitsAB();
	}
	

	@Override
	protected double getValueBA() {
		return getExpressionData().getCurrentComparison().getNotMatchingSplitsBA();
	}
}
