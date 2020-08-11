package info.bioinfweb.osrfilter.analysis.calculation;


import java.util.Map;
import java.util.Stack;

import org.nfunk.jep.ParseException;

import info.bioinfweb.osrfilter.analysis.UserExpressionData;



public class UserValueFunction extends AbstractFunction {
	public UserValueFunction(UserExpressionData expressionData) {
		super(expressionData);
		setCurNumberOfParameters(1);
	}

	
	@Override
	public String getName() {
		return "userValue";
	}


	@Override
	public int getNumberOfParameters() {
		return 1;
	}


	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void run(Stack stack) throws ParseException {
		Object name = stack.pop();
		
		if (name instanceof CharSequence) {
			Map<String, Object> map = getExpressionData().getCurrentComparison().getUserValues();
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
}
