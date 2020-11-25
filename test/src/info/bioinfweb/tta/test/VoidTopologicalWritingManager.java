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
package info.bioinfweb.tta.test;


import java.io.IOException;

import info.bioinfweb.tta.data.AnalysesData;
import info.bioinfweb.tta.io.TopologicalDataWritingManager;



public class VoidTopologicalWritingManager extends TopologicalDataWritingManager {
	public VoidTopologicalWritingManager(AnalysesData data) {
		super(data, "", 0);
		unregister();
	}

	
	@Override
	public boolean writeNewData() throws IOException {
		return true;
	}
}
