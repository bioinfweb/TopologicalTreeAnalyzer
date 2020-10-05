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
package info.bioinfweb.tta.data.parameters;


import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import info.bioinfweb.commons.SystemUtils;


@XmlAccessorType(XmlAccessType.FIELD)
public class ExportColumnList {
	@XmlAttribute
	private String columnDelimiter = "\t";
	
	@XmlAttribute
	private String lineDelimiter = SystemUtils.LINE_SEPARATOR;
	
	@XmlElement(name="column")
	private List<String> columns = new ArrayList<String>();
	
	
	public String getColumnDelimiter() {
		return columnDelimiter;
	}
	
	
	public void setColumnDelimiter(String columnDelimiter) {
		this.columnDelimiter = columnDelimiter;
	}


	public String getLineDelimiter() {
		return lineDelimiter;
	}
	
	
	public void setLineDelimiter(String lineDelimiter) {
		this.lineDelimiter = lineDelimiter;
	}


	public List<String> getColumns() {
		return columns;
	}
}
