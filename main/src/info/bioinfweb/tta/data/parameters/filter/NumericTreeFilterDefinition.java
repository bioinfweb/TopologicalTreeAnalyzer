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
package info.bioinfweb.tta.data.parameters.filter;


import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;



@XmlAccessorType(XmlAccessType.FIELD)
public abstract class NumericTreeFilterDefinition extends TreeFilterDefinition {
	public static class Absolute extends NumericTreeFilterDefinition {
		public Absolute() {
			super();
		}

		public Absolute(String name, String treeUserValueName, boolean belowThreshold, String defaultFormat) {
			super(name, treeUserValueName, belowThreshold, defaultFormat);
		}
	}
	
	
	public static class Relative extends NumericTreeFilterDefinition {
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
	
	
	protected NumericTreeFilterDefinition() {
		this("", "", true, null);  //TODO Adjust default values?
	}


	protected NumericTreeFilterDefinition(String name, String treeUserValueName, boolean belowThreshold, String defaultFormat) {
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
