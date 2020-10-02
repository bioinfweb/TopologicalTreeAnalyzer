package info.bioinfweb.tta.io.filter.document;


import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.iterators.SingletonIterator;

import info.bioinfweb.jphyloio.ReadWriteParameterMap;
import info.bioinfweb.jphyloio.dataadapters.DocumentDataAdapter;
import info.bioinfweb.jphyloio.dataadapters.TreeNetworkGroupDataAdapter;
import info.bioinfweb.jphyloio.dataadapters.implementations.EmptyDocumentDataAdapter;
import info.bioinfweb.tta.io.filter.TreeFilterSet;



public class FilterDocumentDataAdapter extends EmptyDocumentDataAdapter implements DocumentDataAdapter {
	private FilterTreeGroupAdapter treeGroup;
	
	
	public FilterDocumentDataAdapter(TreeFilterSet filterSet, List<String> treeFilesNames) {
		super();
		treeGroup = new FilterTreeGroupAdapter(filterSet, treeFilesNames);
	}

	
	@Override
	public Iterator<TreeNetworkGroupDataAdapter> getTreeNetworkGroupIterator(ReadWriteParameterMap parameters) {
		return new SingletonIterator<TreeNetworkGroupDataAdapter>(treeGroup, false);
	}
}
