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


import org.nfunk.jep.ParseException;

import info.bioinfweb.tta.analysis.UserExpressionDataProvider;



public class PairUserValueFunction extends AbstractPairUserValueFunction implements UserValueFunction {
	public PairUserValueFunction(UserExpressionDataProvider expressionData) {
		super(expressionData);
	}

	
	@Override
	public String getName() {
		return "pairUserValue";
	}

	
	@Override
	protected Object calculateValue(CharSequence userValueName) throws ParseException {
		if (!getExpressionData().isTreeExpression()) {
			Object result = getExpressionData().getCurrentPairUserData().getUserValue(userValueName.toString());
			
			if (result == null) {
				throw new ParseException("Invalid parameter. No pair user value \"" + userValueName + 
						"\" could be found. It is either undefined or a tree user value.");
			}
			else {
				return result;
			}
		}
		else {
			throw new ParseException("Access to current pair user values is only possible in pair expressions. (You can use treeUserValue() in tree expressions.)");
		}
	}
}
