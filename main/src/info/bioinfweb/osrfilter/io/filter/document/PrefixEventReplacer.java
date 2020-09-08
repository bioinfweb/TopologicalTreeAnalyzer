package info.bioinfweb.osrfilter.io.filter.document;


import info.bioinfweb.jphyloio.events.EdgeEvent;
import info.bioinfweb.jphyloio.events.JPhyloIOEvent;
import info.bioinfweb.jphyloio.events.LabeledIDEvent;
import info.bioinfweb.jphyloio.events.LinkedIDEvent;



public class PrefixEventReplacer {
	private String prefix;
	
	
	public PrefixEventReplacer(String prefix) {
		super();
		if (prefix != null) {
			this.prefix = prefix;
		}
		else {
			throw new IllegalArgumentException("prefix must not be null");
		}
	}


	public String getPrefix() {
		return prefix;
	}


	public String removePrefix(String id) {
		return id.substring(prefix.length());
	}
	
	
	public String addPrefix(String id) {
		if (id == null) {
			return id;  // Root edges can have null as ID references.
		}
		else {
			return prefix + id;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public <E extends JPhyloIOEvent> E replaceEvent(E event) {
		if (event instanceof LabeledIDEvent) {
			LabeledIDEvent labeledIDEvent = event.asLabeledIDEvent();
			String newID = addPrefix(labeledIDEvent.getID());
			
			if (event instanceof LinkedIDEvent) {
				LinkedIDEvent linkedIDEvent = event.asLinkedIDEvent();
				return (E)linkedIDEvent.cloneWithNewIDs(newID, addPrefix(linkedIDEvent.getLinkedID()));
			}
			else if (event instanceof EdgeEvent) {
				EdgeEvent edgeEvent = event.asEdgeEvent();
				return (E)edgeEvent.cloneWithNewIDs(newID, addPrefix(edgeEvent.getSourceID()), addPrefix(edgeEvent.getTargetID()));
			}
			else {  // LabeledIDEvent and all other inherited classes
				return (E)labeledIDEvent.cloneWithNewID(newID);
			}
		}
		else {
			return event;
		}
	}
}
