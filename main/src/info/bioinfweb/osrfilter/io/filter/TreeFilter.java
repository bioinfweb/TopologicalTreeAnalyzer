package info.bioinfweb.osrfilter.io.filter;


import java.util.Iterator;
import java.util.Map;

import info.bioinfweb.commons.io.ContentExtensionFileFilter.TestStrategy;
import info.bioinfweb.osrfilter.data.TreeData;
import info.bioinfweb.osrfilter.data.TreeIdentifier;
import info.bioinfweb.osrfilter.data.parameters.filter.TreeFilterDefinition;
import info.bioinfweb.osrfilter.data.parameters.filter.TreeFilterThreshold;
import info.bioinfweb.osrfilter.exception.InvalidParameterTypeException;
import info.bioinfweb.osrfilter.io.TreeWriter;



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
