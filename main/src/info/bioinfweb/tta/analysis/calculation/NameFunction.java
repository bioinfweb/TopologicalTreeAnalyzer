package info.bioinfweb.tta.analysis.calculation;


import info.bioinfweb.tta.analysis.UserExpressionDataProvider;



public class NameFunction extends TreeDataFunction<String> {
	public NameFunction(UserExpressionDataProvider expressionData) {
		super(expressionData);
	}

	
	@Override
	public String getName() {
		return "name";
	}


	@Override
	protected String getValue(int index) {
		return getExpressionData().getTreeIdentifier(index).getName();
	}
}
