/*
 * Topological Tree Analyzer - User defined topological analyses of large sets of phylogenetic trees. 
 * Copyright (C) 2020  Ben C. Stöver
 * <http://bioinfweb.info/TTA>
 * 
 * This file is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
