package info.bioinfweb.tta.io.filter;


import java.util.Map;

import info.bioinfweb.tta.data.TreeData;
import info.bioinfweb.tta.data.TreeIdentifier;
import info.bioinfweb.tta.data.parameters.filter.NumericTreeFilterDefinition;
import info.bioinfweb.tta.data.parameters.filter.TreeFilterThreshold;
import info.bioinfweb.tta.data.parameters.filter.NumericTreeFilterDefinition.Absolute;



public class AbsoluteNumericTreeFilter extends NumericTreeFilter<NumericTreeFilterDefinition.Absolute> {
	public AbsoluteNumericTreeFilter(Absolute definition, Map<TreeIdentifier, TreeData> treeDataMap) {
		super(definition, treeDataMap);
	}
	

	@Override
	protected void fillSet(TreeFilterThreshold threshold, TreeFilterSet set) {
		for (TreeIdentifier identifier : getTreeDataMap().keySet()) {
			double value = getUserValue(getTreeDataMap().get(identifier), Number.class).doubleValue();
			if ((getDefinition().isBelowThreshold() && (value <= threshold.getThreshold())) ||
					(!getDefinition().isBelowThreshold() && (value >= threshold.getThreshold()))) {
				
				set.getTrees().add(identifier);
			}
		}
	}
}
