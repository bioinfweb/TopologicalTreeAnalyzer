package info.bioinfweb.osrfilter.io.filter.document;


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
import info.bioinfweb.osrfilter.io.filter.TreeFilterSet;
import info.bioinfweb.osrfilter.io.treeiterator.FilterTreeIterator;



public class FilterTreeGroupAdapter implements TreeNetworkGroupDataAdapter {
	private TreeFilterSet filterSet;
	private List<String> treeFilesNames;
	
	
	public FilterTreeGroupAdapter(TreeFilterSet filterSet, List<String> treeFilesNames) {
		super();
		this.filterSet = filterSet;
		this.treeFilesNames = treeFilesNames;
	}
	//TODO Load trees until group size and switch group when necessary. Wrap adapters around them.
	//     - treeIdentifiers must be ordered by the input order for this to be efficient. (If trees should be resorted several write operations and possibly adapter will be required.)
	//TODO Possibly also accept loaded trees in the future if group size is not lower than overall tree count.


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
