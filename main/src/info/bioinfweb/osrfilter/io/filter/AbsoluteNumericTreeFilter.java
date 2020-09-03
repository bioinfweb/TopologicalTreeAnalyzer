package info.bioinfweb.osrfilter.io.filter;


import java.util.Iterator;
import java.util.Map;

import info.bioinfweb.osrfilter.data.TreeData;
import info.bioinfweb.osrfilter.data.TreeIdentifier;
import info.bioinfweb.osrfilter.data.parameters.filter.NumericTreeFilterDefinition;
import info.bioinfweb.osrfilter.data.parameters.filter.TreeFilterThreshold;



public class AbsoluteNumericTreeFilter<D extends NumericTreeFilterDefinition.Absolute> extends TreeFilter<D> {
	private Iterator<TreeFilterThreshold> thresholdIterator;
	
	
	public AbsoluteNumericTreeFilter(D definition, Map<TreeIdentifier, TreeData> treeDataMap) {
		super(definition, treeDataMap);
		thresholdIterator = getDefinition().getThresholds().iterator();
	}

	
	@Override
	public boolean hasNext() {
		return thresholdIterator.hasNext();
	}

	
	private TreeFilterSet createSet(TreeFilterThreshold threshold) {
		TreeFilterSet result = new TreeFilterSet(getDefinition().getName() + "_" + threshold.getThreshold());  //TODO Format in a different way?
		
		for (TreeIdentifier identifier : getTreeDataMap().keySet()) {
			double value = getUserValue(getTreeDataMap().get(identifier), Number.class).doubleValue();
			if ((getDefinition().isBelowThreshold() && (value <= threshold.getThreshold())) ||
					(!getDefinition().isBelowThreshold() && (value >= threshold.getThreshold()))) {
				
				result.getTrees().add(identifier);
			}
		}
		return result;
	}


	@Override
	public TreeFilterSet next() {
		return createSet(thresholdIterator.next());
	}
}
