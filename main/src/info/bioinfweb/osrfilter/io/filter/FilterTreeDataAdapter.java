package info.bioinfweb.osrfilter.io.filter;


import info.bioinfweb.jphyloio.ReadWriteParameterMap;
import info.bioinfweb.jphyloio.dataadapters.ObjectListDataAdapter;
import info.bioinfweb.jphyloio.dataadapters.TreeNetworkDataAdapter;
import info.bioinfweb.jphyloio.dataadapters.implementations.NoSetsTreeNetworkDataAdapter;
import info.bioinfweb.jphyloio.events.EdgeEvent;
import info.bioinfweb.jphyloio.events.LabeledIDEvent;
import info.bioinfweb.jphyloio.events.NodeEvent;



public class FilterTreeDataAdapter extends NoSetsTreeNetworkDataAdapter implements TreeNetworkDataAdapter {
	@Override
	public LabeledIDEvent getStartEvent(ReadWriteParameterMap parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public boolean isTree(ReadWriteParameterMap parameters) {
		return true;
	}

	
	@Override
	public ObjectListDataAdapter<NodeEvent> getNodes(ReadWriteParameterMap parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public ObjectListDataAdapter<EdgeEvent> getEdges(ReadWriteParameterMap parameters) {
		// TODO Auto-generated method stub
		return null;
	}
}
