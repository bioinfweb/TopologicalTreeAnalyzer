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


import java.sql.SQLException;
import java.util.Stack;

import org.nfunk.jep.ParseException;

import info.bioinfweb.tta.analysis.UserExpressionDataProvider;



public abstract class TreeDataFunction<T> extends AbstractFunction {
	public TreeDataFunction(UserExpressionDataProvider expressionData) {
		super(expressionData);
	}


	@Override
	public int getNumberOfParameters() {
		return getExpressionData().isTreeExpression() ? 0 : 1;
		//TODO Where can a meaningful exception message be thrown if the wrong (tree/pair) version/parameter count of the method is used?
		//     It could all be done if -1 is returned here. If no other way is found, this would be an option.
	}

	
	protected abstract T getValue(int index) throws SQLException;
	
	
	@Override
	public void doRun(Stack<Object> stack) throws ParseException, SQLException {
		int index = 0;
		if (!getExpressionData().isTreeExpression()) {
			Object sourceTree = stack.pop();
			if (sourceTree instanceof Number) {
				index = ((Number)sourceTree).intValue();
			}
			else {
				throw new ParseException("Invalid parameter type. This function must have one numeric parameter when used to calculate pair data.");
			}
		}
		stack.push(getValue(index));
	}
}