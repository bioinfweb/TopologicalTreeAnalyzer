package info.bioinfweb.osrfilter.analysis;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import info.bioinfweb.osrfilter.data.TreeData;
import info.bioinfweb.osrfilter.data.TreeIdentifier;
import info.bioinfweb.osrfilter.exception.InvalidParameterTypeException;



public class TreeSorter {
	public List<TreeIdentifier> sort(final Map<TreeIdentifier, TreeData> treeDataMap, final String userValueName, final boolean upwards) 
			throws InvalidParameterTypeException {
		
		List<TreeIdentifier> result = new ArrayList<TreeIdentifier>(treeDataMap.size());
		result.addAll(treeDataMap.keySet());
		
		result.sort(new Comparator<TreeIdentifier>() {
			private double getUserValue(TreeIdentifier identifier) {
				Object value = treeDataMap.get(identifier).getUserValues().get(userValueName);
				if (value instanceof Number) {
					return ((Number)value).doubleValue();
				}
				else {
					throw new InvalidParameterTypeException("The user expression \"" + userValueName + 
							"\" used to filter the tree output did not produce a numeric result, although used with an numeric tree filter.");
				}
			}
			
			@Override
			public int compare(TreeIdentifier identifierA, TreeIdentifier identifierB) {
				int result = (int)(getUserValue(identifierA) - getUserValue(identifierB));
				if (upwards) {
					return result;
				}
				else {
					return -result;
				}
			}
		});
		
		return result;
	}
}
