package info.bioinfweb.osrfilter.data.parameters;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;



@XmlAccessorType(XmlAccessType.FIELD)
public class TreeFilter {
	@XmlAttribute
	private String name;
	
	@XmlAttribute
	private String treeUserValueName;
	
	@XmlAttribute
	private double threshold;
	
	@XmlAttribute
	private boolean relativeThreshold;
	
	@XmlAttribute
	private boolean belowThreshold;
	
	
	public TreeFilter() {
		this("", "", 0.0, true, false);  //TODO Adjust default values?
	}


	public TreeFilter(String name, String treeUserValue, double threshold, boolean relativeTheshold, boolean belowThreshold) {
		super();
		this.name = name;
		this.treeUserValueName = treeUserValue;
		this.threshold = threshold;
		this.relativeThreshold = relativeTheshold;
		this.belowThreshold = belowThreshold;
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


	public double getThreshold() {
		return threshold;
	}


	public void setThreshold(double threshold) {
		this.threshold = threshold;
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
