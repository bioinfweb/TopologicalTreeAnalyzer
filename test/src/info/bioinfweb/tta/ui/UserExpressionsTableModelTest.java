/*
 * Topological Tree Analyzer - User defined topological analyses of large sets of phylogenetic trees. 
 * Copyright (C) 2020  Ben C. Stöver
 * <http://bioinfweb.info/TTA>
 * 
 * This file is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package info.bioinfweb.tta.ui;


import static org.junit.Assert.*;

import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.junit.Test;

import info.bioinfweb.tta.data.UserExpression;
import info.bioinfweb.tta.ui.UserExpressionsTableModel.ExpressionTarget;



public class UserExpressionsTableModelTest {
	private UserExpressionsTableModel createModel() {
		Map<String, UserExpression> map = new HashedMap<String, UserExpression>();
		map.put("treeExp1", new UserExpression(true, "terminals()"));
		map.put("treeExp2", new UserExpression(true, "2 * treeUserValue(\"treeExp1\")"));
		map.put("pairExp1", new UserExpression(false, "c()"));
		map.put("pairExp2", new UserExpression(false, "m()"));
		return new UserExpressionsTableModel(map);
	}
	
	
	@Test
	public void test_getValueAt_dimensions() {
		 UserExpressionsTableModel model = createModel();
		 assertEquals(3, model.getColumnCount());
		 assertEquals(4, model.getRowCount());
		 
		 assertEquals("pairExp1", model.getValueAt(0, 0));
		 assertEquals(ExpressionTarget.PAIR, model.getValueAt(0, 1));
		 assertEquals("c()", model.getValueAt(0, 2));
		 
		 assertEquals("pairExp2", model.getValueAt(1, 0));
		 assertEquals(ExpressionTarget.PAIR, model.getValueAt(1, 1));
		 assertEquals("m()", model.getValueAt(1, 2));

		 assertEquals("treeExp1", model.getValueAt(2, 0));
		 assertEquals(ExpressionTarget.TREE, model.getValueAt(2, 1));
		 assertEquals("terminals()", model.getValueAt(2, 2));
		 
		 assertEquals("treeExp2", model.getValueAt(3, 0));
		 assertEquals(ExpressionTarget.TREE, model.getValueAt(3, 1));
		 assertEquals("2 * treeUserValue(\"treeExp1\")", model.getValueAt(3, 2));
	}
	
	
	@Test
	public void test_setValueAt() {
		 UserExpressionsTableModel model = createModel();
		 
		 model.setValueAt("newName", 1, 0);  // Rename "pairExp2".
		 model.setValueAt(ExpressionTarget.TREE, 1, 1);  // Edit type of "pairExp1". (Note that the row indices have changed due to the previous edit.)
		 model.setValueAt("splits()", 1, 2);  // Edit expressions of "pairExp1".
		 
		 assertEquals(4, model.getRowCount());
		 
		 assertEquals("newName", model.getValueAt(0, 0));
		 assertEquals(ExpressionTarget.PAIR, model.getValueAt(0, 1));
		 assertEquals("m()", model.getValueAt(0, 2));
		 
		 assertEquals("pairExp1", model.getValueAt(1, 0));
		 assertEquals(ExpressionTarget.TREE, model.getValueAt(1, 1));
		 assertEquals("splits()", model.getValueAt(1, 2));

		 assertEquals("treeExp1", model.getValueAt(2, 0));
		 assertEquals(ExpressionTarget.TREE, model.getValueAt(2, 1));
		 assertEquals("terminals()", model.getValueAt(2, 2));

		 assertEquals("treeExp2", model.getValueAt(3, 0));
		 assertEquals(ExpressionTarget.TREE, model.getValueAt(3, 1));
		 assertEquals("2 * treeUserValue(\"treeExp1\")", model.getValueAt(3, 2));
		 
		 assertTrue(model.getExpressions().containsKey("newName"));
		 assertFalse(model.getExpressions().containsKey("pairExp2"));
	}
}
