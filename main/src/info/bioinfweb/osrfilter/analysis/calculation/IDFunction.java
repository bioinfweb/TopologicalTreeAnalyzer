package info.bioinfweb.osrfilter.analysis.calculation;


import info.bioinfweb.osrfilter.analysis.UserExpressionDataProvider;



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
