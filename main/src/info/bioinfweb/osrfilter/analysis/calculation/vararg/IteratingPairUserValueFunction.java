package info.bioinfweb.osrfilter.analysis.calculation.vararg;


import org.nfunk.jep.ParseException;

import info.bioinfweb.osrfilter.analysis.UserExpressionDataProvider;
import info.bioinfweb.osrfilter.analysis.calculation.AbstractPairUserValueFunction;



@SuppressWarnings("rawtypes")
public class IteratingPairUserValueFunction extends AbstractPairUserValueFunction {
	private VarArgCalculator calculator;
	
	
	public IteratingPairUserValueFunction(UserExpressionDataProvider expressionData, VarArgCalculator calculator) {
		super(expressionData);
		if (calculator != null) {
			this.calculator = calculator;
		}
		else {
			throw new IllegalArgumentException("calculator must not be null.");
		}
	}

	
	@Override
	public String getName() {
		return calculator.getName() + "OfPairUserValues";
	}

	
	@SuppressWarnings("unchecked")
	@Override
	protected Object calculateValue(CharSequence userValueName) throws ParseException {
		if (getExpressionData().isTreeExpression()) {
			return calculator.calculate(new PairUserValuesIterator(userValueName, getExpressionData().getAnalysesData().getComparisonMap(), getExpressionData().getTreeIdentifier(0)));
		}
		else {
			throw new ParseException("Iterating over all pair user values is only possible in tree expressions.");
		}
	}
}
