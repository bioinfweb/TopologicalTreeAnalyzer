package info.bioinfweb.osrfilter.io;


import java.io.IOException;
import java.util.Map;

import info.bioinfweb.jphyloio.JPhyloIOEventReader;
import info.bioinfweb.jphyloio.events.EdgeEvent;
import info.bioinfweb.jphyloio.events.JPhyloIOEvent;
import info.bioinfweb.jphyloio.events.type.EventContentType;
import info.bioinfweb.jphyloio.events.type.EventTopologyType;
import info.bioinfweb.jphyloio.utils.JPhyloIOReadingUtils;
import info.bioinfweb.osrfilter.Tree;



public class TreeLoader {
	private void readEdge(EdgeEvent event, Tree tree) {
		//TODO 
		// - Create new LeafSet for this edge.
		// - Add entry for target node (Map nodeID -> TerminalName -> Index). -> Actually, it must be the terminal under target node.
		// - Add entry for target node to all splits that are parent of source node, i.e., all LeafSets that currently contain the index of source node-
		//   - How can these be determined efficiently? Using a tree data structure that links leaf sets? If so, should the existing TreeGraph implementation be used to determine LeafSets?
		//   - When searching a matching leaf set later, a tree structure or another way to efficiently search for it will be required as well.
		// => It looks like using a tree data structure makes sense. Should the TG implementation be used or is a more memory efficient way of storing trees required?
		
		//tree.getSplits().put(event.getID(), value)
		
	}
	
	public Tree loadTree(JPhyloIOEventReader reader, Map<String, Integer> leafValueToIndexMap) throws IOException {  //TODO Possibly reconsider String as the value type
		Tree result = new Tree();
		
		// Process JPhyloIO events:
		JPhyloIOEvent event = reader.next();  
		while ((!event.getType().getTopologyType().equals(EventTopologyType.END))) {  // Read until tree end  
			if (event.getType().getContentType().equals(EventContentType.EDGE)) {
				readEdge(event.asEdgeEvent(), result);
				JPhyloIOReadingUtils.reachElementEnd(reader);
			}
			else {
				JPhyloIOReadingUtils.reachElementEnd(reader);
			}
			event = reader.next();  // Read the next event from the JPhyloIO reader.          
		}		
		
		return result;
	}
}
