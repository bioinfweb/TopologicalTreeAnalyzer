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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class UserExpressions {
	private Map<String, UserExpression> expressions = new HashMap<String, UserExpression>();
	private List<String> order = new ArrayList<String>();
	
	
	public Map<String, UserExpression> getExpressions() {
		return expressions;
	}
	
	
	public List<String> getOrder() {
		return order;
	}
	
	
	public boolean isConsistent() {
		return order.size() == expressions.size();
	}
}
