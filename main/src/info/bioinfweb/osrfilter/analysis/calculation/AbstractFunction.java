package info.bioinfweb.osrfilter.analysis.calculation;


import org.nfunk.jep.function.PostfixMathCommand;

import info.bioinfweb.osrfilter.analysis.UserExpressionData;



public abstract class AbstractFunction extends PostfixMathCommand {
	private UserExpressionData expressionData;  //TODO Possibly reference whole map here for phase 2.

  
	public abstract String getName();
	
	
	public AbstractFunction(UserExpressionData expressionData) {
		super();
		this.expressionData = expressionData;
	}


	public UserExpressionData getExpressionData() {
		return expressionData;
	}
}
