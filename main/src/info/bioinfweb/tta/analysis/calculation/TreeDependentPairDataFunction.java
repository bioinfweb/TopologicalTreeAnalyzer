package info.bioinfweb.tta.analysis.calculation;


import java.util.Stack;

import org.nfunk.jep.ParseException;

import info.bioinfweb.tta.analysis.UserExpressionDataProvider;



public abstract class TreeDependentPairDataFunction<T> extends AbstractFunction {
	public TreeDependentPairDataFunction(UserExpressionDataProvider expressionData) {
		super(expressionData);
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
