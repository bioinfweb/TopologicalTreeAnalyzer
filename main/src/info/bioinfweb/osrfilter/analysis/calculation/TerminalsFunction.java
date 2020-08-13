package info.bioinfweb.osrfilter.analysis.calculation;


import info.bioinfweb.osrfilter.analysis.UserExpressionDataProvider;



public class TerminalsFunction extends TreeDependentFunction<Double> {
	public TerminalsFunction(UserExpressionDataProvider expressionData) {
		super(expressionData);
	}


	@Override
	public String getName() {
		return "terminals";
	}


	@Override
	protected Double getValueAB() {
		return new Double(getExpressionData().getCurrentComparison().getTerminalsA());
	}


	@Override
	protected Double getValueBA() {
		return new Double(getExpressionData().getCurrentComparison().getTerminalsB());
	}
}
