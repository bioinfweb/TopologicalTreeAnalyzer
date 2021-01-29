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


import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import info.bioinfweb.tta.data.database.TreeUserDataTable;
import info.bioinfweb.tta.data.parameters.filter.BooleanTreeFilterDefinition;
import info.bioinfweb.tta.data.parameters.filter.NumericTreeFilterDefinition;
import info.bioinfweb.tta.data.parameters.filter.TreeFilterDefinition;



public class TreeFilterFactory {
	private static TreeFilterFactory firstInstance = null;
	
	private Map<Class<? extends TreeFilterDefinition>, Class<? extends TreeFilter<?>>> map;
	
	
	private TreeFilterFactory() {
		super();
		fillMap();
	}
	
	
	public static TreeFilterFactory getInstance() {
		if (firstInstance == null) {
			firstInstance = new TreeFilterFactory();
		}
		return firstInstance;
	}
	
	
	private void fillMap() {
		map = new HashMap<>();
		map.put(BooleanTreeFilterDefinition.class, BooleanTreeFilter.class);
		map.put(NumericTreeFilterDefinition.Absolute.class, AbsoluteNumericTreeFilter.class);
		map.put(NumericTreeFilterDefinition.Relative.class, RelativeNumericTreeFilter.class);
	}
	
	
	@SuppressWarnings("unchecked")
	public <D extends TreeFilterDefinition> TreeFilter<D> createTreeFilter(D definition, TreeUserDataTable treeUserData) {
		try {
			return (TreeFilter<D>)map.get(definition.getClass()).getConstructor(definition.getClass(), TreeUserDataTable.class).newInstance(definition, treeUserData);
		} 
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			
			throw new InternalError(e);
		}
	}
}
