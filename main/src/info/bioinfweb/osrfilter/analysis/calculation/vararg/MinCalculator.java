package info.bioinfweb.osrfilter.analysis.calculation.vararg;


import org.nfunk.jep.ParseException;



public class MinCalculator extends AbstractVarArgCalculator<Double> {
	@Override
	public String getName() {
		return "min";
	}

	
	@Override
	protected Double calculatePair(Double value1, Double value2) throws ParseException {
		return Math.min(value1, value2);
	}
}
