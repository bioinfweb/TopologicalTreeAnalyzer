package info.bioinfweb.tta.io.filter.document;


import java.io.IOException;

import info.bioinfweb.jphyloio.dataadapters.JPhyloIOEventReceiver;
import info.bioinfweb.jphyloio.events.JPhyloIOEvent;



public class UniqueIDEventReceiverDecorator implements JPhyloIOEventReceiver {
	private JPhyloIOEventReceiver underlyingReceiver;
	private PrefixEventReplacer eventReplacer;
	

	public UniqueIDEventReceiverDecorator(JPhyloIOEventReceiver underlyingReceiver, PrefixEventReplacer eventReplacer) {
		super();
		this.underlyingReceiver = underlyingReceiver;
		this.eventReplacer = eventReplacer;
	}

	
	@Override
	public boolean add(JPhyloIOEvent event) throws IOException {
		return underlyingReceiver.add(eventReplacer.replaceEvent(event));
	}
}
