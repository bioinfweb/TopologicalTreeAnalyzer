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
package info.bioinfweb.tta.io.parameters;


import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.helpers.DefaultValidationEventHandler;
import javax.xml.transform.stream.StreamSource;

import info.bioinfweb.tta.data.parameters.AnalysisParameters;



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
