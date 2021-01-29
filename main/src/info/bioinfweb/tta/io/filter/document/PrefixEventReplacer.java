/*
 * Topological Tree Analyzer - User defined topological analyses of large sets of phylogenetic trees. 
 * Copyright (C) 2020-2021  Ben C. Stöver
 * <http://bioinfweb.info/TTA>
 * 
 * This file is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package info.bioinfweb.tta.io.filter.document;


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
