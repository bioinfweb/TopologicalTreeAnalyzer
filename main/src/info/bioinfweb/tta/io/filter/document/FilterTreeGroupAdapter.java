package info.bioinfweb.tta.io.filter.document;


import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import info.bioinfweb.jphyloio.ReadWriteParameterMap;
import info.bioinfweb.jphyloio.dataadapters.JPhyloIOEventReceiver;
import info.bioinfweb.jphyloio.dataadapters.ObjectListDataAdapter;
import info.bioinfweb.jphyloio.dataadapters.TreeNetworkDataAdapter;
import info.bioinfweb.jphyloio.dataadapters.TreeNetworkGroupDataAdapter;
import info.bioinfweb.jphyloio.dataadapters.implementations.EmptyObjectListDataAdapter;
import info.bioinfweb.jphyloio.events.LinkedLabeledIDEvent;
import info.bioinfweb.jphyloio.events.type.EventContentType;
import info.bioinfweb.tta.io.filter.TreeFilterSet;
import info.bioinfweb.tta.io.treeiterator.FilterTreeIterator;



public class FilterTreeGroupAdapter implements TreeNetworkGroupDataAdapter {
	private TreeFilterSet filterSet;
	private List<String> treeFilesNames;
	
	
	public FilterTreeGroupAdapter(TreeFilterSet filterSet, List<String> treeFilesNames) {
		super();
		this.filterSet = filterSet;
		this.treeFilesNames = treeFilesNames;
	}


	@Override
	public void writeMetadata(ReadWriteParameterMap parameters, JPhyloIOEventReceiver receiver) throws IOException {}
	
	
	@Override
	public LinkedLabeledIDEvent getStartEvent(ReadWriteParameterMap parameters) {
		return new LinkedLabeledIDEvent(EventContentType.TREE_NETWORK_GROUP, "treeGroup", null, null);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public ObjectListDataAdapter<LinkedLabeledIDEvent> getTreeSets(ReadWriteParameterMap parameters) {
		return EmptyObjectListDataAdapter.SHARED_EMPTY_OBJECT_LIST_ADAPTER;
	}
	
	
	@Override
	public Iterator<TreeNetworkDataAdapter> getTreeNetworkIterator(ReadWriteParameterMap parameters) {
		//TODO The last file opened by this iterator will not be closed. Where could this be done? 
		try {
			final FilterTreeIterator iterator = new FilterTreeIterator(filterSet, treeFilesNames);
			return new Iterator<TreeNetworkDataAdapter>() {
				@Override
				public boolean hasNext() {
					try {
						return iterator.hasNext();
					} 
					catch (Exception e) {
						throw new InternalError(e);  //TODO IOExceptions should be thrown in a better way. (Possibly use specific wrapper exception.)
					}
				}
				

				@Override
				public TreeNetworkDataAdapter next() {
					try {
						return iterator.next().getTree();
					} 
					catch (Exception e) {
						throw new InternalError(e);  //TODO IOExceptions should be thrown in a better way. (Possibly use specific wrapper exception.)
					}
				}
			};
		} 
		catch (Exception e) {
			throw new InternalError(e);  //TODO IOExceptions should be thrown in a better way. (Possibly use specific wrapper exception.)
		}
	}
}
