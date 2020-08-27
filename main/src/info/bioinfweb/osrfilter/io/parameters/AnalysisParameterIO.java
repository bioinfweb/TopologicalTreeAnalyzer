package info.bioinfweb.osrfilter.io.parameters;


import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.helpers.DefaultValidationEventHandler;
import javax.xml.transform.stream.StreamSource;

import info.bioinfweb.osrfilter.data.parameters.AnalysisParameters;



public class AnalysisParameterIO {
	private static AnalysisParameterIO firstInstance = null;
	
	private Unmarshaller unmarshaller;
	private Marshaller marshaller;
	
	
	private AnalysisParameterIO() throws JAXBException {
		super();
		JAXBContext context = JAXBContext.newInstance(AnalysisParameters.class);
		
		unmarshaller = context.createUnmarshaller();
		unmarshaller.setEventHandler(new DefaultValidationEventHandler());  // Make sure that exceptions from adapters are thrown.
		
		marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	}
	
	
	public static AnalysisParameterIO getInstance() throws JAXBException {
		if (firstInstance == null) {
			firstInstance = new AnalysisParameterIO();
		}
		return firstInstance;
	}


	public AnalysisParameters read(File file) throws JAXBException {
		return unmarshaller.unmarshal(new StreamSource(file), AnalysisParameters.class).getValue();
	}
	
	
	public void write(AnalysisParameters parameters, File file) throws JAXBException {
		marshaller.marshal(parameters, file);
	}
}
