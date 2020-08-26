package info.bioinfweb.osrfilter.data;


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
