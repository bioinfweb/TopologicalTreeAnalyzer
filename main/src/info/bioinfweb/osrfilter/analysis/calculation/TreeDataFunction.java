package info.bioinfweb.osrfilter.analysis.calculation;


import java.util.Stack;

import org.nfunk.jep.ParseException;

import info.bioinfweb.osrfilter.analysis.UserExpressionDataProvider;



public abstract class TreeDataFunction extends AbstractFunction {
	public TreeDataFunction(UserExpressionDataProvider expressionData) {
		super(expressionData);
		setCurNumberOfParameters(1);
	}


	@Override
	public int getNumberOfParameters() {
		return 1;
	}

	
	protected abstract String getValue(int index);
	
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void run(Stack stack) throws ParseException {
		Object sourceTree = stack.pop();
		
		if (sourceTree instanceof Number) {
			stack.push(getValue(((Number)sourceTree).intValue()));
		}
		else {
			throw new ParseException("Invalid parameter type. This function must have one numeric parameter.");
		}
	}
}