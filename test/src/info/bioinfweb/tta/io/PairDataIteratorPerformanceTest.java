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
package info.bioinfweb.tta.io;


import java.io.File;
import java.io.IOException;

import info.bioinfweb.tta.io.data.PairDataIterator;



public class PairDataIteratorPerformanceTest {
	public static void printTime(long start) {
		long time = System.currentTimeMillis() - start;
		long minutes = time / 60000;
		time -= minutes * 60000;
		long seconds = time / 1000;
		System.out.println(minutes + " m, " + seconds + " s");
	}
	
	
	public static void main(String[] args) throws IOException {
		PairDataIterator iterator = new PairDataIterator(
				new File("D:\\LocalUserData\\bstoe_01\\Projekte\\TTA\\Testdaten\\2020-12-01\\Cloutier_CNEE_TNT_boot_on_opt\\TopologicalPairData.txt"));
		
		try {
			long start = System.currentTimeMillis();
			int pos = 0;
			while (iterator.hasNext()) {
				iterator.next();
				
				pos++;
				if (pos % 1000000 == 0) {
					System.out.print(pos  + " elements read. ");
					printTime(start);
				}
			}
			
			System.out.print("Finished.");
			printTime(start);
		}
		finally {
			iterator.close();
		}
		
		// Took 2 m, 19 s on office desktop spinning disk.
	}
}
