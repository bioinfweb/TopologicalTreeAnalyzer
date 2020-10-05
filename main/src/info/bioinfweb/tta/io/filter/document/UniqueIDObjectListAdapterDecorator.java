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
import java.util.Iterator;

import info.bioinfweb.jphyloio.ReadWriteParameterMap;
import info.bioinfweb.jphyloio.dataadapters.JPhyloIOEventReceiver;
import info.bioinfweb.jphyloio.dataadapters.ObjectListDataAdapter;
import info.bioinfweb.jphyloio.events.JPhyloIOEvent;



public class UniqueIDObjectListAdapterDecorator<E extends JPhyloIOEvent> implements ObjectListDataAdapter<E> {
	private ObjectListDataAdapter<E> underlyingAdapter;
	private PrefixEventReplacer eventReplacer;
	

	public UniqueIDObjectListAdapterDecorator(ObjectListDataAdapter<E> underlyingAdapter,
			PrefixEventReplacer eventReplacer) {
		super();
		this.underlyingAdapter = underlyingAdapter;
		this.eventReplacer = eventReplacer;
	}


	@Override
	public E getObjectStartEvent(ReadWriteParameterMap parameters, String id) throws IllegalArgumentException {
		return eventReplacer.replaceEvent(underlyingAdapter.getObjectStartEvent(parameters, eventReplacer.removePrefix(id)));
	}

	
	@Override
	public long getCount(ReadWriteParameterMap parameters) {
		return underlyingAdapter.getCount(parameters);
	}
	

	@Override
	public Iterator<String> getIDIterator(ReadWriteParameterMap parameters) {
		final Iterator<String> iterator = underlyingAdapter.getIDIterator(parameters);
		return new Iterator<String>() {
			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public String next() {
				return eventReplacer.getPrefix() + iterator.next();
			}
		};
	}

	
	@Override
	public void writeContentData(ReadWriteParameterMap parameters, JPhyloIOEventReceiver receiver, String id)
			throws IOException, IllegalArgumentException {

		underlyingAdapter.writeContentData(parameters, new UniqueIDEventReceiverDecorator(receiver, eventReplacer), eventReplacer.removePrefix(id));
	}
}
