package info.bioinfweb.osrfilter.io.filter;


import java.util.Iterator;
import java.util.List;
import java.util.Map;

import info.bioinfweb.osrfilter.analysis.TreeSorter;
import info.bioinfweb.osrfilter.data.TreeData;
import info.bioinfweb.osrfilter.data.TreeIdentifier;
import info.bioinfweb.osrfilter.data.parameters.filter.NumericTreeFilterDefinition;
import info.bioinfweb.osrfilter.data.parameters.filter.TreeFilterThreshold;



public class RelativeNumericTreeFilter<D extends NumericTreeFilterDefinition.Relative> extends TreeFilter<D> {
	private Iterator<TreeFilterThreshold> thresholdIterator;
	
	
	public RelativeNumericTreeFilter(D definition, Map<TreeIdentifier, TreeData> treeDataMap) {
		super(definition, treeDataMap);
		thresholdIterator = getDefinition().getThresholds().iterator();
	}

	
	@Override
	public boolean hasNext() {
		return thresholdIterator.hasNext();
	}

	
	private TreeFilterSet createSet(TreeFilterThreshold threshold) {
		TreeFilterSet result = new TreeFilterSet(getDefinition().getName() + "_" + threshold.getThreshold());  //TODO Format in a different way?
		List<TreeIdentifier> list = TreeSorter.sort(getTreeDataMap(), getDefinition().getTreeUserValueName(), true);  //TODO Parameterize direction.
		result.getTrees().addAll(list.subList(0, (int)Math.round(threshold.getThreshold() * list.size())));
		return result;
	}


	@Override
	public TreeFilterSet next() {
		return createSet(thresholdIterator.next());
	}
}
