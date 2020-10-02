package info.bioinfweb.tta.analysis.calculation.vararg;


import java.util.Iterator;

import org.nfunk.jep.ParseException;



public class HarmonicMeanCalculator implements VarArgCalculator<Double> {
	@Override
	public String getName() {
		return "harmMean";
	}


	@Override
	public Double calculate(Iterator<Double> iterator) throws ParseException {
		int numberOfElements = 1;
		if (iterator.hasNext()) {
			Double result = 1.0 / iterator.next();
			while (iterator.hasNext()) {
				result += 1.0 / iterator.next();
				numberOfElements++;
			}
			return ((double)numberOfElements) / result;
		}
		else {
			throw new ParseException("Invalid parameter count. At least one parameter needs to be specified.");
		}
	}
}
