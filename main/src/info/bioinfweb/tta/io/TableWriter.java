package info.bioinfweb.tta.io;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import info.bioinfweb.tta.data.PairComparisonData;
import info.bioinfweb.tta.data.TreeData;
import info.bioinfweb.tta.data.TreeIdentifier;
import info.bioinfweb.tta.data.TreePair;
import info.bioinfweb.tta.data.UserValueData;
import info.bioinfweb.tta.data.parameters.ExportColumnList;



public class TableWriter {
	public static final String FILE_HEADING = "File";
	public static final String ID_HEADING = "ID";
	public static final String TREE_NAME_HEADING = "Name";
	public static final String TREE_A_SUFFIX = " A";
	public static final String TREE_B_SUFFIX = " B";
	
	
	private void writeTreeHeadings(Writer writer, ExportColumnList exportColumns, String suffix) throws IOException {
		writer.write(FILE_HEADING + suffix);
		writer.write(exportColumns.getColumnDelimiter());
		writer.write(ID_HEADING + suffix);
		writer.write(exportColumns.getColumnDelimiter());
		writer.write(TREE_NAME_HEADING + suffix);
	}
	
	
	private void writeUserValueHeadings(Writer writer, ExportColumnList exportColumns) throws IOException {
		for (String userValueName : exportColumns.getColumns()) {
			writer.write(exportColumns.getColumnDelimiter());
			writer.write(userValueName);
		}
	}
	
	
	private void writeIdentifier(Writer writer, TreeIdentifier identifier, ExportColumnList exportColumns) throws IOException {
		writer.write(identifier.getFile().toString());  //TODO Use absolute path?
		writer.write(exportColumns.getColumnDelimiter());
		writer.write(identifier.getID());
		writer.write(exportColumns.getColumnDelimiter());
		if (identifier.getName() != null) {
			writer.write(identifier.getName());
		}
	}
	
	
	private void writeUserData(Writer writer, ExportColumnList exportColumns, UserValueData data) throws IOException {
		for (String userValueName : exportColumns.getColumns()) {
			writer.write(exportColumns.getColumnDelimiter());
			writer.write(data.getUserValues().get(userValueName).toString());
		}
	}
	
	
	public void writeTreeData(Writer writer, ExportColumnList exportColumns, Map<TreeIdentifier, TreeData> treeMap) throws IOException {
		writeTreeHeadings(writer, exportColumns, "");
		writeUserValueHeadings(writer, exportColumns);
		
		for (TreeIdentifier identifier : treeMap.keySet()) {
			writer.write(exportColumns.getLineDelimiter());
			writeIdentifier(writer, identifier, exportColumns);
			writeUserData(writer, exportColumns, treeMap.get(identifier));
		}
	}
	
	
	public void writePairData(Writer writer, ExportColumnList exportColumns,  Map<TreePair, PairComparisonData> comparisonMap) throws IOException {
		writeTreeHeadings(writer, exportColumns, TREE_A_SUFFIX);
		writer.write(exportColumns.getColumnDelimiter());
		writeTreeHeadings(writer, exportColumns, TREE_B_SUFFIX);
		writeUserValueHeadings(writer, exportColumns);
		
		for (TreePair pair : comparisonMap.keySet()) {
			writer.write(exportColumns.getLineDelimiter());
			
			writeIdentifier(writer, pair.getTreeA(), exportColumns);
			writer.write(exportColumns.getColumnDelimiter());
			writeIdentifier(writer, pair.getTreeB(), exportColumns);
			
			writeUserData(writer, exportColumns, comparisonMap.get(pair));
		}
	}
	

	public void writeTreeData(File file, ExportColumnList exportColumns, Map<TreeIdentifier, TreeData> treeMap) throws IOException {
		FileWriter fileWriter = new FileWriter(file);
		try {
			BufferedWriter writer = new BufferedWriter(fileWriter);
			try {
				writeTreeData(writer, exportColumns, treeMap);
			}
			finally {
				writer.close();
			}
		}
		finally {
			fileWriter.close();
		}
	}
	

	public void writePairData(File file, ExportColumnList exportColumns,  Map<TreePair, PairComparisonData> comparisonMap) throws IOException {
		FileWriter fileWriter = new FileWriter(file);
		try {
			BufferedWriter writer = new BufferedWriter(fileWriter);
			try {
				writePairData(writer, exportColumns, comparisonMap);
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
