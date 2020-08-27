package info.bioinfweb.osrfilter.data.parameters;


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
