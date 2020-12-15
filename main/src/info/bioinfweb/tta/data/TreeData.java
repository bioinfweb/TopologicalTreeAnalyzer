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



public class TreeData {
	private TreeIdentifier tree;
	private int terminals;
	private int splits;
	
	
	public TreeData(TreeIdentifier tree, int terminals, int splits) {
		super();
		this.tree = tree;
		this.terminals = terminals;
		this.splits = splits;
	}


	public TreeData() {
		this(null, 0, 0);
	}


	public TreeIdentifier getTree() {
		return tree;
	}


	public void setTree(TreeIdentifier tree) {
		this.tree = tree;
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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + splits;
		result = prime * result + terminals;
		result = prime * result + ((tree == null) ? 0 : tree.hashCode());
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
		TreeData other = (TreeData) obj;
		if (splits != other.splits)
			return false;
		if (terminals != other.terminals)
			return false;
		if (tree == null) {
			if (other.tree != null)
				return false;
		} else if (!tree.equals(other.tree))
			return false;
		return true;
	}
}
