package info.bioinfweb.osrfilter.io.treeiterator;


import java.util.HashMap;
import java.util.Map;

import info.bioinfweb.jphyloio.events.LabeledIDEvent;
import info.bioinfweb.jphyloio.events.idediting.IntegerIDEditor;
import info.bioinfweb.jphyloio.events.type.EventContentType;



public class MappingIDEditor extends IntegerIDEditor {
	private Map<String, String> treeIDMap = new HashMap<>();
	
	
	@Override
	public String editID(String id, LabeledIDEvent event) {
		String result = super.editID(id, event);
		if (EventContentType.TREE.equals(event.getType().getContentType())) {
			treeIDMap.put(result, id);
		}
		return result;
	}
	
	
	public String oldIDByNew(String oldID) {
		return treeIDMap.get(oldID);
	}
}
