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


import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import info.bioinfweb.tta.data.UserExpression;
import info.bioinfweb.tta.data.UserExpressions;
import info.bioinfweb.tta.exception.DuplicateEntryException;



public class UserExpressionsAdapter extends XmlAdapter<UserExpressionsAdapter.ExpressionList, UserExpressions> {
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class UserExpressionWithName {
		@XmlAttribute
		private String name;
		
		@XmlAttribute
		private boolean isTreeData;
		
		@XmlValue
		private String expression;
		
		public UserExpressionWithName() {  // For JAXB.
			super();
		}
		
		public UserExpressionWithName(String name, boolean isTreeData, String expression) {
			super();
			this.name = name;
			this.isTreeData = isTreeData;
			this.expression = expression;
		}
	}
	
	
	public static class ExpressionList {
		@XmlElement(name = "expression")
		private List<UserExpressionWithName> expressions = new ArrayList<UserExpressionWithName>();
	}
	
	
	@Override
	public ExpressionList marshal(UserExpressions expressions) throws Exception {
		if (expressions.isConsistent()) {
			ExpressionList result = new ExpressionList();
			for (String name : expressions.getInputOrder()) {
				UserExpression expression = expressions.getExpressions().get(name);
				result.expressions.add(new UserExpressionWithName(name, expression.hasTreeTarget(), expression.getExpression()));
			}
			return result;
		}
		else {
			throw new IllegalStateException("The data in the UserExpressions object is not consistent.");  //TODO Output lengths?
		}
	}
	

	@Override
	public UserExpressions unmarshal(ExpressionList list) throws Exception {
		UserExpressions result = new UserExpressions();
		for (UserExpressionWithName entry : list.expressions) {
			if (!result.getExpressions().containsKey(entry.name)) {
				result.getInputOrder().add(entry.name);
				System.out.println("unmarshalling " + entry.name + " " + result.getInputOrder().size());
				result.getExpressions().put(entry.name, new UserExpression(entry.isTreeData, entry.expression));
			}
			else {
				throw new DuplicateEntryException("The parameter file contains more than one user expression with the name \"" + entry.name + 
						"\". Each user expression must have a unique name.");
			}
		}
		return result;
	}
}
