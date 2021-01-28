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


import org.nfunk.jep.ParseException;

import info.bioinfweb.tta.analysis.UserExpressionDataProvider;
import info.bioinfweb.tta.analysis.calculation.AbstractPairUserValueFunction;



@SuppressWarnings("rawtypes")
public class IteratingPairUserValueFunction extends AbstractPairUserValueFunction {
	private VarArgCalculator calculator;
	
	
	public IteratingPairUserValueFunction(UserExpressionDataProvider expressionData, VarArgCalculator calculator) {
		super(expressionData);
		if (calculator != null) {
			this.calculator = calculator;
		}
		else {
			throw new IllegalArgumentException("calculator must not be null.");
		}
	}

	
	@Override
	public String getName() {
		return calculator.getName() + "OfPairUserValues";
	}

	
	@SuppressWarnings("unchecked")
	@Override
	protected Object calculateValue(CharSequence userValueName) throws ParseException {
		if (getExpressionData().isTreeExpression()) {
			return calculator.calculate(getExpressionData().getPairUserValues(userValueName, getExpressionData().getCurrentTreeData(0).getTree()));
		}
		else {
			throw new ParseException("Iterating over all pair user values is only possible in tree expressions.");
		}
	}
}
