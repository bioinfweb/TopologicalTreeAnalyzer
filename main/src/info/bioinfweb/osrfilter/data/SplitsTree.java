package info.bioinfweb.osrfilter.data;


import java.util.HashSet;
import java.util.Set;

import info.bioinfweb.treegraph.document.topologicalcalculation.LeafSet;



public class SplitsTree {
	private TreeIdentifier treeIdentifier;
	private LeafSet terminalSet;
	private Set<LeafSet> splits = new HashSet<LeafSet>();  //TODO Consider moving LeafSet from TG to commons.core and call it something like PackedIntegerSet. Consider using a value object that also stores ID and maybe more information on a node or branch.
	
	
	public TreeIdentifier getTreeIdentifier() {
		return treeIdentifier;
	}


	public void setTreeIdentifier(TreeIdentifier treeIdentifier) {
		this.treeIdentifier = treeIdentifier;
	}


	public LeafSet getTerminalSet() {
		return terminalSet;
	}


	public void setTerminalSet(LeafSet terminalSet) {
		this.terminalSet = terminalSet;
	}


	public Set<LeafSet> getSplits() {
		return splits;
	}
}
