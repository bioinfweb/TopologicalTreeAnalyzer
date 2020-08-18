package info.bioinfweb.osrfilter.analysis.calculation;


import java.util.Map;
import java.util.Stack;

import org.nfunk.jep.ParseException;

import info.bioinfweb.osrfilter.analysis.UserExpressionDataProvider;



public class TreeUserValueFunction extends AbstractFunction implements UserValueFunction {
	public TreeUserValueFunction(UserExpressionDataProvider expressionData) {
		super(expressionData);
	}


	@Override
	public String getName() {
		return "treeUserValue";
	}


	@Override
	public int getNumberOfParameters() {
		return getExpressionData().isTreeExpression() ? 1 : 2;
	}


	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void run(Stack stack) throws ParseException {
		// Get index:
		int index = 0;
		if (!getExpressionData().isTreeExpression()) {
			Object sourceTree = stack.pop();  // Note that the last parameter is taken from the stack first.
			if (sourceTree instanceof Number) {
				index = ((Number)sourceTree).intValue();
			}
			else {
				throw new ParseException("Invalid parameter type. This function must have one numeric parameter when used to calculate pair data.");
			}
		}
		
		// Return user value:
		Object name = stack.pop();
		if (name instanceof CharSequence) {
			Map<String, Object> map = getExpressionData().getCurrentTreeData(index).getUserValues();
			if (map.containsKey(name)) {
				stack.push(map.get(name));
			}
			else {
				throw new ParseException("Invalid parameter for " + getName() + "(). No user value \"" + name + "\" could be found.");
			}
		}
		else {
			if (getExpressionData().isTreeExpression()) {
				throw new ParseException("Invalid parameter type for " + getName()
						+	"(). This function must have one textual parameter defining the referenced user expression.");
			}
			else {
				throw new ParseException("Invalid parameter type for " + getName()
						+ "(). The first parameter of this function must be textual and define the referenced user expression.");
			}
		}
	}
}
