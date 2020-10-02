package info.bioinfweb.tta.analysis.calculation;


import info.bioinfweb.tta.analysis.UserExpressionDataProvider;



public class SplitsFunction extends TreeDataFunction<Double> {
	public SplitsFunction(UserExpressionDataProvider expressionData) {
		super(expressionData);
	}


	@Override
	public String getName() {
		return "splits";
	}


	@Override
	protected Double getValue(int index) {
		return new Double(getExpressionData().getCurrentTreeData(index).getSplits());
	}
}
