/*
 * Topological Tree Analyzer - User defined topological analyses of large sets of phylogenetic trees. 
 * Copyright (C) 2020  Ben C. Stöver
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


import java.io.IOException;

import info.bioinfweb.jphyloio.ReadWriteParameterMap;
import info.bioinfweb.jphyloio.dataadapters.JPhyloIOEventReceiver;
import info.bioinfweb.jphyloio.dataadapters.ObjectListDataAdapter;
import info.bioinfweb.jphyloio.dataadapters.TreeNetworkDataAdapter;
import info.bioinfweb.jphyloio.dataadapters.implementations.store.StoreTreeNetworkDataAdapter;
import info.bioinfweb.jphyloio.events.EdgeEvent;
import info.bioinfweb.jphyloio.events.LabeledIDEvent;
import info.bioinfweb.jphyloio.events.LinkedLabeledIDEvent;
import info.bioinfweb.jphyloio.events.NodeEvent;



public class UniqueIDTreeAdapterDecorator implements TreeNetworkDataAdapter {
	private StoreTreeNetworkDataAdapter underlyingAdapter;
	private PrefixEventReplacer eventReplacer;

	
	public UniqueIDTreeAdapterDecorator(StoreTreeNetworkDataAdapter underlyingAdapter, PrefixEventReplacer eventReplacer) {
		super();
		this.underlyingAdapter = underlyingAdapter;
		this.eventReplacer = eventReplacer;
	}


	public StoreTreeNetworkDataAdapter getUnderlyingAdapter() {
		return underlyingAdapter;
	}


	public PrefixEventReplacer getEventReplacer() {
		return eventReplacer;
	}


	@Override
	public LabeledIDEvent getStartEvent(ReadWriteParameterMap parameters) {
		return eventReplacer.replaceEvent(underlyingAdapter.getStartEvent(parameters));
	}


	@Override
	public void writeMetadata(ReadWriteParameterMap parameters, JPhyloIOEventReceiver receiver) throws IOException {
		underlyingAdapter.writeMetadata(parameters, new UniqueIDEventReceiverDecorator(receiver, eventReplacer));
	}


	@Override
	public boolean isTree(ReadWriteParameterMap parameters) {
		return underlyingAdapter.isTree(parameters);
	}


	@Override
	public ObjectListDataAdapter<NodeEvent> getNodes(ReadWriteParameterMap parameters) {
		return new UniqueIDObjectListAdapterDecorator<NodeEvent>(underlyingAdapter.getNodes(parameters), eventReplacer);
	}


	@Override
	public ObjectListDataAdapter<EdgeEvent> getEdges(ReadWriteParameterMap parameters) {
		return new UniqueIDObjectListAdapterDecorator<EdgeEvent>(underlyingAdapter.getEdges(parameters), eventReplacer);
	}


	@Override
	public ObjectListDataAdapter<LinkedLabeledIDEvent> getNodeEdgeSets(ReadWriteParameterMap parameters) {
		return underlyingAdapter.getNodeEdgeSets(parameters);  // No ID editing required since the returned set is always empty.
	}
}
