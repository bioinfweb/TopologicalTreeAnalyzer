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
package info.bioinfweb.tta.data;


import org.nfunk.jep.Node;



public class UserExpression {
	private boolean treeTarget;
	private String expression;
	private Node root;
	private Class<?> type;
	
	
	public UserExpression(boolean hasTreeTarget, String expression) {
		this(hasTreeTarget, expression, null);
	}
	
	
	public UserExpression(boolean hasTreeTarget, String expression, Class<?> type) {
		super();
		this.treeTarget = hasTreeTarget;
		this.expression = expression;
		this.root = null;
		this.type = type;
	}


	public boolean hasTreeTarget() {
		return treeTarget;
	}


	public void setTreeTarget(boolean treeTarget) {
		this.treeTarget = treeTarget;
	}


	public String getExpression() {
		return expression;
	}


	public void setExpression(String expression) {
		this.expression = expression;
	}


	public Node getRoot() {
		return root;
	}


	public void setRoot(Node root) {
		this.root = root;
	}


	public Class<?> getType() {
		return type;
	}


	public void setType(Class<?> type) {
		this.type = type;
	}
}
