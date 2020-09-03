package info.bioinfweb.osrfilter.io.filter.document;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.iterators.SingletonIterator;

import info.bioinfweb.jphyloio.ReadWriteParameterMap;
import info.bioinfweb.jphyloio.dataadapters.DocumentDataAdapter;
import info.bioinfweb.jphyloio.dataadapters.JPhyloIOEventReceiver;
import info.bioinfweb.jphyloio.dataadapters.ObjectListDataAdapter;
import info.bioinfweb.jphyloio.dataadapters.TreeNetworkDataAdapter;
import info.bioinfweb.jphyloio.dataadapters.TreeNetworkGroupDataAdapter;
import info.bioinfweb.jphyloio.dataadapters.implementations.EmptyDocumentDataAdapter;
import info.bioinfweb.jphyloio.dataadapters.implementations.EmptyObjectListDataAdapter;
import info.bioinfweb.jphyloio.events.LinkedLabeledIDEvent;



public class FilterDocumentDataAdapter extends EmptyDocumentDataAdapter implements DocumentDataAdapter {
	private List<TreeNetworkGroupDataAdapter> treeGroupList;
	
	
	public FilterDocumentDataAdapter() {
		super();
		treeGroupList = new ArrayList<TreeNetworkGroupDataAdapter>();
		treeGroupList.add(new TreeNetworkGroupDataAdapter() {
			@Override
			public void writeMetadata(ReadWriteParameterMap parameters, JPhyloIOEventReceiver receiver) throws IOException {}
			
			
			@Override
			public LinkedLabeledIDEvent getStartEvent(ReadWriteParameterMap parameters) {
				// TODO Auto-generated method stub
				return null;
			}
			
			
			@SuppressWarnings("unchecked")
			@Override
			public ObjectListDataAdapter<LinkedLabeledIDEvent> getTreeSets(ReadWriteParameterMap parameters) {
				return EmptyObjectListDataAdapter.SHARED_EMPTY_OBJECT_LIST_ADAPTER;
			}
			
			
			@Override
			public Iterator<TreeNetworkDataAdapter> getTreeNetworkIterator(ReadWriteParameterMap parameters) {
				// TODO Auto-generated method stub
				return null;
			}
		});
	}

	
	@Override
	public Iterator<TreeNetworkGroupDataAdapter> getTreeNetworkGroupIterator(ReadWriteParameterMap parameters) {
		//return new SingletonIterator<TreeNetworkGroupDataAdapter>(object, false);
		return null;
	}
}
