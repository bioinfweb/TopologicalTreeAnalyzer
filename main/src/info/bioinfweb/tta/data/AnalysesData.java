package info.bioinfweb.tta.data;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class AnalysesData {
	private List<TreeIdentifier> inputOrder = new ArrayList<TreeIdentifier>();
	private Map<TreePair, PairComparisonData> comparisonMap = new HashMap<TreePair, PairComparisonData>();
	private Map<TreeIdentifier, TreeData> treeMap = new HashMap<TreeIdentifier, TreeData>();
	
	
	public int getTreeCount() {
		return inputOrder.size();
	}


	public List<TreeIdentifier> getInputOrder() {
		return inputOrder;
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
