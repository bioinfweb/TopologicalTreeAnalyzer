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


import java.io.IOException;
import java.io.Writer;

import info.bioinfweb.tta.data.TreeIdentifier;



public class AbstractTableWriter {
	public static final String FILE_HEADING = "File";
	public static final String ID_HEADING = "ID";
	public static final String TREE_NAME_HEADING = "Name";
	public static final String TREE_A_SUFFIX = " A";
	public static final String TREE_B_SUFFIX = " B";
	
	
	protected void writeTreeIdentifierHeadings(Writer writer, String columnDelimiter, String suffix) throws IOException {
		writer.write(FILE_HEADING + suffix);
		writer.write(columnDelimiter);
		writer.write(ID_HEADING + suffix);
		writer.write(columnDelimiter);
		writer.write(TREE_NAME_HEADING + suffix);
	}
	
	
	protected void writeIdentifier(Writer writer, TreeIdentifier identifier, String columnDelimiter) throws IOException {
		writer.write(identifier.getFile().toString());  //TODO Use absolute path?
		writer.write(columnDelimiter);
		writer.write(identifier.getID());
		writer.write(columnDelimiter);
		if (identifier.getName() != null) {
			writer.write(identifier.getName());
		}
	}
}
