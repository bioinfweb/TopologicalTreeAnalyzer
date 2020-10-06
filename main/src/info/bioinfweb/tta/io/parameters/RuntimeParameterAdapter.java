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
package info.bioinfweb.tta.io.parameters;


import javax.xml.bind.annotation.adapters.XmlAdapter;

import info.bioinfweb.commons.Math2;
import info.bioinfweb.tta.data.parameters.RuntimeParameters;



public abstract class RuntimeParameterAdapter extends XmlAdapter<String, Long> {
	public static final String MAX_CONSTANT = "max";
	
	
	public static class Threads extends RuntimeParameterAdapter {
		public Threads() {
			super(new XmlAdapter<String, Long>() {
				@Override
				public String marshal(Long modelValue) throws Exception {
					return modelValue.toString();
				}

				@Override
				public Long unmarshal(String xmlValue) throws Exception {
					return Long.parseLong(xmlValue);
				}
			});
		}
	}
	
	
	public static class Memory extends RuntimeParameterAdapter {
		public static final String KIBI_SUFFIX = "K";
		public static final String MEBI_SUFFIX = "M";
		public static final String GIBI_SUFFIX = "G";
		public static final String TEBI_SUFFIX = "T";
		public static final String PEBI_SUFFIX = "P";
		public static final long MAGNITUDE = 1024;
		
		
		public Memory() {
			super(new XmlAdapter<String, Long>() {
				@Override
				public String marshal(Long modelValue) throws Exception {
					return modelValue.toString();
				}

				@Override
				public Long unmarshal(String xmlValue) throws Exception {
					long exponent = 0;
					xmlValue = xmlValue.toUpperCase();
					if (xmlValue.endsWith(KIBI_SUFFIX)) {
						exponent = 1;
					}
					else if (xmlValue.endsWith(MEBI_SUFFIX)) {
						exponent = 2;
					}
					else if (xmlValue.endsWith(GIBI_SUFFIX)) {
						exponent = 3;
					}
					else if (xmlValue.endsWith(TEBI_SUFFIX)) {
						exponent = 4;
					}
					else if (xmlValue.endsWith(PEBI_SUFFIX)) {
						exponent = 5;
					}
					
					if (exponent > 0) {
						xmlValue = xmlValue.substring(0, xmlValue.length() - 1);
					}
					
					return Math2.longPow(MAGNITUDE, exponent) * Long.parseLong(xmlValue);
				}
			});
		}
	}
	
	
	private XmlAdapter<String, Long> adapter;
	
	
	public RuntimeParameterAdapter(XmlAdapter<String, Long> adapter) {
		super();
		this.adapter = adapter;
	}


	@Override
	public String marshal(Long modelValue) throws Exception {
		if (modelValue == RuntimeParameters.MAXIMUM) {
			return MAX_CONSTANT;
		}
		else if (modelValue < 1) {
			throw new IllegalArgumentException(modelValue.toString() + " is not a valid runtime parameter value.");
		}
		else {
			return adapter.marshal(modelValue);
		}
	}

	
	@Override
	public Long unmarshal(String xmlValue) throws Exception {
		if (MAX_CONSTANT.contentEquals(xmlValue.toLowerCase())) {
			return RuntimeParameters.MAXIMUM;
		}
		else {
			return adapter.unmarshal(xmlValue);
		}
	}
}
