/*
 * Topological Tree Analyzer - User defined topological analyses of large sets of phylogenetic trees. 
 * Copyright (C) 2020-2021  Ben C. Stöver
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



public class TTATree<T> {
	private TreeIdentifier treeIdentifier;
	private T tree;

	
	public TTATree(TreeIdentifier treeIdentifier, T tree) {
		super();
		if (treeIdentifier == null) {
			throw new IllegalArgumentException("treeIdentifier must not be null.");
		}
		else {
			this.treeIdentifier = treeIdentifier;
			this.tree = tree;
		}
	}


	public TreeIdentifier getTreeIdentifier() {
		return treeIdentifier;
	}


	public T getTree() {
		return tree;
	}
}
