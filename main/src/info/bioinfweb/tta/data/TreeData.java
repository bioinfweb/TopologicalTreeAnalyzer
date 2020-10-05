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
package info.bioinfweb.tta.data;


import java.util.HashMap;
import java.util.Map;



public class TreeData implements UserValueData {
	private int terminals;
	private int splits;
	private Map<String, Object> userValues = new HashMap<String, Object>();
	
	
	public TreeData(int terminals, int splits) {
		super();
		this.terminals = terminals;
		this.splits = splits;
	}


	public TreeData() {
		this(0, 0);
	}


	public int getTerminals() {
		return terminals;
	}


	public void setTerminals(int terminals) {
		this.terminals = terminals;
	}


	public int getSplits() {
		return splits;
	}


	public void setSplits(int splits) {
		this.splits = splits;
	}


	@Override
	public Map<String, Object> getUserValues() {
		return userValues;
	}
}
