package info.bioinfweb.osrfilter.io.filter;


import java.util.Iterator;
import java.util.Map;

import info.bioinfweb.osrfilter.data.TreeData;
import info.bioinfweb.osrfilter.data.TreeIdentifier;
import info.bioinfweb.osrfilter.data.parameters.filter.NumericTreeFilterDefinition;
import info.bioinfweb.osrfilter.data.parameters.filter.TreeFilterThreshold;



public abstract class NumericTreeFilter<D extends NumericTreeFilterDefinition> extends TreeFilter<D> {
	private Iterator<TreeFilterThreshold> thresholdIterator;
	
	
	public NumericTreeFilter(D definition, Map<TreeIdentifier, TreeData> treeDataMap) {
		super(definition, treeDataMap);
		thresholdIterator = getDefinition().getThresholds().iterator();
	}

	
	@Override
	public boolean hasNext() {
		return thresholdIterator.hasNext();
	}

	
	protected abstract void fillSet(TreeFilterThreshold threshold, TreeFilterSet set);


	@Override
	public TreeFilterSet next() {
		TreeFilterThreshold threshold = thresholdIterator.next();
		TreeFilterSet result = new TreeFilterSet(getDefinition().getName() + "_" + threshold.getThreshold());  //TODO Format in a different way?
		fillSet(threshold, result);
		return result;
	}
}
