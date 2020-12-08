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
package info.bioinfweb.tta.data.database;



public interface DatabaseConstants {
	public static final int MAX_ITERATOR_GROUP_SIZE = 1024;
	
	public static final String TABLE_TREE_DATA = "treeData";
	public static final String TABLE_PAIR_DATA = "pairData";

	public static final String INDEX_PAIR_DATA = "treePair";

	public static final String COLUMN_TREE_INDEX_A = "treeA";
	public static final String COLUMN_TREE_INDEX_B = "treeB";
	public static final String COLUMN_MATCHING_SPLITS = "matchingSplits";
	public static final String COLUMN_CONFLICTING_SPLITS_AB = "conflictingSplitsAB";
	public static final String COLUMN_CONFLICTING_SPLITS_BA = "conflictingSplitsBA";
	public static final String COLUMN_NON_MATCHING_SPLITS_AB = "nonMatchingSplitsAB";
	public static final String COLUMN_NON_MATCHING_SPLITS_BA = "nonMatchingSplitsBA";
	public static final String COLUMN_SHARED_TERMINALS = "sharedTerminals";
}
