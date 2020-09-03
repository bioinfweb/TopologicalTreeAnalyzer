package info.bioinfweb.osrfilter.io;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import info.bioinfweb.jphyloio.dataadapters.implementations.store.StoreTreeNetworkDataAdapter;
import info.bioinfweb.osrfilter.data.TTATree;
import info.bioinfweb.osrfilter.data.TreeData;
import info.bioinfweb.osrfilter.data.TreeIdentifier;
import info.bioinfweb.osrfilter.data.parameters.filter.TreeFilterDefinition;
import info.bioinfweb.osrfilter.io.filter.TreeFilter;
import info.bioinfweb.osrfilter.io.filter.TreeFilterFactory;
import info.bioinfweb.osrfilter.io.filter.TreeFilterSet;
import info.bioinfweb.osrfilter.io.treeiterator.FilterTreeIterator;



public class TreeWriter {
	//TODO Move functionality of this method to FilterTreeNetworkGroupAdapter.
	private void writeFilteredOutput(TreeFilterSet set, List<String> treeFilesNames) throws IOException, Exception {
		FilterTreeIterator iterator = new FilterTreeIterator(set, treeFilesNames);
		while (iterator.hasNext()) {
			TTATree<StoreTreeNetworkDataAdapter> tree = iterator.next();
			if (set.getTrees().contains(tree.getTreeIdentifier())) {
				//TODO Write tree to output.
			}
		}
	}
	
	
	public void writeFilterOutputs(TreeFilterDefinition definition, List<String> treeFilesNames, Map<TreeIdentifier, TreeData> treeDataMap) throws IOException, Exception {
		//TODO Load trees one by one and write the filtered tree to an output file. Store single trees in a StoreTreeNetworkAdapter.
		
		//TODO Possibly sort trees here later. (Using multiple temporary files.)
	
		
		TreeFilter<?> filter = TreeFilterFactory.getInstance().createTreeFilter(definition, treeDataMap);
		
		
	}
}
