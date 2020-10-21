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


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.table.AbstractTableModel;



public abstract class AbstractTTATableModel<E extends Comparable<E>> extends AbstractTableModel {
	private List<E> order;
	
	
	public static IllegalArgumentException createInvalidColumnException(int columnIndex) {
		return new IllegalArgumentException("A column with the index " + columnIndex + " does not exist in this table model or is not editable.");
	}
	
	
	protected List<E> getOrder() {
		return order;
	}


	protected void refreshOrder(Collection<E> elements) {
		order = new ArrayList<E>(elements.size());
		order.addAll(elements);
		Collections.sort(order);
	}
}
