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
package info.bioinfweb.tta.io.treeiterator;


import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import info.bioinfweb.jphyloio.JPhyloIOEventReader;
import info.bioinfweb.jphyloio.ReadWriteParameterMap;
import info.bioinfweb.jphyloio.events.type.EventContentType;
import info.bioinfweb.jphyloio.events.type.EventTopologyType;
import info.bioinfweb.jphyloio.events.type.EventType;
import info.bioinfweb.jphyloio.factory.JPhyloIOReaderWriterFactory;
import info.bioinfweb.tta.data.TTATree;



public abstract class AbstractTreeIterator<T> {
	private static final EventType TREE_START = new EventType(EventContentType.TREE, EventTopologyType.START);
	
	
	protected TTATree<T> nextTree;
	protected File[] files;
	private int filePos = 0;
	protected int indexInFile = 0;
	private JPhyloIOReaderWriterFactory factory = new JPhyloIOReaderWriterFactory();
	private JPhyloIOEventReader reader = null;
	private boolean isUnusedInstance = true;

	
	public AbstractTreeIterator(File... files) throws IOException, Exception {
		super();
		this.files = files;
	}
	

	public AbstractTreeIterator(List<String> fileNames) throws IOException, Exception {
		this(fileNames.toArray(new String[fileNames.size()]));
	}
	
	
	public AbstractTreeIterator(String... fileNames) throws IOException, Exception {
		super();
		files = new File[fileNames.length];
		for (int i = 0; i < fileNames.length; i++) {
			files[i] = new File(fileNames[i]);
		}
	}
	
	
	private void closeReader() throws IOException {
		if (reader != null) {
			reader.close();
		}
		indexInFile = 0;
	}
	
	
	private boolean moveBeforeNextTreeStart() throws ClassCastException, IOException {
		while (reader.hasNextEvent() && !TREE_START.equals(reader.peek().getType())) {
			reader.next();
		}
		return reader.hasNextEvent();
	}
	
	
	protected abstract TTATree<T> loadTree(JPhyloIOEventReader reader, File file) throws IOException, XMLStreamException;

	
	private void ensureFirstElement() throws IOException, Exception {
		if (isUnusedInstance) {
			isUnusedInstance = false;
			readNext();
		}
	}
	
	
	protected void readNext() throws IOException, Exception {
		while (((reader == null) || !moveBeforeNextTreeStart()) && (filePos < files.length)) {
			closeReader();
			reader = factory.guessReader(files[filePos], new ReadWriteParameterMap());
			filePos++;
		}  // Loop to skip possible files with no trees. 
		
		if ((reader != null) && reader.hasNextEvent()) {
			nextTree = loadTree(reader, files[filePos - 1]);
			nextTree.getTreeIdentifier().setIndexInFile(indexInFile);
			indexInFile++;
		}
		else {
			nextTree = null;  // No more trees to read.
			closeReader();
		}
	}
	

	public boolean hasNext() throws IOException, Exception {
		ensureFirstElement();
		return nextTree != null;
	}

	
	public TTATree<T> next() throws IOException, Exception {
		ensureFirstElement();
		TTATree<T> result = nextTree;
		readNext();
		return result;
	}
	
	
	public void reset() throws Exception {
		closeReader();
		reader = null;
		filePos = 0;
		ensureFirstElement();
	}
}
