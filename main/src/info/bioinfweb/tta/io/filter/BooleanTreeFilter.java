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
package info.bioinfweb.tta.io.filter;


import java.util.Map;
import java.util.NoSuchElementException;

import info.bioinfweb.tta.data.TreeData;
import info.bioinfweb.tta.data.TreeIdentifier;
import info.bioinfweb.tta.data.parameters.filter.BooleanTreeFilterDefinition;



public class BooleanTreeFilter extends TreeFilter<BooleanTreeFilterDefinition> {
	private boolean noSetReturned;
	
	
	public BooleanTreeFilter(BooleanTreeFilterDefinition definition, Map<TreeIdentifier, TreeData> treeDataMap) {
		super(definition, treeDataMap);
		noSetReturned = true;
	}


	@Override
	public boolean hasNext() {
		return noSetReturned;
	}
	
	
	private TreeFilterSet createSet() {
		String format = getDefinition().getDefaultFormat();
		TreeFilterSet result = new TreeFilterSet(getDefinition().getName() + getFileExtension(format), format);
		
		for (TreeIdentifier identifier : getTreeDataMap().keySet()) {
			if (getUserValue(getTreeDataMap().get(identifier), Double.class) != 0.0) {
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
