package info.bioinfweb.osrfilter.data;


import static org.junit.Assert.*;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Test;

import info.bioinfweb.osrfilter.data.parameters.AnalysisParameters;
import info.bioinfweb.osrfilter.data.parameters.TreeFilter;
import info.bioinfweb.osrfilter.data.parameters.TreeFilterThreshold;
import info.bioinfweb.osrfilter.io.parameters.AnalysisParameterIO;



public class AnalysisParametersTest {
	private static void assertUserExpression(boolean expectedTree, String expectedExpression, UserExpression expression) {
		assertEquals(expectedTree, expression.hasTreeTarget());
		assertEquals(expectedExpression, expression.getExpression());
	}
	
	
	private static void assertTreeFilter(String expectedName, String expectedUserValueName, boolean expectedRelativeTheshold, 
			boolean expectedBelowTheshold, String expectedDefaultFormat, TreeFilter filter) {
		
		assertEquals(expectedName, filter.getName());
		assertEquals(expectedUserValueName, filter.getTreeUserValueName());
		assertEquals(expectedRelativeTheshold, filter.isRelativeThreshold());
		assertEquals(expectedBelowTheshold, filter.isBelowThreshold());
		assertEquals(expectedDefaultFormat, filter.getDefaultFormat());
	}
	
	
	@Test
	public void test_Unmashalling() throws JAXBException {
		AnalysisParameters parameters = AnalysisParameterIO.getInstance().read(new File("data/parameters/parameters.xml"));
				
		assertEquals(10, parameters.getGroupSize());
		
		assertEquals(2, parameters.getTreeFilesNames().size());
		assertEquals("data/Tree1.tre", parameters.getTreeFilesNames().get(0));
		assertEquals("data/Tree2.tre", parameters.getTreeFilesNames().get(1));
		
		assertEquals(4, parameters.getUserExpressions().getExpressions().size());
		assertEquals(4, parameters.getUserExpressions().getOrder().size());
		assertEquals("treeExp0", parameters.getUserExpressions().getOrder().get(0));
		assertEquals("treeExp1", parameters.getUserExpressions().getOrder().get(1));
		assertEquals("pairExp0", parameters.getUserExpressions().getOrder().get(2));
		assertEquals("pairExp1", parameters.getUserExpressions().getOrder().get(3));
		assertUserExpression(true, "terminals()", parameters.getUserExpressions().getExpressions().get("treeExp0"));
		assertUserExpression(true, "2 * treeUserValue(\"treeExp0\")", parameters.getUserExpressions().getExpressions().get("treeExp1"));
		assertUserExpression(false, "c(0)", parameters.getUserExpressions().getExpressions().get("pairExp0"));
		assertUserExpression(false, "2 * pairUserValue(\"pairExp0\")", parameters.getUserExpressions().getExpressions().get("pairExp1"));
		
		assertEquals(new File("data/parameters/output"), parameters.getOutputDirectory());
		
		assertEquals(1, parameters.getFilters().size());
		TreeFilter treeFilter = parameters.getFilters().iterator().next();
		assertTreeFilter("filter0", "treeExp0", false, false, "nexml", treeFilter);
		assertEquals(2.0, treeFilter.getThresholds().get(0).getThreshold(), 0.0000001);
		assertNull(treeFilter.getThresholds().get(0).getFormat());
		assertEquals(4.0, treeFilter.getThresholds().get(1).getThreshold(), 0.0000001);
		assertEquals("newick", treeFilter.getThresholds().get(1).getFormat());
		
		assertEquals("\r\n", parameters.getTreeExportColumns().getLineDelimiter());
		assertEquals("\t", parameters.getTreeExportColumns().getColumnDelimiter());
		assertEquals(1, parameters.getTreeExportColumns().getColumns().size());
		assertEquals("treeExp1", parameters.getTreeExportColumns().getColumns().get(0));
		
		assertEquals("\r\n", parameters.getPairExportColumns().getLineDelimiter());
		assertEquals("\t", parameters.getPairExportColumns().getColumnDelimiter());
		assertEquals(2, parameters.getPairExportColumns().getColumns().size());
		assertEquals("pairExp0", parameters.getPairExportColumns().getColumns().get(0));
		assertEquals("pairExp1", parameters.getPairExportColumns().getColumns().get(1));
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
		
		parameters.setOutputDirectory(new File("data/parameters/output"));
		
		parameters.getTreeExportColumns().getColumns().add("treeExp1");
		parameters.getPairExportColumns().getColumns().add("pairExp0");
		parameters.getPairExportColumns().getColumns().add("pairExp1");
		
		TreeFilter treeFilter = new TreeFilter("filter0", "treeExp0", false, false, "nexml");
		treeFilter.getThresholds().add(new TreeFilterThreshold(2.0, null));
		treeFilter.getThresholds().add(new TreeFilterThreshold(4.0, "newick"));
		parameters.getFilters().add(treeFilter);
		
		Marshaller marshaller = JAXBContext.newInstance(AnalysisParameters.class).createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(parameters, System.out);
	}
}
