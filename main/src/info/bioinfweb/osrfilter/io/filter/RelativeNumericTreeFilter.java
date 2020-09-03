package info.bioinfweb.osrfilter.io.filter;


import java.util.List;
import java.util.Map;

import info.bioinfweb.osrfilter.analysis.TreeSorter;
import info.bioinfweb.osrfilter.data.TreeData;
import info.bioinfweb.osrfilter.data.TreeIdentifier;
import info.bioinfweb.osrfilter.data.parameters.filter.NumericTreeFilterDefinition;
import info.bioinfweb.osrfilter.data.parameters.filter.NumericTreeFilterDefinition.Relative;
import info.bioinfweb.osrfilter.data.parameters.filter.TreeFilterThreshold;



public class RelativeNumericTreeFilter extends NumericTreeFilter<NumericTreeFilterDefinition.Relative> {
	public RelativeNumericTreeFilter(Relative definition, Map<TreeIdentifier, TreeData> treeDataMap) {
		super(definition, treeDataMap);
	}

	
	@Override
	protected void fillSet(TreeFilterThreshold threshold, TreeFilterSet set) {
		List<TreeIdentifier> list = TreeSorter.sort(getTreeDataMap(), getDefinition().getTreeUserValueName(), getDefinition().isBelowThreshold());
		set.getTrees().addAll(list.subList(0, (int)Math.round(threshold.getThreshold() * list.size())));
	}
}
