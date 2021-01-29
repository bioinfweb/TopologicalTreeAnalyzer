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


import info.bioinfweb.tta.data.parameters.filter.BooleanTreeFilterDefinition;
import info.bioinfweb.tta.data.parameters.filter.NumericTreeFilterDefinition;
import info.bioinfweb.tta.data.parameters.filter.TreeFilterDefinition;
import info.bioinfweb.tta.data.parameters.filter.TreeFilterDefinitionSet;



public class TreeFilterTableModel extends AbstractTTATableModel<TreeFilterDefinition> {
	public static final int MAXIMUM_PROPERTY_COUNT = 6;
	public static final int NAME_COLUMN_INDEX = 0;
	public static final int TYPE_COLUMN_INDEX = 1;
	public static final int USER_EXPRESSION_COLUMN_INDEX = 2;
	public static final int DEFAULT_FORMAT_COLUMN_INDEX = 3;
	public static final int THRESHOLD_BELOW_COLUMN_INDEX = 4;
	public static final int THRESHOLDS_COLUMN_INDEX = 5;
	
	
	
	private TreeFilterDefinitionSet filters;

	
	public TreeFilterTableModel(TreeFilterDefinitionSet filters) {
		super();
		this.filters = filters;
	}


	public TreeFilterDefinitionSet getFilters() {
		return filters;
	}


	public void setFilters(TreeFilterDefinitionSet filters) {
		if (filters != null) {
			this.filters = filters;
			refreshFromSet();
		}
		else {
			throw new IllegalArgumentException("filters must not be null.");
		}
	}


	protected void refreshFromSet() {
		refreshOrder(filters);
		fireTableDataChanged();
	}


	@Override
	public int getColumnCount() {
		return MAXIMUM_PROPERTY_COUNT;
	}
	

	@Override
	public int getRowCount() {
		return filters.size();
	}

	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex < MAXIMUM_PROPERTY_COUNT) {
			if (columnIndex == THRESHOLD_BELOW_COLUMN_INDEX) {
				return Boolean.class;
			}
			else {
				return String.class;
			}
		}
		else {
			throw createInvalidColumnException(columnIndex);
		}
	}


	@Override
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
			case NAME_COLUMN_INDEX:
				return "Name";
				
			case TYPE_COLUMN_INDEX:
				return "Type";
				
			case USER_EXPRESSION_COLUMN_INDEX:
				return "User Expression";
				
			case DEFAULT_FORMAT_COLUMN_INDEX:
				return "Default Format";
				
			case THRESHOLD_BELOW_COLUMN_INDEX:
				return "Below Threshold";
				
			case THRESHOLDS_COLUMN_INDEX:
				return "Thresholds";
				
			default:
				throw createInvalidColumnException(columnIndex);
		}
	}


	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		switch (columnIndex) {
			case TYPE_COLUMN_INDEX:
			case THRESHOLDS_COLUMN_INDEX:
				return false;
				
			case THRESHOLD_BELOW_COLUMN_INDEX:
				return !(getOrder().get(rowIndex) instanceof BooleanTreeFilterDefinition);
				
			default:
				return true;
		}
	}


	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		TreeFilterDefinition definition = getOrder().get(rowIndex);
		switch (columnIndex) {
			case NAME_COLUMN_INDEX:
				return definition.getName();
				
			case TYPE_COLUMN_INDEX:
				return definition.getClass().getName();  //TODO Use a more user-friendly name
				
			case USER_EXPRESSION_COLUMN_INDEX:
				return definition.getTreeUserValueName();
				
			case DEFAULT_FORMAT_COLUMN_INDEX:
				return definition.getDefaultFormat();
				
			case THRESHOLD_BELOW_COLUMN_INDEX:
			case THRESHOLDS_COLUMN_INDEX:
				if (definition instanceof NumericTreeFilterDefinition) {
					NumericTreeFilterDefinition numericDefinition = (NumericTreeFilterDefinition)definition;
					if (columnIndex == THRESHOLD_BELOW_COLUMN_INDEX) {
						return numericDefinition.isBelowThreshold();
					}
					else {
						return numericDefinition.getThresholds().toString();  //TODO Create better String representation. (Or can lists be displayed directly?)
					}
				}
				else {
					return null;  //TODO Does an empty String have to be returned here?
				}
				
			default:
				throw createInvalidColumnException(columnIndex);
		}
	}

	
	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		TreeFilterDefinition definition = getOrder().get(rowIndex);
		switch (columnIndex) {
			case NAME_COLUMN_INDEX:
				definition.setName((String)value);
				break;
				
			case USER_EXPRESSION_COLUMN_INDEX:
				definition.setTreeUserValueName((String)value);
				break;
				
			case DEFAULT_FORMAT_COLUMN_INDEX:
				definition.setDefaultFormat((String)value);
				break;
				
			case THRESHOLD_BELOW_COLUMN_INDEX:
				if (definition instanceof NumericTreeFilterDefinition) {
					((NumericTreeFilterDefinition)definition).setBelowThreshold((Boolean)value);
				}
				else {
					throw new IllegalArgumentException("The \"Below Theshold\" column cannot be edited in this row since it does not contain numeric filter information.");
				}
				break;
				
			default:
				throw createInvalidColumnException(columnIndex);
		}
	}
}
