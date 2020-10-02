package info.bioinfweb.tta.io.filter;


import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import info.bioinfweb.tta.data.TreeData;
import info.bioinfweb.tta.data.TreeIdentifier;
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
	public <D extends TreeFilterDefinition> TreeFilter<D> createTreeFilter(D definition, Map<TreeIdentifier, TreeData> treeDataMap) {
		try {
			return (TreeFilter<D>)map.get(definition.getClass()).getConstructor(definition.getClass(), Map.class).newInstance(definition, treeDataMap);
		} 
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			
			throw new InternalError(e);
		}
	}
}
