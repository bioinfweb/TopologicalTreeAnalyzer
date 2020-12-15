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
package info.bioinfweb.tta.io;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.util.Map;

import info.bioinfweb.tta.data.TreeIdentifier;
import info.bioinfweb.tta.data.TreePair;
import info.bioinfweb.tta.data.UserValues;
import info.bioinfweb.tta.data.database.DatabaseIterator;
import info.bioinfweb.tta.data.database.PairUserDataTable;
import info.bioinfweb.tta.data.database.TreeUserDataTable;
import info.bioinfweb.tta.data.parameters.ExportColumnList;



public class UserValueTableWriter extends AbstractTableWriter {
	private void writeUserValueHeadings(Writer writer, ExportColumnList exportColumns) throws IOException {
		for (String userValueName : exportColumns.getColumns()) {
			writer.write(exportColumns.getColumnDelimiter());
			writer.write(userValueName);
		}
	}
	
	
	private void writeUserData(Writer writer, ExportColumnList exportColumns, Map<String, Object> userValues) throws IOException {
		for (String userValueName : exportColumns.getColumns()) {
			writer.write(exportColumns.getColumnDelimiter());
			writer.write(userValues.get(userValueName).toString());
		}
	}
	
	
	public void writeTreeData(Writer writer, ExportColumnList exportColumns, TreeUserDataTable treeData) throws IOException, SQLException {
		writeTreeIdentifierHeadings(writer, exportColumns.getColumnDelimiter(), "");
		writeUserValueHeadings(writer, exportColumns);
		
		DatabaseIterator<TreeIdentifier, UserValues<TreeIdentifier>> iterator = treeData.valueIterator();
		try {
			while (iterator.hasNext()) {
				UserValues<TreeIdentifier> rowData = iterator.next();
				writer.write(exportColumns.getLineDelimiter());
				writeIdentifier(writer, rowData.getKey(), exportColumns.getColumnDelimiter());
				writeUserData(writer, exportColumns, rowData.getUserValues());
			}
		}
		finally {
			iterator.close();
		}
	}
	
	
	public void writePairData(Writer writer, ExportColumnList exportColumns, PairUserDataTable pairData) throws IOException, SQLException {
		writeTreeIdentifierHeadings(writer, exportColumns.getColumnDelimiter(), TREE_A_SUFFIX);
		writer.write(exportColumns.getColumnDelimiter());
		writeTreeIdentifierHeadings(writer, exportColumns.getColumnDelimiter(), TREE_B_SUFFIX);
		writeUserValueHeadings(writer, exportColumns);
		
		DatabaseIterator<TreePair, UserValues<TreePair>> iterator = pairData.valueIterator();
		try {
			while (iterator.hasNext()) {
				UserValues<TreePair> rowData = iterator.next();

				writer.write(exportColumns.getLineDelimiter());
				
				writeIdentifier(writer, rowData.getKey().getTreeA(), exportColumns.getColumnDelimiter());
				writer.write(exportColumns.getColumnDelimiter());
				writeIdentifier(writer, rowData.getKey().getTreeB(), exportColumns.getColumnDelimiter());
				
				writeUserData(writer, exportColumns, rowData.getUserValues());
			}
		}
		finally {
			iterator.close();
		}
	}
	

	public void writeTreeData(File file, ExportColumnList exportColumns, TreeUserDataTable treeData) throws IOException, SQLException {
		FileWriter fileWriter = new FileWriter(file);
		try {
			BufferedWriter writer = new BufferedWriter(fileWriter);
			try {
				writeTreeData(writer, exportColumns, treeData);
			}
			finally {
				writer.close();
			}
		}
		finally {
			fileWriter.close();
		}
	}
	

	public void writePairData(File file, ExportColumnList exportColumns, PairUserDataTable pairData) throws IOException, SQLException {
		FileWriter fileWriter = new FileWriter(file);
		try {
			BufferedWriter writer = new BufferedWriter(fileWriter);
			try {
				writePairData(writer, exportColumns, pairData);
			}
			finally {
				writer.close();
			}
		}
		finally {
			fileWriter.close();
		}
	}
}
