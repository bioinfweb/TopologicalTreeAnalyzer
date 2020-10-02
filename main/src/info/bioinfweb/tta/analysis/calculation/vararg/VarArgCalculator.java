package info.bioinfweb.osrfilter.analysis.calculation.vararg;

import java.util.Iterator;

import org.nfunk.jep.ParseException;

public interface VarArgCalculator<T> {

	String getName();

	T calculate(Iterator<T> iterator) throws ParseException;

}