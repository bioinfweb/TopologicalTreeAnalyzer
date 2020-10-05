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


import info.bioinfweb.tta.analysis.UserExpressionDataProvider;



public class CFunction extends TreeDependentPairDataFunction<Double> {
	public CFunction(UserExpressionDataProvider expressionData) {
		super(expressionData);
	}


	@Override
	public String getName() {
		return "c";
	}


	@Override
	protected Double getValueAB() {
		return new Double(getExpressionData().getCurrentComparisonData().getConflictingSplitsAB());
	}


	@Override
	protected Double getValueBA() {
		return new Double(getExpressionData().getCurrentComparisonData().getConflictingSplitsBA());
	}
}
