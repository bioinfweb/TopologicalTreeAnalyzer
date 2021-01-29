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
package info.bioinfweb.tta.io.data;


import java.io.File;
import java.io.IOException;

import info.bioinfweb.tta.data.PairData;
import info.bioinfweb.tta.data.TreePair;



public class PairDataIterator extends DataIterator<PairData> {
	public PairDataIterator(File file) throws IOException {
		super(file, PairData.DATA_PROPERTY_COUNT + 2);  //TODO Determine if file should contain references to the trees in the future.
	}
	
	
	@Override
	protected PairData parseElement(String[] values) {
		return new PairData(new TreePair(null, null), Integer.parseInt(values[2]), Integer.parseInt(values[3]), Integer.parseInt(values[4]), 
				Integer.parseInt(values[5]), Integer.parseInt(values[6]), Integer.parseInt(values[7]));  //TODO Adjust indices or read TreePair data, as well.
	}
}
