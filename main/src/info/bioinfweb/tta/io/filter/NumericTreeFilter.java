package info.bioinfweb.tta.io.filter;


import java.util.Iterator;
import java.util.Map;

import info.bioinfweb.tta.data.TreeData;
import info.bioinfweb.tta.data.TreeIdentifier;
import info.bioinfweb.tta.data.parameters.filter.NumericTreeFilterDefinition;
import info.bioinfweb.tta.data.parameters.filter.TreeFilterThreshold;



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
	
	
	protected String determineFormat(TreeFilterThreshold threshold) {
		String result = threshold.getFormat();
		if (result == null) {
			result = getDefinition().getDefaultFormat();
		}
		return result;
	}


	@Override
	public TreeFilterSet next() {
		TreeFilterThreshold threshold = thresholdIterator.next();
		String format = determineFormat(threshold);
		TreeFilterSet result = new TreeFilterSet(getDefinition().getName() + "_" + 
				Double.toString(threshold.getThreshold()).replaceAll("\\-", "m") + getFileExtension(format), format);  // Avoid "-" since it is invalid in file names.
		fillSet(threshold, result);
		return result;
	}
}
