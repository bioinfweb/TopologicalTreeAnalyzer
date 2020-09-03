package info.bioinfweb.osrfilter.io.filter;


import java.util.HashSet;
import java.util.Set;

import info.bioinfweb.osrfilter.data.TreeIdentifier;



public class TreeFilterSet {
	private String fileName;
	private Set<TreeIdentifier> trees;
	
	
	public TreeFilterSet(String fileName) {
		super();
		this.fileName = fileName;
		trees = new HashSet<TreeIdentifier>();
	}


	public String getFileName() {
		return fileName;
	}


	public Set<TreeIdentifier> getTrees() {
		return trees;
	}
}
