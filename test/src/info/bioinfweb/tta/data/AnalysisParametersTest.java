/*
 * Topological Tree Analyzer - User defined topological analyses of large sets of phylogenetic trees. 
 * Copyright (C) 2020-2021  Ben C. Stöver
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
package info.bioinfweb.tta.data;


import static org.junit.Assert.*;

import java.io.File;
import java.util.Iterator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Test;

import info.bioinfweb.commons.Math2;
import info.bioinfweb.jphyloio.formats.JPhyloIOFormatIDs;
import info.bioinfweb.tta.data.UserExpression;
import info.bioinfweb.tta.data.parameters.AnalysisParameters;
import info.bioinfweb.tta.data.parameters.ReferenceTreeDefinition;
import info.bioinfweb.tta.data.parameters.RuntimeParameters;
import info.bioinfweb.tta.data.parameters.filter.BooleanTreeFilterDefinition;
import info.bioinfweb.tta.data.parameters.filter.NumericTreeFilterDefinition;
import info.bioinfweb.tta.data.parameters.filter.TreeFilterDefinition;
import info.bioinfweb.tta.data.parameters.filter.TreeFilterThreshold;
import info.bioinfweb.tta.io.parameters.AnalysisParameterIO;



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
				
		assertEquals(RuntimeParameters.MAXIMUM, parameters.getRuntimeParameters().getThreads());
		assertEquals(8 * Math2.longPow(1024, 3), parameters.getRuntimeParameters().getMemory());
		
		assertEquals(2, parameters.getTreeFilesNames().size());
		assertEquals("data/Tree1.tre", parameters.getTreeFilesNames().get(0));
		assertEquals("data/Tree2.tre", parameters.getTreeFilesNames().get(1));
		
		assertTrue(parameters.getReferenceTree() instanceof ReferenceTreeDefinition.IndexReferenceTreeDefinition);
		ReferenceTreeDefinition.IndexReferenceTreeDefinition referenceTree = 
				(ReferenceTreeDefinition.IndexReferenceTreeDefinition)parameters.getReferenceTree();
		assertEquals("data/Tree1.tre", referenceTree.getFile());
		assertEquals(2, referenceTree.getIndex());
		
		assertEquals(5, parameters.getUserExpressions().getExpressions().size());
		assertEquals(5, parameters.getUserExpressions().getInputOrder().size());
		assertEquals("treeExp0", parameters.getUserExpressions().getCalculationOrder().get(0));
		assertEquals("treeExp1", parameters.getUserExpressions().getCalculationOrder().get(1));
		assertEquals("pairExp0", parameters.getUserExpressions().getCalculationOrder().get(2));
		assertEquals("pairExp1", parameters.getUserExpressions().getCalculationOrder().get(3));
		assertEquals("booleanExp", parameters.getUserExpressions().getCalculationOrder().get(4));
		assertUserExpression(true, "terminals()", parameters.getUserExpressions().getExpressions().get("treeExp0"));
		assertUserExpression(true, "2 * treeUserValue(\"treeExp0\")", parameters.getUserExpressions().getExpressions().get("treeExp1"));
		assertUserExpression(false, "c(0)", parameters.getUserExpressions().getExpressions().get("pairExp0"));
		assertUserExpression(false, "2 * pairUserValue(\"pairExp0\")", parameters.getUserExpressions().getExpressions().get("pairExp1"));
		assertUserExpression(true, "splits() > 2", parameters.getUserExpressions().getExpressions().get("booleanExp"));
		
		assertEquals(new File("data/parameters/output"), parameters.getOutputDirectory());
		
		assertEquals(3, parameters.getFilters().size());
		Iterator<TreeFilterDefinition> iterator = parameters.getFilters().iterator();
		
		NumericTreeFilterDefinition numericFilter = assertTreeFilter(NumericTreeFilterDefinition.Relative.class, "relativeFilter", "treeExp0", false, null, iterator.next());
		assertEquals(0.3, numericFilter.getThresholds().get(0).getThreshold(), 0.0000001);
		assertEquals(JPhyloIOFormatIDs.NEXML_FORMAT_ID, numericFilter.getThresholds().get(0).getFormat());
		assertEquals(0.5, numericFilter.getThresholds().get(1).getThreshold(), 0.0000001);
		assertEquals(JPhyloIOFormatIDs.NEXML_FORMAT_ID, numericFilter.getThresholds().get(1).getFormat());
		
		numericFilter = assertTreeFilter(NumericTreeFilterDefinition.Absolute.class, "absoluteFilter", "treeExp0", false, JPhyloIOFormatIDs.NEXML_FORMAT_ID, iterator.next());
		assertEquals(2.0, numericFilter.getThresholds().get(0).getThreshold(), 0.0000001);
		assertNull(numericFilter.getThresholds().get(0).getFormat());
		assertEquals(4.0, numericFilter.getThresholds().get(1).getThreshold(), 0.0000001);
		assertEquals(JPhyloIOFormatIDs.NEWICK_FORMAT_ID, numericFilter.getThresholds().get(1).getFormat());
		
		assertTreeFilter(BooleanTreeFilterDefinition.class, "booleanFilter", "booleanExp", false, JPhyloIOFormatIDs.NEXML_FORMAT_ID, iterator.next());
		
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
		
		parameters.getTreeFilesNames().add("data/Tree1.tre");
		parameters.getTreeFilesNames().add("data/Tree2.tre");
		
		parameters.setReferenceTree(new ReferenceTreeDefinition.IndexReferenceTreeDefinition("data/Tree1.tre", 2));
		
		parameters.getUserExpressions().getExpressions().put("treeExp0", new UserExpression(true, "terminals()"));
		parameters.getUserExpressions().getCalculationOrder().add("treeExp0");
		parameters.getUserExpressions().getExpressions().put("treeExp1", new UserExpression(true, "2 * treeUserValue(\"treeExp0\")"));
		parameters.getUserExpressions().getCalculationOrder().add("treeExp1");
		parameters.getUserExpressions().getExpressions().put("pairExp0", new UserExpression(false, "c(0)"));
		parameters.getUserExpressions().getCalculationOrder().add("pairExp0");
		parameters.getUserExpressions().getExpressions().put("pairExp1", new UserExpression(false, "2 * pairUserValue(\"treeExp0\")"));
		parameters.getUserExpressions().getCalculationOrder().add("pairExp1");
		
		parameters.setOutputDirectory(new File("data/parameters/output"));
		
		parameters.getTreeExportColumns().getColumns().add("treeExp1");
		parameters.getPairExportColumns().getColumns().add("pairExp0");
		parameters.getPairExportColumns().getColumns().add("pairExp1");
		
		NumericTreeFilterDefinition treeFilter = new NumericTreeFilterDefinition.Absolute("absoluteFilter", "treeExp0", false, JPhyloIOFormatIDs.NEXML_FORMAT_ID);
		treeFilter.getThresholds().add(new TreeFilterThreshold(2.0, null));
		treeFilter.getThresholds().add(new TreeFilterThreshold(4.0, JPhyloIOFormatIDs.NEWICK_FORMAT_ID));
		parameters.getFilters().add(treeFilter);
		treeFilter = new NumericTreeFilterDefinition.Relative("relativeFilter", "treeExp0", false, null);
		treeFilter.getThresholds().add(new TreeFilterThreshold(3.0, JPhyloIOFormatIDs.NEXML_FORMAT_ID));
		treeFilter.getThresholds().add(new TreeFilterThreshold(5.2, JPhyloIOFormatIDs.NEXML_FORMAT_ID));
		parameters.getFilters().add(treeFilter);
		parameters.getFilters().add(new BooleanTreeFilterDefinition("booleanFilter", "booleanExp", JPhyloIOFormatIDs.NEXML_FORMAT_ID));
		
		Marshaller marshaller = JAXBContext.newInstance(AnalysisParameters.class).createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(parameters, System.out);
	}
}
