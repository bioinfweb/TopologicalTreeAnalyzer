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

	
	@Test
	public void test_checkExpressions_orderMultipleReferences() throws ParseException {
		UserExpressionsManager manager = new UserExpressionsManager();
		manager.addExpression(false, "ref0", "2 * userValue(\"referenced\")");
		manager.addExpression(false, "ref1", "userValue(\"referenced\") - 1");
		manager.addExpression(false, "referenced", "splits(0)");
		manager.addExpression(false, "ref2", "userValue(\"referenced\")");

		manager.checkExpressions();
		
		assertEquals("referenced", manager.getExpressionOrder().get(0));
		for (int i = 1; i < 4; i++) {
			assertTrue(manager.getExpressionOrder().get(i).startsWith("ref"));
		}
	}

	
	@Test(expected=ParseException.class)
	public void test_checkExpressions_circularReferences() throws ParseException {
		UserExpressionsManager manager = new UserExpressionsManager();
		manager.addExpression(false, "exp0", "userValue(\"exp2\")");
		manager.addExpression(false, "exp1", "userValue(\"exp0\")");
		manager.addExpression(false, "exp2", "userValue(\"exp1\")");

		manager.checkExpressions();
	}

	
	@Test(expected=ParseException.class)
	public void test_checkExpressions_invalidParameterCount() throws ParseException {
		UserExpressionsManager manager = new UserExpressionsManager();
		manager.addExpression(false, "exp0", "splits(0, 18)");
		manager.checkExpressions();
	}

	
	@Test(expected=ParseException.class)
	public void test_checkExpressions_invalidParameterType() throws ParseException {
		UserExpressionsManager manager = new UserExpressionsManager();
		manager.addExpression(false, "exp0", "splits(\"A\")");
		manager.checkExpressions();
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
