package info.bioinfweb.tta.io.filter;


import java.util.HashSet;
import java.util.Set;

import info.bioinfweb.tta.data.TreeIdentifier;



public class TreeFilterSet {
	private String fileName;
	private String format;
	private Set<TreeIdentifier> trees;
	
	
	public TreeFilterSet(String fileName, String format) {
		super();
		this.fileName = fileName;
		this.format = format;
		trees = new HashSet<TreeIdentifier>();
	}


	public String getFileName() {
		return fileName;
	}


	public String getFormat() {
		return format;
	}


	public Set<TreeIdentifier> getTrees() {
		return trees;
	}
}
