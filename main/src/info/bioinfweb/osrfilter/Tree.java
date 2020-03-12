package info.bioinfweb.osrfilter;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

import info.bioinfweb.treegraph.document.topologicalcalculation.LeafSet;



public class Tree {
	private File file;
	private String id;
	private Map<String, LeafSet> splits = new HashMap<String, LeafSet>();  //TODO Move LeafSet from TG to commons.core and call it something like PackedIntegerSet.
	
	
	public File getFile() {
		return file;
	}
	
	
	public void setFile(File file) {
		this.file = file;
	}
	
	
	public String getId() {
		return id;
	}
	
	
	public void setId(String id) {
		this.id = id;
	}
	
	
	public Map<String, LeafSet> getSplits() {
		return splits;
	}
}
