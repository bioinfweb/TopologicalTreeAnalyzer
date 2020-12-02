/*
 * Topological Tree Analyzer - User defined topological analyses of large sets of phylogenetic trees. 
 * Copyright (C) 2020  Ben C. Stöver
 * <http://bioinfweb.info/TTA>
 * 
 * This file is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package info.bioinfweb.tta.data;



public class PairData {
	public static final int DATA_PROPERTY_COUNT = 6;
	
	
	private TreePair pair;
	private int matchingSplits;
	private int conflictingSplitsAB;
	private int notMatchingSplitsAB;
	private int conflictingSplitsBA;
	private int notMatchingSplitsBA;
	private int sharedTerminals;
	
	
	public PairData(TreePair pair, int matchingSplits, int conflictingSplitsAB, int notMatchingSplitsAB, int conflictingSplitsBA,
			int notMatchingSplitsBA, int sharedTerminals) {
		
		super();
		if (pair == null) {
			throw new IllegalArgumentException("pair must not be null.");
		}
		else {
			this.pair = pair;
			this.matchingSplits = matchingSplits;
			this.conflictingSplitsAB = conflictingSplitsAB;
			this.notMatchingSplitsAB = notMatchingSplitsAB;
			this.conflictingSplitsBA = conflictingSplitsBA;
			this.notMatchingSplitsBA = notMatchingSplitsBA;
			this.sharedTerminals = sharedTerminals;
		}
	}


	public PairData(TreePair pair) {
		this(pair, 0, 0, 0, 0, 0, 0);
	}


	public TreePair getPair() {
		return pair;
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


	public int getSharedTerminals() {
		return sharedTerminals;
	}


	public void setSharedTerminals(int sharedTerminals) {
		this.sharedTerminals = sharedTerminals;
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
		result = prime * result + ((pair == null) ? 0 : pair.hashCode());
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
		PairData other = (PairData) obj;
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
		if (pair == null) {
			if (other.pair != null)
				return false;
		} else if (!pair.equals(other.pair))
			return false;
		if (sharedTerminals != other.sharedTerminals)
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "[" + pair + ": matches=" + matchingSplits + ", conflictsAB=" + conflictingSplitsAB + ", conflictsBA=" + conflictingSplitsBA 
				+ ", notMatchingSplitsAB=" + notMatchingSplitsAB + ", notMatchingSplitsBA=" + notMatchingSplitsBA 
				+ ", sharedTerminals=" + sharedTerminals + "]";
	}
}
