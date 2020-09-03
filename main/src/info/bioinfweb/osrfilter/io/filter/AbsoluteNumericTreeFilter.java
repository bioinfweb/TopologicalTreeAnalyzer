package info.bioinfweb.osrfilter.io.filter;


import java.util.Map;

import info.bioinfweb.osrfilter.data.TreeData;
import info.bioinfweb.osrfilter.data.TreeIdentifier;
import info.bioinfweb.osrfilter.data.parameters.filter.NumericTreeFilterDefinition;
import info.bioinfweb.osrfilter.data.parameters.filter.NumericTreeFilterDefinition.Absolute;
import info.bioinfweb.osrfilter.data.parameters.filter.TreeFilterThreshold;



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
