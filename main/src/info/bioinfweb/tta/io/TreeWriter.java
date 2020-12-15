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


import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import info.bioinfweb.jphyloio.ReadWriteParameterMap;
import info.bioinfweb.jphyloio.factory.JPhyloIOReaderWriterFactory;
import info.bioinfweb.tta.data.database.TreeUserDataTable;
import info.bioinfweb.tta.data.parameters.filter.TreeFilterDefinition;
import info.bioinfweb.tta.data.parameters.filter.TreeFilterDefinitionSet;
import info.bioinfweb.tta.io.filter.TreeFilter;
import info.bioinfweb.tta.io.filter.TreeFilterFactory;
import info.bioinfweb.tta.io.filter.TreeFilterSet;
import info.bioinfweb.tta.io.filter.document.FilterDocumentDataAdapter;



public class TreeWriter {
	public static final JPhyloIOReaderWriterFactory READER_WRITER_FACTORY = new JPhyloIOReaderWriterFactory();
	
	
	public void writeFilterOutputs(TreeFilterDefinitionSet definitions, File outputDirectory, String[] treeFilesNames, 
			TreeUserDataTable treeUserData) throws IOException, Exception {
		
		for (TreeFilterDefinition definition : definitions) {
			TreeFilter<?> filter = TreeFilterFactory.getInstance().createTreeFilter(definition, treeUserData);
			while (filter.hasNext()) {
				TreeFilterSet set = filter.next();
				READER_WRITER_FACTORY.getWriter(set.getFormat()).writeDocument(new FilterDocumentDataAdapter(set, Arrays.asList(treeFilesNames)), 
						new File(outputDirectory.getAbsolutePath() + File.separator + set.getFileName()), new ReadWriteParameterMap());
			}
		}
	}
	//TODO Possibly sort trees here later. (Using multiple temporary files.)
}
