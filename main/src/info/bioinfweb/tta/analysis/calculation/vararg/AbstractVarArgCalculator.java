package info.bioinfweb.tta.analysis.calculation.vararg;


import java.util.Iterator;

import org.nfunk.jep.ParseException;



public abstract class AbstractVarArgCalculator<T> implements VarArgCalculator<T> {
	private int currentNumberOfElements = 0;
	
	
	protected int getCurrentNumberOfElements() {
		return currentNumberOfElements;
	}


	@Override
	public abstract String getName();


	protected abstract T calculatePair(T value1, T value2) throws ParseException;

	
	@Override
	public T calculate(Iterator<T> iterator) throws ParseException {
		currentNumberOfElements = 0;
		if (iterator.hasNext()) {
			currentNumberOfElements++;
			T result = iterator.next();
			while (iterator.hasNext()) {
				currentNumberOfElements++;
				result = calculatePair(result, iterator.next());
			}
			return result;
		}
		else {
			throw new ParseException("Invalid parameter count. At least one parameter needs to be specified.");
		}
	}
}
