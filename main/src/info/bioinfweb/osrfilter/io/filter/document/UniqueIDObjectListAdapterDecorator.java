package info.bioinfweb.osrfilter.io.filter.document;


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
