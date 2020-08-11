package info.bioinfweb.osrfilter.analysis.calculation;


import info.bioinfweb.osrfilter.analysis.UserExpressionData;
import info.bioinfweb.osrfilter.data.TreeIdentifier;



public class IDFunction extends TreeDataFunction {
	public IDFunction(UserExpressionData expressionData) {
		super(expressionData);
	}

	
	@Override
	public String getName() {
		return "id";
	}


	@Override
	protected String getTreeData(TreeIdentifier identifier) {
		return identifier.getID();
	}
}
