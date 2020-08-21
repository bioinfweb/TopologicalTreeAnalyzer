package info.bioinfweb.osrfilter.analysis.calculation.vararg;


import java.util.Iterator;

import org.nfunk.jep.ParseException;



public class GeometicMeanCalculator extends ProductCalculator {
	@Override
	public String getName() {
		return "geomMean";
	}

	
	@Override
	public Double calculate(Iterator<Double> iterator) throws ParseException {
		return Math.sqrt(super.calculate(iterator));
	}
}
