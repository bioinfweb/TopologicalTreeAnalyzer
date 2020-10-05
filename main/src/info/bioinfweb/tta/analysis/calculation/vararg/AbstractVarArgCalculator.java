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

import org.nfunk.jep.ParseException;



public abstract class AbstractVarArgCalculator<T> implements VarArgCalculator<T> {
	private int currentNumberOfElements = 0;
	
	
	protected int getCurrentNumberOfElements() {
		return currentNumberOfElements;
	}


	@Override
	public abstract String getName();


	protected abstract T calculatePair(T value1, T value2) throws ParseException;

	
	@Override
	public T calculate(Iterator<T> iterator) throws ParseException {
		currentNumberOfElements = 0;
		if (iterator.hasNext()) {
			currentNumberOfElements++;
			T result = iterator.next();
			while (iterator.hasNext()) {
				currentNumberOfElements++;
				result = calculatePair(result, iterator.next());
			}
			return result;
		}
		else {
			throw new ParseException("Invalid parameter count. At least one parameter needs to be specified.");
		}
	}
}
