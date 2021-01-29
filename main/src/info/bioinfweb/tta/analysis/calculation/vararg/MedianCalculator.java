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
package info.bioinfweb.tta.analysis.calculation.vararg;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.nfunk.jep.ParseException;



public class MedianCalculator implements VarArgCalculator<Double> {
	@Override
	public String getName() {
		return "median";
	}


	@Override
	public Double calculate(Iterator<Double> iterator) throws ParseException {
		List<Double> elements = new ArrayList<Double>();
		while (iterator.hasNext()) {
			elements.add(iterator.next());
		}
		if (!elements.isEmpty()) {
			Collections.sort(elements);
			return elements.get(elements.size() / 2);
		}
		else {
			throw new ParseException("Invalid parameter count. At least one parameter needs to be specified.");
		}
	}
}
