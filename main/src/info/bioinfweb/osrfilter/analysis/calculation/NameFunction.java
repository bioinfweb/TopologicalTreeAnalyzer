package info.bioinfweb.osrfilter.analysis.calculation;


import info.bioinfweb.osrfilter.analysis.UserExpressionDataProvider;



public class NameFunction extends TreeDataFunction {
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
