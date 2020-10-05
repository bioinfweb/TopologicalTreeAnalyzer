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
package info.bioinfweb.tta.io.filter.document;


import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.iterators.SingletonIterator;

import info.bioinfweb.jphyloio.ReadWriteParameterMap;
import info.bioinfweb.jphyloio.dataadapters.DocumentDataAdapter;
import info.bioinfweb.jphyloio.dataadapters.TreeNetworkGroupDataAdapter;
import info.bioinfweb.jphyloio.dataadapters.implementations.EmptyDocumentDataAdapter;
import info.bioinfweb.tta.io.filter.TreeFilterSet;



public class FilterDocumentDataAdapter extends EmptyDocumentDataAdapter implements DocumentDataAdapter {
	private FilterTreeGroupAdapter treeGroup;
	
	
	public FilterDocumentDataAdapter(TreeFilterSet filterSet, List<String> treeFilesNames) {
		super();
		treeGroup = new FilterTreeGroupAdapter(filterSet, treeFilesNames);
	}

	
	@Override
	public Iterator<TreeNetworkGroupDataAdapter> getTreeNetworkGroupIterator(ReadWriteParameterMap parameters) {
		return new SingletonIterator<TreeNetworkGroupDataAdapter>(treeGroup, false);
	}
}
