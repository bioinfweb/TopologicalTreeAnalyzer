package info.bioinfweb.osrfilter.analysis.calculation;


import java.util.Stack;

import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommandI;

import info.bioinfweb.osrfilter.analysis.UserExpressionData;



public abstract class AbstractFunction implements PostfixMathCommandI {
	private int curNumberOfParameters = 1;
	private UserExpressionData expressionData;  //TODO Possibly reference whole map here for phase 2.

  
	public abstract String getName();
	
	
	public AbstractFunction(UserExpressionData expressionData) {
		super();
		this.expressionData = expressionData;
	}


	@Override
	public boolean checkNumberOfParameters(int parameterCount) {
		return (getNumberOfParameters() == -1) || (parameterCount == getNumberOfParameters());
	}

	
	@Override
	public void setCurNumberOfParameters(int n) {
		curNumberOfParameters = n;
	}


	protected int getCurNumberOfParameters() {
		return curNumberOfParameters;
	}


	public UserExpressionData getExpressionData() {
		return expressionData;
	}
	
	
	@SuppressWarnings("rawtypes")
	protected abstract void doRun(Stack stack) throws ParseException;
	
	
	@SuppressWarnings({"rawtypes"})
	@Override
	public void run(Stack stack) throws ParseException {
		if (checkNumberOfParameters(getCurNumberOfParameters())) {
			doRun(stack);
		}
		else {
			throw new ParseException("Invalid number of parameters. This function must have one numeric parameter.");
		}
	}	
}
