package info.bioinfweb.tta.analysis.calculation;


import org.nfunk.jep.ParseException;

import info.bioinfweb.tta.analysis.UserExpressionDataProvider;



public class PairUserValueFunction extends AbstractPairUserValueFunction implements UserValueFunction {
	public PairUserValueFunction(UserExpressionDataProvider expressionData) {
		super(expressionData);
	}

	
	@Override
	public String getName() {
		return "pairUserValue";
	}

	
	@Override
	protected Object calculateValue(CharSequence userValueName) throws ParseException {
		if (!getExpressionData().isTreeExpression()) {
			return getUserValue(userValueName, getExpressionData().getCurrentComparisonData().getUserValues());
		}
		else {
			throw new ParseException("Access to current pair user values is only possible in pair expressions. (You can use treeUserValue() in tree expressions.)");
		}
	}
}
