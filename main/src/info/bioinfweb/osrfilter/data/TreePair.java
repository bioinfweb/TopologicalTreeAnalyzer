package info.bioinfweb.osrfilter.data;



public class TreePair {
	private TreeIdentifier treeA;
	private TreeIdentifier treeB;
	
	
	public TreePair(TreeIdentifier treeA, TreeIdentifier treeB) {
		super();
		this.treeA = treeA;
		this.treeB = treeB;
	}


	public TreeIdentifier getTreeA() {
		return treeA;
	}


	public TreeIdentifier getTreeB() {
		return treeB;
	}


	@Override
	public String toString() {
		return "[" + getTreeA() + ", " + getTreeB() + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((treeA == null) ? 0 : treeA.hashCode());
		result = prime * result + ((treeB == null) ? 0 : treeB.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TreePair other = (TreePair) obj;
		if (treeA == null) {
			if (other.treeA != null)
				return false;
		} else if (!treeA.equals(other.treeA))
			return false;
		if (treeB == null) {
			if (other.treeB != null)
				return false;
		} else if (!treeB.equals(other.treeB))
			return false;
		return true;
	}
}
