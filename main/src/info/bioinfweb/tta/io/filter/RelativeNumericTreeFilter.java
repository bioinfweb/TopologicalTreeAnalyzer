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

import info.bioinfweb.tta.data.TreeIdentifier;
import info.bioinfweb.tta.data.UserValues;
import info.bioinfweb.tta.data.database.DatabaseConstants;
import info.bioinfweb.tta.data.database.DatabaseIterator;
import info.bioinfweb.tta.data.database.TreeUserDataTable;
import info.bioinfweb.tta.data.parameters.filter.NumericTreeFilterDefinition;
import info.bioinfweb.tta.data.parameters.filter.NumericTreeFilterDefinition.Relative;
import info.bioinfweb.tta.data.parameters.filter.TreeFilterThreshold;



public class RelativeNumericTreeFilter extends NumericTreeFilter<NumericTreeFilterDefinition.Relative> {
	public RelativeNumericTreeFilter(Relative definition, TreeUserDataTable treeUserData) {
		super(definition, treeUserData);
	}

	
	@Override
	protected void fillSet(TreeFilterThreshold threshold, TreeFilterSet set) throws SQLException {
		String order;
		if (getDefinition().isBelowThreshold()) {
			order = " ASC";
		}
		else {
			order = " DESC";
		}
		
		DatabaseIterator<TreeIdentifier, UserValues<TreeIdentifier>> iterator = 
				getTreeUserData().valueIterator(0, (int)Math.round(threshold.getThreshold() * getTreeUserData().getTreeOrder().size()), 
						null, DatabaseConstants.COLUMN_PREFIX_USER_DATA + getDefinition().getTreeUserValueName() + order);

		while (iterator.hasNext()) {
			UserValues<TreeIdentifier> rowData = iterator.next();
			set.getTrees().add(rowData.getKey());
		}
	}
}
