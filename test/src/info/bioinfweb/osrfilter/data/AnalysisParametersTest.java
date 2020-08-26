package info.bioinfweb.osrfilter.data;


import static org.junit.Assert.*;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;



public class AnalysisParametersTest {
	@Test
	public void test_Unmashalling() throws JAXBException {
		AnalysisParameters parameters = JAXBContext.newInstance(AnalysisParameters.class).createUnmarshaller().unmarshal(
				new StreamSource(new File("data/parameters/parameters.xml")), AnalysisParameters.class).getValue();
				
		//assertEquals(4, info.getConstants().size());
	}
	
	
	public static void main(String[] args) throws JAXBException {
		AnalysisParameters parameters = new AnalysisParameters();
		parameters.setGroupSize(10);
		parameters.getTreeFilesNames().add("data/Tree1.tre");
		parameters.getTreeFilesNames().add("data/Tree2.tre");
		
		parameters.getUserExpressions().getExpressions().put("treeExp0", new UserExpression(true, "terminals()"));
		parameters.getUserExpressions().getOrder().add("treeExp0");
		parameters.getUserExpressions().getExpressions().put("treeExp1", new UserExpression(true, "2 * treeUserValue(\"treeExp0\")"));
		parameters.getUserExpressions().getOrder().add("treeExp1");
		parameters.getUserExpressions().getExpressions().put("pairExp0", new UserExpression(false, "c(0)"));
		parameters.getUserExpressions().getOrder().add("pairExp0");
		parameters.getUserExpressions().getExpressions().put("pairExp1", new UserExpression(false, "2 * pairUserValue(\"treeExp0\")"));
		parameters.getUserExpressions().getOrder().add("pairExp1");
		
		parameters.getExportColumns().add("treeExp1");
		parameters.getExportColumns().add("pairExp1");
		
		Marshaller marshaller = JAXBContext.newInstance(AnalysisParameters.class).createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(parameters, System.out);
	}
}
