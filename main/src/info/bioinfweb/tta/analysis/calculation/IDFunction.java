package info.bioinfweb.tta.analysis.calculation;


import info.bioinfweb.tta.analysis.UserExpressionDataProvider;



public class IDFunction extends TreeDataFunction<String> {
	public IDFunction(UserExpressionDataProvider expressionData) {
		super(expressionData);
	}

	
	@Override
	public String getName() {
		return "id";
	}


	@Override
	protected String getValue(int index) {
		return getExpressionData().getTreeIdentifier(index).getID();
	}
}
