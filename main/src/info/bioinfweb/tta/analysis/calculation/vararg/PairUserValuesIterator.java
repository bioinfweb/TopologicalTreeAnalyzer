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
package info.bioinfweb.tta.analysis.calculation.vararg;


import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import org.nfunk.jep.ParseException;

import info.bioinfweb.tta.analysis.calculation.AbstractPairUserValueFunction;
import info.bioinfweb.tta.data.PairComparisonData;
import info.bioinfweb.tta.data.TreeIdentifier;
import info.bioinfweb.tta.data.TreePair;



public class PairUserValuesIterator implements Iterator<Object> {
	private CharSequence userValueName;
	private Map<TreePair, PairComparisonData> pairData;
	private TreeIdentifier currentTree;
	private Iterator<TreePair> pairDataIterator;
	private Object nextElement;
	
	
	public PairUserValuesIterator(CharSequence userValueName, Map<TreePair, PairComparisonData> pairData, TreeIdentifier currentTree) throws ParseException {
		super();
		this.userValueName = userValueName;
		this.pairData = pairData;
		this.currentTree = currentTree;
		
		pairDataIterator = pairData.keySet().iterator(); 
		ensureNextElement();
	}
	
	
	private void ensureNextElement() throws ParseException {
		nextElement = null;
		while (pairDataIterator.hasNext() && (nextElement == null)) {
			TreePair pair = pairDataIterator.next();
			if (currentTree.equals(pair.getTreeA()) || currentTree.equals(pair.getTreeB())) {
				nextElement = AbstractPairUserValueFunction.getUserValue(userValueName, pairData.get(pair).getUserValues());
			}
		}
	}


	@Override
	public boolean hasNext() {
		return (nextElement != null);
	}


	@Override
	public Object next() {
		if (hasNext()) {
			Object result = nextElement;
			try {
				ensureNextElement();
				return result;
			}
			catch (ParseException e) {
				throw new InternalError(e);  // If the user value with the specified name does not exist, it should be the case for all pairs. Then, an exception should have been thrown in the constructor already.
			}
		}
		else {
			throw new NoSuchElementException();
		}
	}
}
