package info.bioinfweb.osrfilter.analysis.calculation.vararg;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.nfunk.jep.ParseException;



public class MedianCalculator implements VarArgCalculator<Double> {
	@Override
	public String getName() {
		return "median";
	}


	@Override
	public Double calculate(Iterator<Double> iterator) throws ParseException {
		List<Double> elements = new ArrayList<Double>();
		while (iterator.hasNext()) {
			elements.add(iterator.next());
		}
		if (!elements.isEmpty()) {
			Collections.sort(elements);
			return elements.get(elements.size() / 2);
		}
		else {
			throw new ParseException("Invalid parameter count. At least one parameter needs to be specified.");
		}
	}
}
