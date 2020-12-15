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
import info.bioinfweb.tta.data.database.DatabaseIterator;
import info.bioinfweb.tta.data.database.TreeUserDataTable;
import info.bioinfweb.tta.data.parameters.filter.NumericTreeFilterDefinition;
import info.bioinfweb.tta.data.parameters.filter.NumericTreeFilterDefinition.Absolute;
import info.bioinfweb.tta.data.parameters.filter.TreeFilterThreshold;



public class AbsoluteNumericTreeFilter extends NumericTreeFilter<NumericTreeFilterDefinition.Absolute> {
	public AbsoluteNumericTreeFilter(Absolute definition, TreeUserDataTable treeUserData) {
		super(definition, treeUserData);
	}
	

	@Override
	protected void fillSet(TreeFilterThreshold threshold, TreeFilterSet set) throws SQLException {
		DatabaseIterator<TreeIdentifier, UserValues<TreeIdentifier>> iterator = getTreeUserData().valueIterator();
		try {
			while (iterator.hasNext()) {
				UserValues<TreeIdentifier> rowData = iterator.next();
				
				double value = getUserValue(rowData.getUserValues(), Number.class).doubleValue();
				if ((getDefinition().isBelowThreshold() && (value <= threshold.getThreshold())) ||
						(!getDefinition().isBelowThreshold() && (value >= threshold.getThreshold()))) {
					
					set.getTrees().add(rowData.getKey());
				}
			}
		}
		finally {
			iterator.close();
		}
	}
}
