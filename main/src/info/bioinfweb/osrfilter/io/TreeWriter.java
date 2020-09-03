package info.bioinfweb.osrfilter.io;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import info.bioinfweb.jphyloio.dataadapters.DocumentDataAdapter;
import info.bioinfweb.jphyloio.dataadapters.implementations.ListBasedDocumentDataAdapter;
import info.bioinfweb.jphyloio.dataadapters.implementations.store.StoreTreeNetworkDataAdapter;
import info.bioinfweb.osrfilter.data.TreeData;
import info.bioinfweb.osrfilter.data.TreeIdentifier;
import info.bioinfweb.osrfilter.data.parameters.filter.BooleanTreeFilter;
import info.bioinfweb.osrfilter.data.parameters.filter.NumericTreeFilter;
import info.bioinfweb.osrfilter.data.parameters.filter.TreeFilter;
import info.bioinfweb.osrfilter.data.parameters.filter.TreeFilterThreshold;
import info.bioinfweb.osrfilter.exception.InvalidParameterTypeException;
import info.bioinfweb.osrfilter.io.filter.treeiterator.FilterTreeIterator;



public class TreeWriter {
	private InvalidParameterTypeException createInvalidParameterTypeException(TreeFilter filter, String type) {
		return new InvalidParameterTypeException("The user expression \"" + filter.getTreeUserValueName() + 
				"\" used to filter the tree output did not produce a " + type + " result, although the filter \"" + filter.getName() + 
				"\" is a " + type + " filter.");
	}
	
	
	private boolean checkTree(TreeFilter filter, double threshold, Object value) {
		if (filter instanceof NumericTreeFilter) {
			if (value instanceof Number) {
				double numericValue = ((Number)value).doubleValue();
				if (filter instanceof NumericTreeFilter.Absolute) {
					if (((NumericTreeFilter)filter).isBelowThreshold()) {
						return numericValue <= threshold;
					}
					else {
						return numericValue >= threshold;
					}
				}
				else {  // NumericTreeFilter.Relative
					throw new InternalError("Not implemented.");
				}
			}
			else {
				throw createInvalidParameterTypeException(filter, "numeric");
			}
		}
		else if (filter instanceof BooleanTreeFilter) {
			if (value instanceof Boolean) {
				return (Boolean)value;
			}
			else {
				throw createInvalidParameterTypeException(filter, "boolean");
			}
		}
		else {
			throw new InternalError("The filter type " + filter.getClass().getCanonicalName() + " is not supported by this implementation.");
		}
	}
	
	
	public void writeFilterOutputs(TreeFilter filter, List<String> treeFilesNames, Map<TreeIdentifier, TreeData> treeDataMap) throws IOException, Exception {
		//TODO Load trees one by one and write the filtered tree to an output file. Store single trees in a StoreTreeNetworkAdapter.
		
		//TODO Possibly sort trees here. (Using multiple temporary files.)
		
//		for (TreeFilterThreshold threshold : filter.getThresholds()) {
//			FilterTreeIterator iterator = new FilterTreeIterator(treeFilesNames);
//			while (iterator.hasNext()) {
//				StoreTreeNetworkDataAdapter tree = iterator.next();
//				if (checkTree(filter, threshold.getThreshold(), treeDataMap.get(tree.getTreeIdentifier()).getUserValues().get(filter.getTreeUserValueName()))) {
//					//TODO Write tree to output.
//				}
//			}
//		}
	}
}
