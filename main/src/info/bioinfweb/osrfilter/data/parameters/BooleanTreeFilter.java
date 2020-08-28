package info.bioinfweb.osrfilter.data.parameters;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;



@XmlAccessorType(XmlAccessType.FIELD)
public class BooleanTreeFilter extends TreeFilter {
	public BooleanTreeFilter() {
		super();
	}

	
	public BooleanTreeFilter(String name, String treeUserValueName, String defaultFormat) {
		super(name, treeUserValueName, defaultFormat);
	}
}