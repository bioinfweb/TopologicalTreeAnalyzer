package info.bioinfweb.osrfilter.analysis.calculation.vararg;


import org.nfunk.jep.ParseException;



public class SumCalculator extends AbstractVarArgCalculator<Double> {
	@Override
	public String getName() {
		return "sum";
	}
	

	@Override
	protected Double calculatePair(Double value1, Double value2) throws ParseException {
		return value1 + value2;
	}
}
