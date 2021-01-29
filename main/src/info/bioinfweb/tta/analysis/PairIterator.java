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
package info.bioinfweb.tta.analysis;


import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import info.bioinfweb.tta.data.TreeIdentifier;
import info.bioinfweb.tta.data.TreeOrder;
import info.bioinfweb.tta.data.TreePair;



public class PairIterator implements Iterator<TreePair> {
	private TreeOrder trees;
	private TreeIdentifier referenceTree;
	private int pos1;
	private int pos2;
	
	
	public PairIterator(TreeOrder trees, TreeIdentifier referenceTree) {
		super();
		this.trees = trees;
		this.referenceTree = referenceTree;
		pos1 = 0;
		pos2 = 1;
	}


	public PairIterator(TreeOrder trees) {
		this(trees, null);
	}
	
	
	@Override
	public boolean hasNext() {
		if (referenceTree == null) {
			return (pos1 < trees.size() - 1);
		}
		else {
			return pos1 < trees.size();
		}
	}
	

	@Override
	public TreePair next() {
		TreePair result = null;
		if (referenceTree == null) {
			if (pos1 < trees.size() - 1) {
				result = new TreePair(trees.identifierByIndex(pos1), trees.identifierByIndex(pos2));
				pos2++;
			}
			if (pos2 >= trees.size()) {
				pos1++;
				pos2 = pos1 + 1;
			}
		}
		else if (pos1 < trees.size()) {
			TreeIdentifier identifier = trees.identifierByIndex(pos1);
			pos1++;
			if (identifier.equals(referenceTree)) {
				result = next();
			}
			else {
				result = new TreePair(referenceTree, identifier);
			}
		}
		
		if (result == null) {
			throw new NoSuchElementException();
		}
		else {
			return result;
		}
	}
}
