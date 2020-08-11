package info.bioinfweb.osrfilter.data;


import java.util.HashMap;
import java.util.Map;



/**
 * Represents the comparison raw data of a pairwise topological tree comparison.
 * 
 * @author Ben St&ouml;ver
 */
public class PairComparison {
	//TODO Trees should probably be referenced here unless the map structure will reflect which tree is which is the future.
	private int matchingSplits;
	private int conflictingSplitsAB;
	private int notMatchingSplitsAB;
	private int conflictingSplitsBA;
	private int notMatchingSplitsBA;
	private int terminalsA;
	private int terminalsB;
	private int sharedTerminals;
	private int splitsA;
	private int splitsB;
	private Map<String, Object> userValues = new HashMap<String, Object>();
	
	
	public PairComparison(int matchingSplits, int conflictingSplitsForward, int notMatchingSplitsForward,
			int conflictingSplitsReverse, int notMatchingSplitsReverse, int sharedTerminals) {
		
		super();
		this.matchingSplits = matchingSplits;
		this.conflictingSplitsAB = conflictingSplitsForward;
		this.notMatchingSplitsAB = notMatchingSplitsForward;
		this.conflictingSplitsBA = conflictingSplitsReverse;
		this.notMatchingSplitsBA = notMatchingSplitsReverse;
		this.sharedTerminals = sharedTerminals;
	}


	public PairComparison() {
		this(0, 0, 0, 0, 0, 0);
	}


	/**
	 * Returns the number of matching splits between the two compared trees.
	 * <p>
	 * Note that this value is identical in both directions and is not multiplied by two to reflect both direction. It reflects 
	 * the number of split pairs (not the number of splits) that correspond between the two trees.
	 * 
	 * @return the number of matching splits
	 */
	public int getMatchingSplits() {
		return matchingSplits;
	}

	
	public void setMatchingSplits(int matchingSplits) {
		this.matchingSplits = matchingSplits;
	}

	
	public int getConflictingSplitsAB() {
		return conflictingSplitsAB;
	}


	public void setConflictingSplitsAB(int conflictingSplitsForward) {
		this.conflictingSplitsAB = conflictingSplitsForward;
	}


	public int getNotMatchingSplitsAB() {
		return notMatchingSplitsAB;
	}


	public void setNotMatchingSplitsAB(int notMatchingSplitsForward) {
		this.notMatchingSplitsAB = notMatchingSplitsForward;
	}


	public int getConflictingSplitsBA() {
		return conflictingSplitsBA;
	}


	public void setConflictingSplitsBA(int conflictingSplitsReverse) {
		this.conflictingSplitsBA = conflictingSplitsReverse;
	}


	public int getNotMatchingSplitsBA() {
		return notMatchingSplitsBA;
	}


	public void setNotMatchingSplitsBA(int notMatchingSplitsReverse) {
		this.notMatchingSplitsBA = notMatchingSplitsReverse;
	}


	public int getTerminalsA() {
		return terminalsA;
	}


	public void setTerminalsA(int terminalsA) {
		this.terminalsA = terminalsA;
	}


	public int getTerminalsB() {
		return terminalsB;
	}


	public void setTerminalsB(int terminalsB) {
		this.terminalsB = terminalsB;
	}


	public int getSharedTerminals() {
		return sharedTerminals;
	}


	public void setSharedTerminals(int sharedTerminals) {
		this.sharedTerminals = sharedTerminals;
	}


	public int getSplitsA() {
		return splitsA;
	}


	public void setSplitsA(int splitsA) {
		this.splitsA = splitsA;
	}


	public int getSplitsB() {
		return splitsB;
	}


	public void setSplitsB(int splitsB) {
		this.splitsB = splitsB;
	}


	public Map<String, Object> getUserValues() {
		return userValues;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + conflictingSplitsAB;
		result = prime * result + conflictingSplitsBA;
		result = prime * result + matchingSplits;
		result = prime * result + notMatchingSplitsAB;
		result = prime * result + notMatchingSplitsBA;
		result = prime * result + sharedTerminals;
		result = prime * result + splitsA;
		result = prime * result + splitsB;
		result = prime * result + terminalsA;
		result = prime * result + terminalsB;
		result = prime * result + ((userValues == null) ? 0 : userValues.hashCode());
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
		if (conflictingSplitsAB != other.conflictingSplitsAB)
			return false;
		if (conflictingSplitsBA != other.conflictingSplitsBA)
			return false;
		if (matchingSplits != other.matchingSplits)
			return false;
		if (notMatchingSplitsAB != other.notMatchingSplitsAB)
			return false;
		if (notMatchingSplitsBA != other.notMatchingSplitsBA)
			return false;
		if (sharedTerminals != other.sharedTerminals)
			return false;
		if (splitsA != other.splitsA)
			return false;
		if (splitsB != other.splitsB)
			return false;
		if (terminalsA != other.terminalsA)
			return false;
		if (terminalsB != other.terminalsB)
			return false;
		if (userValues == null) {
			if (other.userValues != null)
				return false;
		} else if (!userValues.equals(other.userValues))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "[matches=" + matchingSplits + ", conflictsAB=" + conflictingSplitsAB + ", conflictsBA=" + conflictingSplitsBA 
				+ ", notMatchingSplitsAB=" + notMatchingSplitsAB + ", notMatchingSplitsBA=" + notMatchingSplitsBA 
				+ ", terminalsA=" + terminalsA + ", terminalsB=" + terminalsB + ", sharedTerminals=" + sharedTerminals 
				+ ", splitsA=" + splitsA + ", splitsB=" + splitsB + "]";
	}
}
