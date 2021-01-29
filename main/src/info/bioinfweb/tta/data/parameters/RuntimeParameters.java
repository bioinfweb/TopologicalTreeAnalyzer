/*
 * Topological Tree Analyzer - User defined topological analyses of large sets of phylogenetic trees. 
 * Copyright (C) 2020-2021  Ben C. Stöver
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


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import info.bioinfweb.tta.io.parameters.RuntimeParameterAdapter;



@XmlAccessorType(XmlAccessType.FIELD)
public class RuntimeParameters {
	public static final long MAXIMUM = -1;
	
	
	@XmlAttribute
	@XmlJavaTypeAdapter(type=long.class, value=RuntimeParameterAdapter.Threads.class)
	private long threads;
	
	@XmlAttribute
	@XmlJavaTypeAdapter(type=long.class, value=RuntimeParameterAdapter.Memory.class)
	private long memory;
	
	
	public RuntimeParameters() {
		this(MAXIMUM, MAXIMUM);
	}


	public RuntimeParameters(long threads, long memory) {
		super();
		this.threads = threads;
		this.memory = memory;
	}


	public long getThreads() {
		return threads;
	}


	public void setThreads(long threads) {
		this.threads = threads;
	}


	public long getMemory() {
		return memory;
	}


	public void setMemory(long memory) {
		this.memory = memory;
	}
}
