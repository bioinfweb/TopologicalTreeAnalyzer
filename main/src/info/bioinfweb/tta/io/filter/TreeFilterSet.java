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
package info.bioinfweb.tta.io.filter;


import java.util.HashSet;
import java.util.Set;

import info.bioinfweb.tta.data.TreeIdentifier;



public class TreeFilterSet {
	private String fileName;
	private String format;
	private Set<TreeIdentifier> trees;
	
	
	public TreeFilterSet(String fileName, String format) {
		super();
		this.fileName = fileName;
		this.format = format;
		trees = new HashSet<TreeIdentifier>();
	}


	public String getFileName() {
		return fileName;
	}


	public String getFormat() {
		return format;
	}


	public Set<TreeIdentifier> getTrees() {
		return trees;
	}
}
