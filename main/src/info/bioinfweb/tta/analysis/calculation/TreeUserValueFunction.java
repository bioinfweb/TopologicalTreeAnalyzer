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
package info.bioinfweb.tta.analysis.calculation;


import java.util.Map;
import java.util.Stack;

import org.nfunk.jep.ParseException;

import info.bioinfweb.tta.analysis.UserExpressionDataProvider;



public class TreeUserValueFunction extends AbstractFunction implements UserValueFunction {
	public TreeUserValueFunction(UserExpressionDataProvider expressionData) {
		super(expressionData);
	}


	@Override
	public String getName() {
		return "treeUserValue";
	}


	@Override
	public int getNumberOfParameters() {
		return getExpressionData().isTreeExpression() ? 1 : 2;
	}


	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void run(Stack stack) throws ParseException {
		// Get index:
		int index = 0;
		if (!getExpressionData().isTreeExpression()) {
			Object sourceTree = stack.pop();  // Note that the last parameter is taken from the stack first.
			if (sourceTree instanceof Number) {
				index = ((Number)sourceTree).intValue();
			}
			else {
				throw new ParseException("Invalid parameter type. This function must have one numeric parameter when used to calculate pair data.");
			}
		}
		
		// Return user value:
		Object name = stack.pop();
		if (name instanceof CharSequence) {
			Map<String, Object> map = getExpressionData().getCurrentTreeUserData(index);
			if (map.containsKey(name)) {
				stack.push(map.get(name));
			}
			else {
				throw new ParseException("Invalid parameter for " + getName() + "(). No tree user value \"" + name + 
						"\" could be found. It is either undefined or a pair user value.");
			}
		}
		else {
			if (getExpressionData().isTreeExpression()) {
				throw new ParseException("Invalid parameter type for " + getName()
						+	"(). This function must have one textual parameter defining the referenced user expression.");
			}
			else {
				throw new ParseException("Invalid parameter type for " + getName()
						+ "(). The first parameter of this function must be textual and define the referenced user expression.");
			}
		}
	}
}
