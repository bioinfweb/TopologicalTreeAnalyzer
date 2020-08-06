package info.bioinfweb.osrfilter.analysis.calculation;


import java.util.Stack;

import org.nfunk.jep.ParseException;



public class CFunction extends AbstractFunction {
	@Override
	public String getName() {
		return "c";
	}


	@Override
	public int getNumberOfParameters() {
		return 1;
	}

	
	@Override
	public void run(Stack stack) throws ParseException {
		if (checkNumberOfParameters(getCurNumberOfParameters())) {
			Object sourceTree = stack.pop();
			
			if (sourceTree instanceof Number) {
				Integer result;
				if (((Number)sourceTree).intValue() == 0) {
					result = new Integer(getCurrentComparison().getConflictingSplitsAB());
				}
				else {
					result = new Integer(getCurrentComparison().getConflictingSplitsBA());
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
