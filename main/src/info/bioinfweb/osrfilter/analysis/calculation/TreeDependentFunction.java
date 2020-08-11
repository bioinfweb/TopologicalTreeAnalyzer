package info.bioinfweb.osrfilter.analysis.calculation;


import java.util.Stack;

import org.nfunk.jep.ParseException;

import info.bioinfweb.osrfilter.analysis.UserExpressionData;



public abstract class TreeDependentFunction<T> extends AbstractFunction {
	public TreeDependentFunction(UserExpressionData expressionData) {
		super(expressionData);
		setCurNumberOfParameters(1);
	}


	@Override
	public int getNumberOfParameters() {
		return 1;
	}

	
	protected abstract T getValueAB();
	
	
	protected abstract T getValueBA();
	
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void run(Stack stack) throws ParseException {
		Object sourceTree = stack.pop();
		
		if (sourceTree instanceof Number) {
			T result;
			if (((Number)sourceTree).intValue() == 0) {
				result = getValueAB();
			}
			else {
				result = getValueBA();
			}
			stack.push(result);
		}
		else {
			throw new ParseException("Invalid parameter type. This function must have one numeric parameter.");
		}
	}
}
