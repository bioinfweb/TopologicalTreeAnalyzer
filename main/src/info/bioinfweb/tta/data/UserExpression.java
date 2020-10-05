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


import org.nfunk.jep.Node;



public class UserExpression {
	private boolean treeTarget;
	private String expression;
	private Node root;
	
	
	public UserExpression(boolean hasTreeTarget, String expression) {
		super();
		this.treeTarget = hasTreeTarget;
		this.expression = expression;
		this.root = null;
	}


	public boolean hasTreeTarget() {
		return treeTarget;
	}


	public String getExpression() {
		return expression;
	}


	public Node getRoot() {
		return root;
	}


	public void setRoot(Node root) {
		this.root = root;
	}
}
