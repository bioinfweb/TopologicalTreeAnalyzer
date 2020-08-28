package info.bioinfweb.osrfilter.io.filter;


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
import info.bioinfweb.osrfilter.data.TreeIdentifier;
import info.bioinfweb.osrfilter.data.parameters.AnalysisParameters;



public class FilterTreeGroupAdapter implements TreeNetworkGroupDataAdapter {
	private List<TreeIdentifier> treeIdentifiers;
	private AnalysisParameters analysisParameters;
	
	
	public FilterTreeGroupAdapter(List<TreeIdentifier> treeIdentifiers, AnalysisParameters analysisParameters) {
		super();
		this.treeIdentifiers = treeIdentifiers;
		this.analysisParameters = analysisParameters;
	}
	//TODO Load trees until group size and switch group when necessary. Wrap adapters around them.
	//     - treeIdentifiers must be ordered by the input order for this to be efficient. (If trees should be resorted several write operations and possibly adapter will be required.)
	//TODO Possibly also accept loaded trees in the future if group size is not lower than overall tree count.


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
}
