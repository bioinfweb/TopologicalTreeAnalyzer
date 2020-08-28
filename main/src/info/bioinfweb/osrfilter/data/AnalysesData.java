package info.bioinfweb.osrfilter.data;


import java.util.HashMap;
import java.util.Map;



public class AnalysesData {
	private Map<TreePair, PairComparisonData> comparisonMap = new HashMap<TreePair, PairComparisonData>();
	private Map<TreeIdentifier, TreeData> treeMap = new HashMap<TreeIdentifier, TreeData>();
	
	
	public int getTreeCount() {
		return treeMap.size();
	}


	public Map<TreePair, PairComparisonData> getComparisonMap() {
		return comparisonMap;
	}
	
	
	public Map<TreeIdentifier, TreeData> getTreeMap() {
		return treeMap;
	}
	
	
	public void clear() {
		comparisonMap.clear();
		treeMap.clear();
	}
}
