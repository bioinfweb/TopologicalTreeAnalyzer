package info.bioinfweb.osrfilter.data;


import static org.junit.Assert.*;

import java.io.File;
import java.util.Iterator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Test;

import info.bioinfweb.osrfilter.data.parameters.AnalysisParameters;
import info.bioinfweb.osrfilter.data.parameters.filter.BooleanTreeFilterDefinition;
import info.bioinfweb.osrfilter.data.parameters.filter.NumericTreeFilterDefinition;
import info.bioinfweb.osrfilter.data.parameters.filter.TreeFilterDefinition;
import info.bioinfweb.osrfilter.data.parameters.filter.TreeFilterThreshold;
import info.bioinfweb.osrfilter.io.parameters.AnalysisParameterIO;



public class AnalysisParametersTest {
	private static void assertUserExpression(boolean expectedTree, String expectedExpression, UserExpression expression) {
		assertEquals(expectedTree, expression.hasTreeTarget());
		assertEquals(expectedExpression, expression.getExpression());
	}
	
	
	@SuppressWarnings("unchecked")
	private static <F extends TreeFilterDefinition> F assertTreeFilter(Class<F> expectedType, String expectedName, String expectedUserValueName,  
			boolean expectedBelowTheshold, String expectedDefaultFormat, TreeFilterDefinition filter) {
		
		assertEquals(expectedType, filter.getClass());
		assertEquals(expectedName, filter.getName());
		assertEquals(expectedUserValueName, filter.getTreeUserValueName());
		assertEquals(expectedDefaultFormat, filter.getDefaultFormat());
		if (filter instanceof NumericTreeFilterDefinition) {
			assertEquals(expectedBelowTheshold, ((NumericTreeFilterDefinition)filter).isBelowThreshold());
		}
		
		return (F)filter;
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
		
		assertEquals(3, parameters.getFilters().size());
		Iterator<TreeFilterDefinition> iterator = parameters.getFilters().iterator();
		
		NumericTreeFilterDefinition numericFilter = assertTreeFilter(NumericTreeFilterDefinition.Relative.class, "relativeFilter", "treeExp0", false, null, iterator.next());
		assertEquals(0.3, numericFilter.getThresholds().get(0).getThreshold(), 0.0000001);
		assertEquals("nexml", numericFilter.getThresholds().get(0).getFormat());
		assertEquals(0.5, numericFilter.getThresholds().get(1).getThreshold(), 0.0000001);
		assertEquals("nexml", numericFilter.getThresholds().get(1).getFormat());
		
		numericFilter = assertTreeFilter(NumericTreeFilterDefinition.Absolute.class, "absoluteFilter", "treeExp0", false, "nexml", iterator.next());
		assertEquals(2.0, numericFilter.getThresholds().get(0).getThreshold(), 0.0000001);
		assertNull(numericFilter.getThresholds().get(0).getFormat());
		assertEquals(4.0, numericFilter.getThresholds().get(1).getThreshold(), 0.0000001);
		assertEquals("newick", numericFilter.getThresholds().get(1).getFormat());
		
		assertTreeFilter(BooleanTreeFilterDefinition.class, "booleanFilter", "booleanExp", false, "nexml", iterator.next());
		
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
		
		NumericTreeFilterDefinition treeFilter = new NumericTreeFilterDefinition.Absolute("absoluteFilter", "treeExp0", false, "nexml");
		treeFilter.getThresholds().add(new TreeFilterThreshold(2.0, null));
		treeFilter.getThresholds().add(new TreeFilterThreshold(4.0, "newick"));
		parameters.getFilters().add(treeFilter);
		treeFilter = new NumericTreeFilterDefinition.Relative("relativeFilter", "treeExp0", false, null);
		treeFilter.getThresholds().add(new TreeFilterThreshold(3.0, "nexml"));
		treeFilter.getThresholds().add(new TreeFilterThreshold(5.2, "nexml"));
		parameters.getFilters().add(treeFilter);
		parameters.getFilters().add(new BooleanTreeFilterDefinition("booleanFilter", "booleanExp", "nexml"));
		
		Marshaller marshaller = JAXBContext.newInstance(AnalysisParameters.class).createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(parameters, System.out);
	}
}
