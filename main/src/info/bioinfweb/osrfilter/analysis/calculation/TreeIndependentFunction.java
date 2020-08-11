package info.bioinfweb.osrfilter.analysis.calculation;


import java.util.Stack;

import org.nfunk.jep.ParseException;

import info.bioinfweb.osrfilter.analysis.UserExpressionData;



public abstract class TreeIndependentFunction<T> extends AbstractFunction {
	public TreeIndependentFunction(UserExpressionData expressionData) {
		super(expressionData);
		setCurNumberOfParameters(0);
	}

	
	@Override
	public int getNumberOfParameters() {
		return 0;
	}

	
	protected abstract T getValue();
	
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void run(Stack stack) throws ParseException {
		stack.push(getValue());
	}
}
