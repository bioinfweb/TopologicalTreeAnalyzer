package info.bioinfweb.osrfilter.data;



public class PairComparison {
	private TreeIdentifier[] treeIdentifiers = new TreeIdentifier[2];  //TODO Add getters and setters if this will done this way.
	private int matchingSplits;
	private int conflictingSplits;
	private int sharedTerminals;
	
	
	public PairComparison(int matchingSplits, int conflictingSplits, int sharedTerminals) {
		super();
		this.matchingSplits = matchingSplits;
		this.conflictingSplits = conflictingSplits;
		this.sharedTerminals = sharedTerminals;
	}


	public int getMatchingSplits() {
		return matchingSplits;
	}


	public int getConflictingSplits() {
		return conflictingSplits;
	}


	public int getSharedTerminals() {
		return sharedTerminals;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + conflictingSplits;
		result = prime * result + matchingSplits;
		result = prime * result + sharedTerminals;
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
		PairComparison other = (PairComparison) obj;
		if (conflictingSplits != other.conflictingSplits)
			return false;
		if (matchingSplits != other.matchingSplits)
			return false;
		if (sharedTerminals != other.sharedTerminals)
			return false;
		return true;
	}
}
