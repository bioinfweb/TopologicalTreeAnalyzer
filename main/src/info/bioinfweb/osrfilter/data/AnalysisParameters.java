package info.bioinfweb.osrfilter.data;


import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import info.bioinfweb.osrfilter.io.parameters.UserExpressionsAdapter;
import info.bioinfweb.treegraph.document.undo.CompareTextElementDataParameters;



@XmlRootElement(name = "topologicalTreeAnalyzerParameters")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnalysisParameters {
	private CompareTextElementDataParameters textComparisonParameters;
	
	private int groupSize;
	
	@XmlElementWrapper(name="treeFiles")
	@XmlElement(name="file")
	private List<String> treeFilesNames = new ArrayList<String>();
	
	@XmlElement(name="userExpressions")
	@XmlJavaTypeAdapter(UserExpressionsAdapter.class)
	private UserExpressions userExpressions = new UserExpressions();

	@XmlElementWrapper(name="exportColumns")
	@XmlElement(name="column")
	private List<String> exportColumns = new ArrayList<String>();
	
	
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


	public List<String> getExportColumns() {
		return exportColumns;
	}
}
