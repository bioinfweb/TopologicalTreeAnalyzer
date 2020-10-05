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


import java.util.List;
import java.util.Map;

import info.bioinfweb.tta.analysis.TreeSorter;
import info.bioinfweb.tta.data.TreeData;
import info.bioinfweb.tta.data.TreeIdentifier;
import info.bioinfweb.tta.data.parameters.filter.NumericTreeFilterDefinition;
import info.bioinfweb.tta.data.parameters.filter.TreeFilterThreshold;
import info.bioinfweb.tta.data.parameters.filter.NumericTreeFilterDefinition.Relative;



public class RelativeNumericTreeFilter extends NumericTreeFilter<NumericTreeFilterDefinition.Relative> {
	public RelativeNumericTreeFilter(Relative definition, Map<TreeIdentifier, TreeData> treeDataMap) {
		super(definition, treeDataMap);
	}

	
	@Override
	protected void fillSet(TreeFilterThreshold threshold, TreeFilterSet set) {
		List<TreeIdentifier> list = TreeSorter.sort(getTreeDataMap(), getDefinition().getTreeUserValueName(), getDefinition().isBelowThreshold());
		set.getTrees().addAll(list.subList(0, (int)Math.round(threshold.getThreshold() * list.size())));
	}
}
