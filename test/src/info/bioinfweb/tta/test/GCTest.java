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
package info.bioinfweb.tta.test;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class GCTest {
	@SuppressWarnings("unused")
	private static void freeableObjects() {
		for (int i = 0; i < 100000000; i++) {
			List<String> list = new ArrayList<String>(100);
			for (int j = 0; j < 100; j++) {
				list.add("entry " + j);
			}
			if (i % 100000 == 0) {
				System.out.println(Runtime.getRuntime().totalMemory());
			}
		}
	}
	
	
	@SuppressWarnings("unused")
	private static void smallReferencedObjects() {
    HashMap<Integer, String> myMap = new HashMap<>();
    for (int i = 0; i < 100000000; i++) {
        myMap.put(i, String.valueOf(i));
    }       
	}
	

	@SuppressWarnings("unused")
	private static void bigReferencedObjects() {
    HashMap<Integer, int[]> myMap = new HashMap<>();
    for (int i = 0; i < 100000000; i++) {
        myMap.put(i, new int[100000]);
    }       
	}
	

	public static void main(String[] args) {
		freeableObjects();  // no error
		//smallReferencedObjects();  // java.lang.OutOfMemoryError: GC overhead limit exceeded
		//bigReferencedObjects();  // java.lang.OutOfMemoryError: Java heap space
	}
}
