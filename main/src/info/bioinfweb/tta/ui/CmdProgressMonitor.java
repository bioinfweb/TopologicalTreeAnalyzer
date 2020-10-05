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
package info.bioinfweb.tta.ui;


import info.bioinfweb.commons.progress.AbstractProgressMonitor;



public class CmdProgressMonitor extends AbstractProgressMonitor {
	public static final int ELEMENT_COUNT = 80; 
	public static final char ELEMENT_CHAR = '='; 
	

	private int writtenElements = 0;
	
	
	@Override
	public boolean isCanceled() {
		return false;
	}
	

	@Override
	protected void onProgress(double value, String text) {
		int elements = (int)Math.round(value * ELEMENT_COUNT);
		for (int i = writtenElements; i < elements; i++) {
			System.out.print(ELEMENT_CHAR);
		}
		writtenElements = elements;
	}
}
