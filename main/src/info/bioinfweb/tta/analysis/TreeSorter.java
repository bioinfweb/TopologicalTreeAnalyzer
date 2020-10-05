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
package info.bioinfweb.tta.analysis;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import info.bioinfweb.tta.data.TreeData;
import info.bioinfweb.tta.data.TreeIdentifier;
import info.bioinfweb.tta.exception.InvalidParameterTypeException;



/**
 * Allows to sort a list of tree identifiers by a user value stored in a tree data map.
 * <p>
 * The actual tree topologies are not required for this operation after the tree data map has been created.
 * 
 * @author Ben St&ouml;ver
 */
public class TreeSorter {
	public static List<TreeIdentifier> sort(final Map<TreeIdentifier, TreeData> treeDataMap, final String userValueName, final boolean upwards) 
			throws InvalidParameterTypeException {
		
		List<TreeIdentifier> result = new ArrayList<TreeIdentifier>(treeDataMap.size());
		result.addAll(treeDataMap.keySet());
		
		result.sort(new Comparator<TreeIdentifier>() {
			private double getUserValue(TreeIdentifier identifier) {
				Object value = treeDataMap.get(identifier).getUserValues().get(userValueName);
				if (value instanceof Number) {
					return ((Number)value).doubleValue();
				}
				else {
					throw new InvalidParameterTypeException("The user expression \"" + userValueName + 
							"\" used to filter the tree output did not produce a numeric result, although used with an numeric tree filter.");
				}
			}
			
			@Override
			public int compare(TreeIdentifier identifierA, TreeIdentifier identifierB) {
				int result = (int)(getUserValue(identifierA) - getUserValue(identifierB));
				if (upwards) {
					return result;
				}
				else {
					return -result;
				}
			}
		});
		
		return result;
	}
}
