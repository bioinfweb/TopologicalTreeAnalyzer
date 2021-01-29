/*
 * Topological Tree Analyzer - User defined topological analyses of large sets of phylogenetic trees. 
 * Copyright (C) 2020-2021  Ben C. Stöver
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
package info.bioinfweb.tta.io.parameters;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import info.bioinfweb.commons.Math2;
import info.bioinfweb.tta.data.parameters.RuntimeParameters;



public class RuntimeParameterAdapterTest {
	@Test
	public void test_memoryUnmarshal() throws Exception {
		RuntimeParameterAdapter.Memory adapter = new RuntimeParameterAdapter.Memory();
		
		assertEquals(1234, adapter.unmarshal("1234").longValue());
		assertEquals(2 * 1024, adapter.unmarshal("2K").longValue());
		assertEquals(2 * 1024, adapter.unmarshal("2k").longValue());
		assertEquals(2 * Math2.longPow(1024, 2), adapter.unmarshal("2M").longValue());
		assertEquals(2 * Math2.longPow(1024, 2), adapter.unmarshal("2m").longValue());
		assertEquals(5 * Math2.longPow(1024, 3), adapter.unmarshal("5G").longValue());
		assertEquals(2 * Math2.longPow(1024, 3), adapter.unmarshal("2g").longValue());
		assertEquals(2 * Math2.longPow(1024, 4), adapter.unmarshal("2T").longValue());
		assertEquals(2 * Math2.longPow(1024, 4), adapter.unmarshal("2t").longValue());
		assertEquals(7 * Math2.longPow(1024, 5), adapter.unmarshal("7P").longValue());
		assertEquals(2 * Math2.longPow(1024, 5), adapter.unmarshal("2p").longValue());
	}
	
	
	@Test
	public void test_max() throws Exception {
		RuntimeParameterAdapter adapter = new RuntimeParameterAdapter.Memory();
		assertEquals(RuntimeParameters.MAXIMUM, adapter.unmarshal(RuntimeParameterAdapter.MAX_CONSTANT).longValue());
		assertEquals(RuntimeParameters.MAXIMUM, adapter.unmarshal(RuntimeParameterAdapter.MAX_CONSTANT.toLowerCase()).longValue());
		assertEquals(RuntimeParameters.MAXIMUM, adapter.unmarshal(RuntimeParameterAdapter.MAX_CONSTANT.toUpperCase()).longValue());
		assertEquals(RuntimeParameterAdapter.MAX_CONSTANT, adapter.marshal(RuntimeParameters.MAXIMUM));
		
		adapter = new RuntimeParameterAdapter.Threads();
		assertEquals(RuntimeParameters.MAXIMUM, adapter.unmarshal(RuntimeParameterAdapter.MAX_CONSTANT).longValue());
		assertEquals(RuntimeParameters.MAXIMUM, adapter.unmarshal(RuntimeParameterAdapter.MAX_CONSTANT.toLowerCase()).longValue());
		assertEquals(RuntimeParameters.MAXIMUM, adapter.unmarshal(RuntimeParameterAdapter.MAX_CONSTANT.toUpperCase()).longValue());
		assertEquals(RuntimeParameterAdapter.MAX_CONSTANT, adapter.marshal(RuntimeParameters.MAXIMUM));
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public void test_memoryExcetion0() throws Exception {
		new RuntimeParameterAdapter.Memory().marshal(0l);
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public void test_memoryExcetionNegative() throws Exception {
		new RuntimeParameterAdapter.Memory().marshal(-12l);
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public void test_threadsExcetion0() throws Exception {
		new RuntimeParameterAdapter.Threads().marshal(0l);
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public void test_threadsExcetionNegative() throws Exception {
		new RuntimeParameterAdapter.Threads().marshal(-12l);
	}
}
