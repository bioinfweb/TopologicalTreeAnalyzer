package info.bioinfweb.osrfilter.analysis.calculation;


import java.util.Stack;

import org.nfunk.jep.ParseException;

import info.bioinfweb.osrfilter.analysis.UserExpressionData;



public abstract class TreeIndependentFunction<T> extends AbstractFunction {
	public TreeIndependentFunction(UserExpressionData expressionData) {
		super(expressionData);
	}

	
	@Override
	public int getNumberOfParameters() {
		return 0;
	}

	
	protected abstract T getValue();
	
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void doRun(Stack stack) throws ParseException {
		stack.push(getValue());
	}
}
