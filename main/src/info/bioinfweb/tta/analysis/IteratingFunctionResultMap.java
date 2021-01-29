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
package info.bioinfweb.tta.analysis;


import java.util.HashMap;
import java.util.Map;



public class IteratingFunctionResultMap {
	public static final String KEY_SEPARATOR = ">";
	
	
	private Map<String, Object> valueMap = new HashMap<String, Object>();
	
	
	private String createKey(String functionName, String expressionName) {
		return functionName + KEY_SEPARATOR + expressionName;
	}
	
	
	public Object getValue(String functionName, String expressionName) {
		return valueMap.get(createKey(functionName, expressionName));
	}
	
	
	public Object setValue(String functionName, String expressionName, Object value) {
		return valueMap.put(createKey(functionName, expressionName), value);
	}


	public void clear() {
		valueMap.clear();
	}
}
