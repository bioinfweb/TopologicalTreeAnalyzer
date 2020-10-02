package info.bioinfweb.tta.analysis.calculation;


import java.util.Map;
import java.util.Stack;

import org.nfunk.jep.ParseException;

import info.bioinfweb.tta.analysis.UserExpressionDataProvider;



public abstract class AbstractPairUserValueFunction extends AbstractFunction implements UserValueFunction {
	public AbstractPairUserValueFunction(UserExpressionDataProvider expressionData) {
		super(expressionData);
	}

	
	@Override
	public int getNumberOfParameters() {
		return 1;
	}

	
	public static Object getUserValue(CharSequence userValueName, Map<String, Object> map) throws ParseException {
		if (map.containsKey(userValueName)) {
			return map.get(userValueName);
		}
		else {
			throw new ParseException("Invalid parameter. No pair user value \"" + userValueName + 
					"\" could be found. It is either undefined or a tree user value.");
		}
	}
	

	protected abstract Object calculateValue(CharSequence userValueName) throws ParseException;
	

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void run(Stack stack) throws ParseException {
		Object name = stack.pop();
		if (name instanceof CharSequence) {
			stack.push(calculateValue((CharSequence)name));
		}
		else {
			throw new ParseException("Invalid parameter type. This function must have one textual parameter defining the referenced user expression.");
		}
	}
}
