package info.bioinfweb.tta.io.filter;


import java.util.List;
import java.util.Map;

import info.bioinfweb.tta.analysis.TreeSorter;
import info.bioinfweb.tta.data.TreeData;
import info.bioinfweb.tta.data.TreeIdentifier;
import info.bioinfweb.tta.data.parameters.filter.NumericTreeFilterDefinition;
import info.bioinfweb.tta.data.parameters.filter.TreeFilterThreshold;
import info.bioinfweb.tta.data.parameters.filter.NumericTreeFilterDefinition.Relative;



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
