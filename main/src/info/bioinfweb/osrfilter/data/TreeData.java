package info.bioinfweb.osrfilter.data;


import java.util.HashMap;
import java.util.Map;



public class TreeData {
	private int terminals;
	private int splits;
	private Map<String, Object> userValues = new HashMap<String, Object>();
	
	
	public TreeData(int terminals, int splits) {
		super();
		this.terminals = terminals;
		this.splits = splits;
	}


	public TreeData() {
		this(0, 0);
	}


	public int getTerminals() {
		return terminals;
	}


	public void setTerminals(int terminals) {
		this.terminals = terminals;
	}


	public int getSplits() {
		return splits;
	}


	public void setSplits(int splits) {
		this.splits = splits;
	}


	public Map<String, Object> getUserValues() {
		return userValues;
	}
}
