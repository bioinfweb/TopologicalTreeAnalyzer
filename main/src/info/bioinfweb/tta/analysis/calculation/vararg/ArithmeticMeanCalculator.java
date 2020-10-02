package info.bioinfweb.tta.analysis.calculation.vararg;


import java.util.Iterator;

import org.nfunk.jep.ParseException;



public class ArithmeticMeanCalculator extends SumCalculator {
	@Override
	public String getName() {
		return "arithMean";
	}

	
	@Override
	public Double calculate(Iterator<Double> iterator) throws ParseException {
		return super.calculate(iterator) / (double)getCurrentNumberOfElements();
	}
}
