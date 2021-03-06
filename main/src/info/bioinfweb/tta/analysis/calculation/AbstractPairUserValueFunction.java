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


import java.util.Map;
import java.util.Stack;

import org.nfunk.jep.ParseException;

import info.bioinfweb.tta.analysis.UserExpressionDataProvider;



public abstract class AbstractPairUserValueFunction extends AbstractFunction implements UserValueFunction {
	public AbstractPairUserValueFunction(UserExpressionDataProvider expressionData) {
		super(expressionData);
	}

	
	@Override
	public int getNumberOfParameters() {
		return 1;
	}

	
	public static Object getUserValue(CharSequence userValueName, Map<String, Object> map) throws ParseException {
		if (map.containsKey(userValueName)) {
			return map.get(userValueName);
		}
		else {
			throw new ParseException("Invalid parameter. No pair user value \"" + userValueName + 
					"\" could be found. It is either undefined or a tree user value.");
		}
	}
	

	protected abstract Object calculateValue(CharSequence userValueName) throws ParseException;


	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void run(Stack stack) throws ParseException {
		Object name = stack.pop();
		if (name instanceof CharSequence) {
			stack.push(calculateValue((CharSequence)name));
		}
		else {
			throw new ParseException("Invalid parameter type. This function must have one textual parameter defining the referenced user expression.");
		}
	}
}
