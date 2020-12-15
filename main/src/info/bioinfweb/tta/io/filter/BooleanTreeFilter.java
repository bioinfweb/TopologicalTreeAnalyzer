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


import java.sql.SQLException;
import java.util.NoSuchElementException;

import info.bioinfweb.tta.data.TreeIdentifier;
import info.bioinfweb.tta.data.UserValues;
import info.bioinfweb.tta.data.database.DatabaseIterator;
import info.bioinfweb.tta.data.database.TreeUserDataTable;
import info.bioinfweb.tta.data.parameters.filter.BooleanTreeFilterDefinition;



public class BooleanTreeFilter extends TreeFilter<BooleanTreeFilterDefinition> {
	private boolean noSetReturned;
	
	
	public BooleanTreeFilter(BooleanTreeFilterDefinition definition, TreeUserDataTable treeUserData) {
		super(definition, treeUserData);
		noSetReturned = true;
	}


	@Override
	public boolean hasNext() {
		return noSetReturned;
	}
	
	
	private TreeFilterSet createSet() throws SQLException {
		String format = getDefinition().getDefaultFormat();
		TreeFilterSet result = new TreeFilterSet(getDefinition().getName() + getFileExtension(format), format);
		
		DatabaseIterator<TreeIdentifier, UserValues<TreeIdentifier>> iterator = getTreeUserData().valueIterator();
		try {
			while (iterator.hasNext()) {
				UserValues<TreeIdentifier> rowData = iterator.next();
				
				if (getUserValue(rowData.getUserValues(), Double.class) != 0.0) {
					result.getTrees().add(rowData.getKey());
				}
			}
		}
		finally {
			iterator.close();
		}
		return result;
	}


	@Override
	public TreeFilterSet next() throws SQLException {
		if (noSetReturned) {
			noSetReturned = false;
			return createSet();
		}
		else {
			throw new NoSuchElementException();
		}
	}
}
