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
import java.util.List;

import info.bioinfweb.commons.SystemUtils;
import info.bioinfweb.tta.data.TreeIdentifier;



public class TopologicalDataWriter extends AbstractTableWriter {
	private void writeLineBreak(Writer writer) throws IOException {
		writer.write(SystemUtils.LINE_SEPARATOR);  //TODO Always use the same delimiter instead of OS-dependent one?
	}
	
	
	public void writeTreeList() {
		
	}
	
	
	public void writeTreeData() {
		
	}
	
	
	public void writePairData(File file, List<TreeIdentifier> trees) throws IOException {
		FileWriter fileWriter = new FileWriter(file);
		try {
			BufferedWriter writer = new BufferedWriter(fileWriter);
			try {
				writeTreeHeadings(writer, "\t", "");
				writeLineBreak(writer);
				for (TreeIdentifier identifier : trees) {
					writeIdentifier(writer, identifier, "\t");
					writeLineBreak(writer);
				}
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
