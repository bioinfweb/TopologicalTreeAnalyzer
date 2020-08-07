package info.bioinfweb.osrfilter.analysis.calculation;


import java.util.Stack;

import org.nfunk.jep.ParseException;

import info.bioinfweb.osrfilter.analysis.UserExpressionData;



public class CFunction extends AbstractFunction {
	public CFunction(UserExpressionData expressionData) {
		super(expressionData);
	}


	@Override
	public String getName() {
		return "c";
	}


	@Override
	public int getNumberOfParameters() {
		return 1;
	}

	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void run(Stack stack) throws ParseException {
		if (checkNumberOfParameters(getCurNumberOfParameters())) {
			Object sourceTree = stack.pop();
			
			if (sourceTree instanceof Number) {
				Double result;
				if (((Number)sourceTree).intValue() == 0) {
					result = new Double(getExpressionData().getCurrentComparison().getConflictingSplitsAB());
				}
				else {
					result = new Double(getExpressionData().getCurrentComparison().getConflictingSplitsBA());
				}
				stack.push(result);
			}
			else {
				throw new ParseException("Invalid parameter type. This function must have one numeric parameter.");
			}
		}
		else {
			throw new ParseException("Invalid number of parameters. This function must have one numeric parameter.");
		}
	}
}
