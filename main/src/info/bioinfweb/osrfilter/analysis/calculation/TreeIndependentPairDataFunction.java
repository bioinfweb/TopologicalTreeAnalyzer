package info.bioinfweb.osrfilter.analysis.calculation;


import java.util.Stack;

import org.nfunk.jep.ParseException;

import info.bioinfweb.osrfilter.analysis.UserExpressionDataProvider;



public abstract class TreeIndependentPairDataFunction<T> extends AbstractFunction {
	public TreeIndependentPairDataFunction(UserExpressionDataProvider expressionData) {
		super(expressionData);
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
