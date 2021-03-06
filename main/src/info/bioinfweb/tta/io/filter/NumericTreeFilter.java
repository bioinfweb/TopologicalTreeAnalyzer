/*
 * Topological Tree Analyzer - User defined topological analyses of large sets of phylogenetic trees. 
 * Copyright (C) 2020-2021  Ben C. Stöver
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
import java.util.Iterator;

import info.bioinfweb.tta.data.database.TreeUserDataTable;
import info.bioinfweb.tta.data.parameters.filter.NumericTreeFilterDefinition;
import info.bioinfweb.tta.data.parameters.filter.TreeFilterThreshold;



public abstract class NumericTreeFilter<D extends NumericTreeFilterDefinition> extends TreeFilter<D> {
	private Iterator<TreeFilterThreshold> thresholdIterator;
	
	
	public NumericTreeFilter(D definition, TreeUserDataTable treeUserData) {
		super(definition, treeUserData);
		thresholdIterator = getDefinition().getThresholds().iterator();
	}

	
	@Override
	public boolean hasNext() {
		return thresholdIterator.hasNext();
	}

	
	protected abstract void fillSet(TreeFilterThreshold threshold, TreeFilterSet set) throws SQLException;
	
	
	protected String determineFormat(TreeFilterThreshold threshold) {
		String result = threshold.getFormat();
		if (result == null) {
			result = getDefinition().getDefaultFormat();
		}
		return result;
	}


	@Override
	public TreeFilterSet next() throws SQLException {
		TreeFilterThreshold threshold = thresholdIterator.next();
		String format = determineFormat(threshold);
		TreeFilterSet result = new TreeFilterSet(getDefinition().getName() + "_" + 
				Double.toString(threshold.getThreshold()).replaceAll("\\-", "m") + getFileExtension(format), format);  // Avoid "-" since it is invalid in file names.
		fillSet(threshold, result);
		return result;
	}
}
