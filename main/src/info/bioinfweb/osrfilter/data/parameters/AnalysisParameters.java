package info.bioinfweb.osrfilter.data.parameters;


import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import info.bioinfweb.osrfilter.data.UserExpressions;
import info.bioinfweb.osrfilter.io.parameters.UserExpressionsAdapter;
import info.bioinfweb.treegraph.document.undo.CompareTextElementDataParameters;



@XmlRootElement(name = "topologicalTreeAnalyzerParameters")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnalysisParameters {
	private AnalysisCompareTextElementDataParameters textComparisonParameters = new AnalysisCompareTextElementDataParameters();
	//private CompareTextElementDataParameters textComparisonParameters = new CompareTextElementDataParameters();
	
	private int groupSize = 100;
	
	@XmlElementWrapper(name="treeFiles")
	@XmlElement(name="file")
	private List<String> treeFilesNames = new ArrayList<String>();
	
	@XmlElement(name="userExpressions")
	@XmlJavaTypeAdapter(UserExpressionsAdapter.class)
	private UserExpressions userExpressions = new UserExpressions();

	@XmlElementWrapper(name="treeExportColumns")
	@XmlElement(name="column")
	private List<String> treeExportColumns = new ArrayList<String>();
	
	@XmlElementWrapper(name="pairExportColumns")
	@XmlElement(name="column")
	private List<String> pairExportColumns = new ArrayList<String>();
	
	
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


	public List<String> getTreeExportColumns() {
		return treeExportColumns;
	}


	public List<String> getPairExportColumns() {
		return pairExportColumns;
	}
}
