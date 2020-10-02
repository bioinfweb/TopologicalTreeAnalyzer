package info.bioinfweb.osrfilter.analysis.calculation.vararg;


import java.util.Stack;

import org.nfunk.jep.ParseException;

import info.bioinfweb.osrfilter.analysis.UserExpressionDataProvider;
import info.bioinfweb.osrfilter.analysis.calculation.AbstractFunction;



public class VarArgFunction<T> extends AbstractFunction {
	private VarArgCalculator<T> calculator;

	
	public VarArgFunction(UserExpressionDataProvider expressionData, VarArgCalculator<T> calculator) {
		super(expressionData);
		this.calculator = calculator;
	}


	@Override
	public String getName() {
		return calculator.getName();
	}


	@Override
	public int getNumberOfParameters() {
		return -1;
	}


	@Override
	public boolean checkNumberOfParameters(int numberOfParameters) {
		return (numberOfParameters > 0);
	}


	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public void run(Stack stack) throws ParseException {
		stack.push(calculator.calculate(new VarArgStackIterator<T>(stack, curNumberOfParameters)));
	}
}
