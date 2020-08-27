package info.bioinfweb.osrfilter.io.parameters;


import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import info.bioinfweb.osrfilter.data.UserExpression;
import info.bioinfweb.osrfilter.data.UserExpressions;



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
		@XmlElement(name = "expression", namespace=XMLConstants.PARAMETERS_NS)
		private List<UserExpressionWithName> expressions = new ArrayList<UserExpressionWithName>();
	}
	
	
	@Override
	public ExpressionList marshal(UserExpressions expressions) throws Exception {
		if (expressions.isConsistent()) {
			ExpressionList result = new ExpressionList();
			for (String name : expressions.getOrder()) {
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
			result.getOrder().add(entry.name);
			result.getExpressions().put(entry.name, new UserExpression(entry.isTreeData, entry.expression));
		}
		return result;
	}
}
