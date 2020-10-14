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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import info.bioinfweb.tta.data.UserExpression;
import info.bioinfweb.tta.data.UserExpressions;



/**
 * An implementation of {@link TableModel} that delegates to {@link UserExpressions#getExpressions()}.
 * 
 * @author Ben St&ouml;ver
 * @since 0.3.0
 */
public class UserExpressionsTableModel extends AbstractTableModel {
	public static enum ExpressionTarget {
		TREE, PAIR;
	}
	
	
	private Map<String, UserExpression> expressions;
	private List<String> nameOrder;
	
	
	public UserExpressionsTableModel(Map<String, UserExpression> expressions) {  //TODO Will it be necessary to set the expressions property during runtime (and refresh table respectively)?
		super();
		setExpressions(expressions);
	}


	public Map<String, UserExpression> getExpressions() {
		return expressions;
	}


	/**
	 * Sorts the expression names alphabetically.
	 */
	private void refreshNameOrder( ) {
		nameOrder = new ArrayList<String>(expressions.size());
		nameOrder.addAll(expressions.keySet());
		Collections.sort(nameOrder);

		fireTableDataChanged();
		//TODO Is it a problem when the line order changes right after the user edited this line? (Maybe move the cursor to the respective new position depending on its previous one.)  
	}
	
	
	public void setExpressions(Map<String, UserExpression> expressions) {
		if (expressions != null) {
			this.expressions = expressions;
			refreshNameOrder();  // Also fires table change.
		}
		else {
			throw new IllegalArgumentException("expressions must not be null");
		}
	}


	private IllegalArgumentException createInvalidColumnException(int columnIndex) {
		return new IllegalArgumentException("A column with the index " + columnIndex + " does not exist in this table model.");
	}
	
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
			case 0:
			case 2:
				return String.class;
			case 1:
				return ExpressionTarget.class;
			default:
				throw createInvalidColumnException(columnIndex);
		}
	}

	
	@Override
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
			case 0:
				return "Name";
			case 1:
				return "Target";
			case 2:
				return "Expression";
			default:
				throw createInvalidColumnException(columnIndex);
		}
	}

	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}

	
	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		String name = nameOrder.get(rowIndex);
		UserExpression expression = expressions.get(name);
		switch (columnIndex) {
			case 0:
				expressions.remove(name);
				expressions.put(value.toString(), expression);
				refreshNameOrder();  // Also fires table change.
				break;
			case 1:
				expression.setTreeTarget(ExpressionTarget.TREE.equals((ExpressionTarget)value));
				fireTableCellUpdated(rowIndex, columnIndex);
				break;
			case 2:
				expression.setExpression(value.toString());
				fireTableCellUpdated(rowIndex, columnIndex);
				break;
			default:
				throw createInvalidColumnException(columnIndex);
		}
	}

	
	@Override
	public int getColumnCount() {
		return 3;
	}

	
	@Override
	public int getRowCount() {
		return expressions.size();
	}

	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String name = nameOrder.get(rowIndex);
		switch (columnIndex) {
			case 0:
				return name;
			case 1:
				if (expressions.get(name).hasTreeTarget()) {
					return ExpressionTarget.TREE;
				}
				else {
					return ExpressionTarget.PAIR;
				}
			case 2:
				return expressions.get(name).getExpression();
			default:
				throw createInvalidColumnException(columnIndex);
		}
	}
}
