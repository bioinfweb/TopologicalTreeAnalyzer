package info.bioinfweb.osrfilter.analysis.calculation;


import org.nfunk.jep.function.PostfixMathCommand;

import info.bioinfweb.osrfilter.analysis.UserExpressionDataProvider;



public abstract class AbstractFunction extends PostfixMathCommand {
	private UserExpressionDataProvider expressionData;  //TODO Possibly reference whole map here for phase 2.

  
	public abstract String getName();
	
	
	public AbstractFunction(UserExpressionDataProvider expressionData) {
		super();
		this.expressionData = expressionData;
	}


	public UserExpressionDataProvider getExpressionData() {
		return expressionData;
	}
}
