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


import java.util.Stack;

import org.nfunk.jep.ParseException;

import info.bioinfweb.tta.analysis.UserExpressionDataProvider;
import info.bioinfweb.tta.analysis.calculation.AbstractFunction;



public class VarArgFunction<T> extends AbstractFunction {
	private VarArgCalculator<T> calculator;

	
	public VarArgFunction(UserExpressionDataProvider expressionData, VarArgCalculator<T> calculator) {
		super(expressionData);
		this.calculator = calculator;
	}


	@Override
	public String getName() {
		return calculator.getName();
	}


	@Override
	public int getNumberOfParameters() {
		return -1;
	}


	@Override
	public boolean checkNumberOfParameters(int numberOfParameters) {
		return (numberOfParameters > 0);
	}


	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public void run(Stack stack) throws ParseException {
		stack.push(calculator.calculate(new VarArgStackIterator<T>(stack, curNumberOfParameters)));
	}
}
