package info.bioinfweb.osrfilter.data.parameters;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import info.bioinfweb.osrfilter.data.UserExpressions;
import info.bioinfweb.osrfilter.data.parameters.filter.BooleanTreeFilter;
import info.bioinfweb.osrfilter.data.parameters.filter.NumericTreeFilter;
import info.bioinfweb.osrfilter.data.parameters.filter.TreeFilterSet;
import info.bioinfweb.osrfilter.io.parameters.UserExpressionsAdapter;
import info.bioinfweb.treegraph.document.undo.CompareTextElementDataParameters;



@XmlRootElement(name = "topologicalTreeAnalyzerParameters")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnalysisParameters {
	private CompareTextElementDataParameters textComparisonParameters = new CompareTextElementDataParameters();
	
	private int groupSize = 100;
	
	@XmlElementWrapper(name="treeFiles")
	@XmlElement(name="file")
	private List<String> treeFilesNames = new ArrayList<String>();
	
	@XmlElement(name="userExpressions")
	@XmlJavaTypeAdapter(UserExpressionsAdapter.class)
	private UserExpressions userExpressions = new UserExpressions();

	private File outputDirectory = new File("");  //TODO Is this the current working directory?
	
	@XmlElementWrapper(name="filters")
  @XmlElements({
    @XmlElement(name = "booleanNumericFilter", type = BooleanTreeFilter.class),
    @XmlElement(name = "absoluteNumericFilter", type = NumericTreeFilter.Absolute.class),
    @XmlElement(name = "relativeNumericFilter", type = NumericTreeFilter.Relative.class)
  })
	private TreeFilterSet filters = new TreeFilterSet();
	
	@XmlElement(name="treeExportColumns")
	private ExportColumnList treeExportColumns = new ExportColumnList();
	
	@XmlElement(name="pairExportColumns")
	private ExportColumnList pairExportColumns = new ExportColumnList();
	
	
	public CompareTextElementDataParameters getTextComparisonParameters() {
		return textComparisonParameters;
	}


	public int getGroupSize() {
		return groupSize;
	}


	public void setGroupSize(int groupSize) {
		this.groupSize = groupSize;
	}


	public List<String> getTreeFilesNames() {
		return treeFilesNames;
	}


	public UserExpressions getUserExpressions() {
		return userExpressions;
	}


	public File getOutputDirectory() {
		return outputDirectory;
	}


	public void setOutputDirectory(File outputDirectory) {
		this.outputDirectory = outputDirectory;
	}


	public TreeFilterSet getFilters() {
		return filters;
	}


	public ExportColumnList getTreeExportColumns() {
		return treeExportColumns;
	}


	public ExportColumnList getPairExportColumns() {
		return pairExportColumns;
	}
}
