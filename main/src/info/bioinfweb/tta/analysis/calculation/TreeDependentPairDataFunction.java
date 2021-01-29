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
package info.bioinfweb.tta.analysis.calculation;


import java.util.Stack;

import org.nfunk.jep.ParseException;

import info.bioinfweb.tta.analysis.UserExpressionDataProvider;



public abstract class TreeDependentPairDataFunction<T> extends AbstractFunction {
	public TreeDependentPairDataFunction(UserExpressionDataProvider expressionData) {
		super(expressionData);
	}


	@Override
	public int getNumberOfParameters() {
		return 1;
	}

	
	protected abstract T getValueAB();
	
	
	protected abstract T getValueBA();
	
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void run(Stack stack) throws ParseException {
		Object sourceTree = stack.pop();
		
		if (sourceTree instanceof Number) {
			T result;
			if (((Number)sourceTree).intValue() == 0) {
				result = getValueAB();
			}
			else {
				result = getValueBA();
			}
			stack.push(result);
		}
		else {
			throw new ParseException("Invalid parameter type. This function must have one numeric parameter.");
		}
	}
}
