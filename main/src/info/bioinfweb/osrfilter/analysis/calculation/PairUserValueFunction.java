package info.bioinfweb.osrfilter.analysis.calculation;


import java.util.Map;
import java.util.Stack;

import org.nfunk.jep.ParseException;

import info.bioinfweb.osrfilter.analysis.UserExpressionDataProvider;



public class PairUserValueFunction extends AbstractFunction {
	public PairUserValueFunction(UserExpressionDataProvider expressionData) {
		super(expressionData);
	}

	
	@Override
	public String getName() {
		return "pairUserValue";
	}


	@Override
	public int getNumberOfParameters() {
		return 1;
	}


	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void run(Stack stack) throws ParseException {
		if (!getExpressionData().isTreeExpression()) {
			Object name = stack.pop();
			if (name instanceof CharSequence) {
				Map<String, Object> map = getExpressionData().getCurrentComparisonData().getUserValues();
				if (map.containsKey(name)) {
					stack.push(map.get(name));
				}
				else {
					throw new ParseException("Invalid parameter for " + getName() + "(). No user value \"" + name + "\" could be found.");
				}
			}
			else {
				throw new ParseException("Invalid parameter type. This function must have one numeric parameter.");
			}
		}
		else {
			throw new ParseException("Access to current pair user values is only possible in pair expressions. (You can use treeUserValue() in tree expressions.)");
		}
	}
}
