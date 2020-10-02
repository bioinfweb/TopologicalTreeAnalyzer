package info.bioinfweb.tta.analysis.calculation.vararg;


import org.nfunk.jep.ParseException;



public class MaxCalculator extends AbstractVarArgCalculator<Double> {
	@Override
	public String getName() {
		return "max";
	}

	
	@Override
	protected Double calculatePair(Double value1, Double value2) throws ParseException {
		return Math.max(value1, value2);
	}
}
