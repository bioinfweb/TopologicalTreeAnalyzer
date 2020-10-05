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
import java.util.NoSuchElementException;
import java.util.Stack;



public class VarArgStackIterator<T> implements Iterator<T> {
	private Stack<Object> stack;
	private int numberOfElements;
	private int returnedElements;
	
	
	public VarArgStackIterator(Stack<Object> stack, int numberOfElements) {
		super();
		this.stack = stack;
		this.numberOfElements = numberOfElements;
		returnedElements = 0;
	}


	@Override
	public boolean hasNext() {
		return (numberOfElements > returnedElements);
	}


	@SuppressWarnings("unchecked")
	@Override
	public T next() {
		if (hasNext()) {
			returnedElements++;
			return (T)stack.pop();
		}
		else {
			throw new NoSuchElementException();
		}
	}
}
