package info.bioinfweb.osrfilter.data.parameters.filter;


import java.util.HashSet;

import info.bioinfweb.osrfilter.exception.DuplicateEntryException;



public class TreeFilterSet extends HashSet<TreeFilter> {
	private static final long serialVersionUID = 1L;

	
	@Override
	public boolean add(TreeFilter filter) {
		if (!contains(filter)) {
			return super.add(filter);
		}
		else {
			throw new DuplicateEntryException("The parameter file contains more than one tree filter with the name \"" + filter.getName() + 
					"\". Each tree filter must have a unique name.");
		}
	}
}
