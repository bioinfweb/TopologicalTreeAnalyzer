package info.bioinfweb.tta.data.parameters;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import info.bioinfweb.commons.io.IOUtils;
import info.bioinfweb.treegraph.document.undo.CompareTextElementDataParameters;
import info.bioinfweb.tta.data.UserExpressions;
import info.bioinfweb.tta.data.parameters.ReferenceTreeDefinition.IDReferenceTreeDefinition;
import info.bioinfweb.tta.data.parameters.ReferenceTreeDefinition.IndexReferenceTreeDefinition;
import info.bioinfweb.tta.data.parameters.ReferenceTreeDefinition.NameReferenceTreeDefinition;
import info.bioinfweb.tta.data.parameters.filter.BooleanTreeFilterDefinition;
import info.bioinfweb.tta.data.parameters.filter.NumericTreeFilterDefinition;
import info.bioinfweb.tta.data.parameters.filter.TreeFilterDefinitionSet;
import info.bioinfweb.tta.io.parameters.UserExpressionsAdapter;



@XmlRootElement(name = "topologicalTreeAnalyzerParameters")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnalysisParameters {
	private CompareTextElementDataParameters textComparisonParameters = new CompareTextElementDataParameters();
	
	private int groupSize = 100;
	
	@XmlElementWrapper(name="treeFiles")
	@XmlElement(name="file")
	private List<String> treeFilesNames = new ArrayList<String>();
	
  @XmlElements({
    @XmlElement(name = "referenceTreeID", type = IDReferenceTreeDefinition.class),
    @XmlElement(name = "referenceTreeName", type = NameReferenceTreeDefinition.class),
    @XmlElement(name = "referenceTreeIndex", type = IndexReferenceTreeDefinition.class)
  })
	private ReferenceTreeDefinition referenceTree = null;  //TODO Can this be null by default?
  
	@XmlElement(name="userExpressions")
	@XmlJavaTypeAdapter(UserExpressionsAdapter.class)
	private UserExpressions userExpressions = new UserExpressions();

	private File outputDirectory = new File("");  //TODO Is this the current working directory?
	
	@XmlElementWrapper(name="filters")
  @XmlElements({
    @XmlElement(name = "booleanNumericFilter", type = BooleanTreeFilterDefinition.class),
    @XmlElement(name = "absoluteNumericFilter", type = NumericTreeFilterDefinition.Absolute.class),
    @XmlElement(name = "relativeNumericFilter", type = NumericTreeFilterDefinition.Relative.class)
  })
	private TreeFilterDefinitionSet filters = new TreeFilterDefinitionSet();
	
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


	public String[] getRelativizedTreeFilesNames(File baseDirectory) {
		String[] result = new String[treeFilesNames.size()];
		for (int i = 0; i < treeFilesNames.size(); i++) {
			result[i] = IOUtils.absoluteFilePath(new File(treeFilesNames.get(i)), baseDirectory);
		}
		return result;
	}


	public List<String> getTreeFilesNames() {
		return treeFilesNames;
	}


	public ReferenceTreeDefinition getReferenceTree() {
		return referenceTree;
	}


  public void setReferenceTree(ReferenceTreeDefinition referenceTree) {
		this.referenceTree = referenceTree;
	}


	public boolean definedReferenceTree() {
  	return referenceTree != null;
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


	public TreeFilterDefinitionSet getFilters() {
		return filters;
	}


	public ExportColumnList getTreeExportColumns() {
		return treeExportColumns;
	}


	public ExportColumnList getPairExportColumns() {
		return pairExportColumns;
	}
}
