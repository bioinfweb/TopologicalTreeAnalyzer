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
package info.bioinfweb.tta.data.parameters.filter;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import info.bioinfweb.tta.io.parameters.FormatAdapter;



@XmlAccessorType(XmlAccessType.FIELD)
public class TreeFilterDefinition implements Comparable<TreeFilterDefinition> {
	@XmlAttribute
	protected String name;

	@XmlAttribute
	protected String treeUserValueName;
	
	@XmlAttribute(name = "format")
	@XmlJavaTypeAdapter(FormatAdapter.class)
	protected String defaultFormat;

	
	protected TreeFilterDefinition() {
		super();
	}

	
	protected TreeFilterDefinition(String name, String treeUserValueName, String defaultFormat) {
		super();
		this.name = name;
		this.treeUserValueName = treeUserValueName;
		this.defaultFormat = defaultFormat;
	}


	public String getName() {
		return name;
	}

	
	public void setName(String name) {
		this.name = name;
	}

	
	public String getTreeUserValueName() {
		return treeUserValueName;
	}

	
	public void setTreeUserValueName(String treeUserValue) {
		this.treeUserValueName = treeUserValue;
	}

	
	public String getDefaultFormat() {
		return defaultFormat;
	}

	
	public void setDefaultFormat(String defaultFormat) {
		this.defaultFormat = defaultFormat;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TreeFilterDefinition other = (TreeFilterDefinition) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}


	@Override
	public int compareTo(TreeFilterDefinition o) {
		return getName().compareTo(o.getName());
	}
}