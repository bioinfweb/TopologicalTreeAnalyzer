package info.bioinfweb.osrfilter.analysis;


import static org.junit.Assert.*;


import org.junit.Test;
import org.nfunk.jep.ParseException;



public class UserExpressionsManagerTest {
	@Test
	public void test_checkExpressions_order() throws ParseException {
		UserExpressionsManager manager = new UserExpressionsManager();
		manager.addExpression(false, "exp3", "userValue(\"exp2\")");
		manager.addExpression(false, "exp1", "userValue(\"exp0\")");
		manager.addExpression(false, "exp2", "userValue(\"exp0\") + userValue(\"exp1\")");
		manager.addExpression(false, "exp0", "splits(0)");

		manager.checkExpressions();
		
		for (int i = 0; i < 4; i++) {
			assertEquals("exp" + i, manager.getExpressionOrder().get(i));
		}
	}

	
	//analyzer.getUserExpressions().put("testSplitsA", "splits(0)");
	//analyzer.getUserExpressions().put("testSplitsB", "splits(1)");
	//analyzer.getUserExpressions().put("testC", "c(0) + c(1)");
	//analyzer.getUserExpressions().put("testN", "n(0) + n(1)");
	//analyzer.getUserExpressions().put("testTerminals", "terminals(0) + terminals(1)");
	//analyzer.getUserExpressions().put("testMSharedTerminals", "m() - sharedTerminals()");
	//analyzer.getUserExpressions().put("testID", "id(0) + \" \" + id(1)");
	//analyzer.getUserExpressions().put("testUserValue", "userValue(\"testC\")");
}
