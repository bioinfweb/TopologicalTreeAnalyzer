package info.bioinfweb.osrfilter.io.filter;


import java.util.Map;
import java.util.NoSuchElementException;

import info.bioinfweb.osrfilter.data.TreeData;
import info.bioinfweb.osrfilter.data.TreeIdentifier;
import info.bioinfweb.osrfilter.data.parameters.filter.BooleanTreeFilterDefinition;



public class BooleanTreeFilter extends TreeFilter<BooleanTreeFilterDefinition> {
	private boolean noSetReturned;
	
	
	public BooleanTreeFilter(BooleanTreeFilterDefinition definition, Map<TreeIdentifier, TreeData> treeDataMap) {
		super(definition, treeDataMap);
	}


	@Override
	public boolean hasNext() {
		return noSetReturned;
	}
	
	
	private TreeFilterSet createSet() {
		TreeFilterSet result = new TreeFilterSet(getDefinition().getName());
		
		for (TreeIdentifier identifier : getTreeDataMap().keySet()) {
			if (getUserValue(getTreeDataMap().get(identifier), Boolean.class)) {
				result.getTrees().add(identifier);
			}
		}
		return result;
	}


	@Override
	public TreeFilterSet next() {
		if (noSetReturned) {
			noSetReturned = false;
			return createSet();
		}
		else {
			throw new NoSuchElementException();
		}
	}
}
