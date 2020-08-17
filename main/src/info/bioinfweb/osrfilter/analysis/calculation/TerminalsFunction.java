package info.bioinfweb.osrfilter.analysis.calculation;


import info.bioinfweb.osrfilter.analysis.UserExpressionDataProvider;



public class TerminalsFunction extends TreeDataFunction<Double> {
	public TerminalsFunction(UserExpressionDataProvider expressionData) {
		super(expressionData);
	}


	@Override
	public String getName() {
		return "terminals";
	}


	@Override
	protected Double getValue(int index) {
		return new Double(getExpressionData().getCurrentTreeData(index).getTerminals());
	}
}
