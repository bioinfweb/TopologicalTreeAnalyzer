package info.bioinfweb.tta.data.parameters.filter;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import info.bioinfweb.tta.io.parameters.FormatAdapter;



@XmlAccessorType(XmlAccessType.FIELD)
public class TreeFilterDefinition {
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
}