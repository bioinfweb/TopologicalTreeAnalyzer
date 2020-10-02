package info.bioinfweb.tta.data.parameters.filter;


import java.util.HashSet;

import info.bioinfweb.tta.exception.DuplicateEntryException;



public class TreeFilterDefinitionSet extends HashSet<TreeFilterDefinition> {
	private static final long serialVersionUID = 1L;

	
	@Override
	public boolean add(TreeFilterDefinition filter) {
		if (!contains(filter)) {
			return super.add(filter);
		}
		else {
			throw new DuplicateEntryException("The parameter file contains more than one tree filter with the name \"" + filter.getName() + 
					"\". Each tree filter must have a unique name.");
		}
	}
}
