package info.bioinfweb.osrfilter.io;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import info.bioinfweb.osrfilter.data.OSRFilterTree;
import info.bioinfweb.osrfilter.data.TreeData;
import info.bioinfweb.osrfilter.data.TreeIdentifier;
import info.bioinfweb.osrfilter.data.parameters.TreeFilter;
import info.bioinfweb.osrfilter.data.parameters.TreeFilterThreshold;
import info.bioinfweb.osrfilter.exception.InvalidParameterTypeException;



public class TreeWriter {
	private boolean checkTree(TreeFilter filter, double threshold, double value) {
		if (filter.isRelativeThreshold()) {
			throw new InternalError("Not implemented.");  //TODO Distribution of all trees needs to be looked at here. (And trees probably need to be sorted.)
		}
		else {
			if (filter.isBelowThreshold()) {
				return value <= threshold;
			}
			else {
				return value >= threshold;
			}
		}
	}
	
	
	public void writeFilterOutputs(TreeFilter filter, List<String> treeFilesNames, Map<TreeIdentifier, TreeData> treeDataMap) throws IOException, Exception {
		//TODO Possibly sort trees here. (Using multiple temporary files.)
		
		for (TreeFilterThreshold threshold : filter.getThresholds()) {
			TreeIterator iterator = new TreeIterator(treeFilesNames);
			while (iterator.hasNext()) {
				OSRFilterTree tree = iterator.next();
				Object userValue = treeDataMap.get(tree.getTreeIdentifier()).getUserValues().get(filter.getTreeUserValueName());
				if (userValue instanceof Number) {
					if (checkTree(filter, threshold.getThreshold(), ((Number)userValue).doubleValue())) {
						//TODO Write tree to output.
					}
				}
				else {
					throw new InvalidParameterTypeException("The user expression \"" + filter.getTreeUserValueName() + 
							"\" used to filter the tree output did not produce a numeric result.");
				}
			}
		}
	}
}
