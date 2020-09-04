package info.bioinfweb.osrfilter.data.parameters.filter;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import info.bioinfweb.osrfilter.io.parameters.FormatAdapter;



@XmlAccessorType(XmlAccessType.FIELD)
public class TreeFilterThreshold {
	@XmlValue
	private double threshold = Double.NaN;

	@XmlAttribute
	@XmlJavaTypeAdapter(FormatAdapter.class)
	private String format = null;
	
	
	public TreeFilterThreshold() {
		super();
	}
	
	
	public TreeFilterThreshold(double threshold, String format) {
		super();
		this.threshold = threshold;
		this.format = format;
	}


	public double getThreshold() {
		return threshold;
	}


	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}


	public String getFormat() {
		return format;
	}


	public void setFormat(String format) {
		this.format = format;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((format == null) ? 0 : format.hashCode());
		long temp;
		temp = Double.doubleToLongBits(threshold);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		TreeFilterThreshold other = (TreeFilterThreshold) obj;
		if (format == null) {
			if (other.format != null)
				return false;
		} else if (!format.equals(other.format))
			return false;
		if (Double.doubleToLongBits(threshold) != Double.doubleToLongBits(other.threshold))
			return false;
		return true;
	}
}
