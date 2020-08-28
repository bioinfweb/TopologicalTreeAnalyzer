package info.bioinfweb.osrfilter.data.parameters;


import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;



@XmlAccessorType(XmlAccessType.FIELD)
public class TreeFilter {
	@XmlAttribute
	private String name;
	
	@XmlAttribute
	private String treeUserValueName;
	
	@XmlAttribute
	private boolean relativeThreshold;
	
	@XmlAttribute
	private boolean belowThreshold;
	
	@XmlAttribute(name="format")
	private String defaultFormat;
	
	@XmlElement(name="threshold")
	private List<TreeFilterThreshold> thresholds = new ArrayList<>();
	
	
	public TreeFilter() {
		this("", "", true, false, null);  //TODO Adjust default values?
	}


	public TreeFilter(String name, String treeUserValueName, boolean relativeThreshold, boolean belowThreshold, String defaultFormat) {
		super();
		this.name = name;
		this.treeUserValueName = treeUserValueName;
		this.relativeThreshold = relativeThreshold;
		this.belowThreshold = belowThreshold;
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


	public boolean isRelativeThreshold() {
		return relativeThreshold;
	}


	public void setRelativeThreshold(boolean relativeTheshold) {
		this.relativeThreshold = relativeTheshold;
	}


	public boolean isBelowThreshold() {
		return belowThreshold;
	}


	public void setBelowThreshold(boolean belowThreshold) {
		this.belowThreshold = belowThreshold;
	}


	public String getDefaultFormat() {
		return defaultFormat;
	}


	public void setDefaultFormat(String defaultFormat) {
		this.defaultFormat = defaultFormat;
	}


	public List<TreeFilterThreshold> getThresholds() {
		return thresholds;
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
		TreeFilter other = (TreeFilter) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
