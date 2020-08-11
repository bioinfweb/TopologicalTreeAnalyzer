package info.bioinfweb.osrfilter.analysis.calculation;


import info.bioinfweb.osrfilter.analysis.UserExpressionData;
import info.bioinfweb.osrfilter.data.TreeIdentifier;



public class NameFunction extends TreeDataFunction {
	public NameFunction(UserExpressionData expressionData) {
		super(expressionData);
	}

	
	@Override
	public String getName() {
		return "name";
	}


	@Override
	protected String getTreeData(TreeIdentifier identifier) {
		return identifier.getName();
	}
}
