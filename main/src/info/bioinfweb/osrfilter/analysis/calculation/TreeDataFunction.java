package info.bioinfweb.osrfilter.analysis.calculation;


import info.bioinfweb.osrfilter.analysis.UserExpressionData;
import info.bioinfweb.osrfilter.data.TreeIdentifier;



public abstract class TreeDataFunction extends TreeDependentFunction<String> {
	public TreeDataFunction(UserExpressionData expressionData) {
		super(expressionData);
	}

	
	protected abstract String getTreeData(TreeIdentifier identifier);
	
	
	@Override
	protected String getValueAB() {
		return getTreeData(getExpressionData().getCurrentTreeA().getTreeIdentifier());
	}

	
	@Override
	protected String getValueBA() {
		return getTreeData(getExpressionData().getCurrentTreeB().getTreeIdentifier());
	}

}