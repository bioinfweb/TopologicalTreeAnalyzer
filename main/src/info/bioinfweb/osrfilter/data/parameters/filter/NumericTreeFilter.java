package info.bioinfweb.osrfilter.data.parameters.filter;


import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;



@XmlAccessorType(XmlAccessType.FIELD)
public abstract class NumericTreeFilter extends BooleanTreeFilter {
	public static class Absolute extends NumericTreeFilter {
		public Absolute() {
			super();
		}

		public Absolute(String name, String treeUserValueName, boolean belowThreshold, String defaultFormat) {
			super(name, treeUserValueName, belowThreshold, defaultFormat);
		}
	}
	
	
	public static class Relative extends NumericTreeFilter {
		public Relative() {
			super();
		}

		public Relative(String name, String treeUserValueName, boolean belowThreshold, String defaultFormat) {
			super(name, treeUserValueName, belowThreshold, defaultFormat);
		}
	}
	
	
	@XmlAttribute
	private boolean belowThreshold;
	
	@XmlElement(name="threshold")
	private List<TreeFilterThreshold> thresholds = new ArrayList<>();
	
	
	protected NumericTreeFilter() {
		this("", "", true, null);  //TODO Adjust default values?
	}


	protected NumericTreeFilter(String name, String treeUserValueName, boolean belowThreshold, String defaultFormat) {
		super();
		this.name = name;
		this.treeUserValueName = treeUserValueName;
		this.belowThreshold = belowThreshold;
		this.defaultFormat = defaultFormat;
	}


	public boolean isBelowThreshold() {
		return belowThreshold;
	}


	public void setBelowThreshold(boolean belowThreshold) {
		this.belowThreshold = belowThreshold;
	}


	public List<TreeFilterThreshold> getThresholds() {
		return thresholds;
	}
}
