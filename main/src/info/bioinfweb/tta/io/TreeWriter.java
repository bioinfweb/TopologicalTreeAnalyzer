package info.bioinfweb.tta.io;


import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import info.bioinfweb.jphyloio.ReadWriteParameterMap;
import info.bioinfweb.jphyloio.factory.JPhyloIOReaderWriterFactory;
import info.bioinfweb.tta.data.TreeData;
import info.bioinfweb.tta.data.TreeIdentifier;
import info.bioinfweb.tta.data.parameters.filter.TreeFilterDefinition;
import info.bioinfweb.tta.data.parameters.filter.TreeFilterDefinitionSet;
import info.bioinfweb.tta.io.filter.TreeFilter;
import info.bioinfweb.tta.io.filter.TreeFilterFactory;
import info.bioinfweb.tta.io.filter.TreeFilterSet;
import info.bioinfweb.tta.io.filter.document.FilterDocumentDataAdapter;



public class TreeWriter {
	public static final JPhyloIOReaderWriterFactory READER_WRITER_FACTORY = new JPhyloIOReaderWriterFactory();
	
	
	public void writeFilterOutputs(TreeFilterDefinitionSet definitions, File outputDirectory, String[] treeFilesNames, 
			Map<TreeIdentifier, TreeData> treeDataMap) throws IOException, Exception {
		
		for (TreeFilterDefinition definition : definitions) {
			TreeFilter<?> filter = TreeFilterFactory.getInstance().createTreeFilter(definition, treeDataMap);
			while (filter.hasNext()) {
				TreeFilterSet set = filter.next();
				READER_WRITER_FACTORY.getWriter(set.getFormat()).writeDocument(new FilterDocumentDataAdapter(set, Arrays.asList(treeFilesNames)), 
						new File(outputDirectory.getAbsolutePath() + File.separator + set.getFileName()), new ReadWriteParameterMap());
			}
		}
	}
	//TODO Possibly sort trees here later. (Using multiple temporary files.)
}
