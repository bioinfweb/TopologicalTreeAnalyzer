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
