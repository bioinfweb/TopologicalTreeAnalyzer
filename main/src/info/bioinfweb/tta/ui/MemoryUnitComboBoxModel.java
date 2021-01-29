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
package info.bioinfweb.tta.ui;


import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import info.bioinfweb.commons.Math2;



public class MemoryUnitComboBoxModel extends AbstractListModel<String> implements ComboBoxModel<String> {
	private static final String[] PREFIXES = {"B", "KiB", "MiB", "GiB", "TiB", "PiB"};
	
	
	private int selectedIndex = 0;
	
	
	@Override
	public String getElementAt(int index) {
		return PREFIXES[index];
	}

	
	@Override
	public int getSize() {
		return 6;
	}

	
	@Override
	public Object getSelectedItem() {
		return getElementAt(selectedIndex);
	}

	
	@Override
	public void setSelectedItem(Object anItem) {
		String text = anItem.toString();
		for (int i = 0; i < PREFIXES.length; i++) {
			if (PREFIXES[i].contentEquals(text)) {
				selectedIndex = i;
			}
		}
		// Selection will not be changed of the input is not in the list, i.e., invalid.
	}
	
	
	public long multiplyValue(long value) {
		return Math2.longPow(1024, selectedIndex) * value;
	}
}
