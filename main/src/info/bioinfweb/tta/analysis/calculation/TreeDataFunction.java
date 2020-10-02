package info.bioinfweb.tta.analysis.calculation;


import java.util.Stack;

import org.nfunk.jep.ParseException;

import info.bioinfweb.tta.analysis.UserExpressionDataProvider;



public abstract class TreeDataFunction<T> extends AbstractFunction {
	public TreeDataFunction(UserExpressionDataProvider expressionData) {
		super(expressionData);
	}


	@Override
	public int getNumberOfParameters() {
		return getExpressionData().isTreeExpression() ? 0 : 1;
		//TODO Where can a meaningful exception message be thrown if the wrong (tree/pair) version/parameter count of the method is used?
		//     It could all be done if -1 is returned here. If no other way is found, this would be an option.
	}

	
	protected abstract T getValue(int index);
	
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void run(Stack stack) throws ParseException {
		int index = 0;
		if (!getExpressionData().isTreeExpression()) {
			Object sourceTree = stack.pop();
			if (sourceTree instanceof Number) {
				index = ((Number)sourceTree).intValue();
			}
			else {
				throw new ParseException("Invalid parameter type. This function must have one numeric parameter when used to calculate pair data.");
			}
		}
		stack.push(getValue(index));
	}
}