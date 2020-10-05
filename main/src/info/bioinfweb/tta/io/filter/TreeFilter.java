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


import java.util.Iterator;
import java.util.Map;

import info.bioinfweb.commons.io.ContentExtensionFileFilter.TestStrategy;
import info.bioinfweb.tta.data.TreeData;
import info.bioinfweb.tta.data.TreeIdentifier;
import info.bioinfweb.tta.data.parameters.filter.TreeFilterDefinition;
import info.bioinfweb.tta.data.parameters.filter.TreeFilterThreshold;
import info.bioinfweb.tta.exception.InvalidParameterTypeException;
import info.bioinfweb.tta.io.TreeWriter;



public abstract class TreeFilter<D extends TreeFilterDefinition> implements Iterator<TreeFilterSet> {
	private D definition;
	private Map<TreeIdentifier, TreeData> treeDataMap;
	
	
	public TreeFilter(D definition, Map<TreeIdentifier, TreeData> treeDataMap) {
		super();
		this.definition = definition;
		this.treeDataMap = treeDataMap;
	}


	public D getDefinition() {
		return definition;
	}


	protected Map<TreeIdentifier, TreeData> getTreeDataMap() {
		return treeDataMap;
	}
	
	
	protected String determineFormat(TreeFilterThreshold threshold) {
		String result = threshold.getFormat();
		if (result == null) {
			result = getDefinition().getDefaultFormat();
		}
		return result;
	}


	protected String getFileExtension(String format) {
		return "." + TreeWriter.READER_WRITER_FACTORY.getFormatInfo(format).createFileFilter(TestStrategy.EXTENSION).getDefaultExtension();
	}


	protected <T> T getUserValue(TreeData treeData, Class<T> valueType) {
		Object value = treeData.getUserValues().get(getDefinition().getTreeUserValueName());
		if (valueType.isAssignableFrom(value.getClass())) {
			return valueType.cast(value);
		}
		else {
			throw new InvalidParameterTypeException("The user expression \"" + getDefinition().getTreeUserValueName() + 
					"\" used to filter the tree output did produce a result of type " + value.getClass().getName() + " instead of " + valueType.getName() + 
					" although used with a tree filter that requires this type.");
		}
	}
}
