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
package info.bioinfweb.tta.data.database;



public interface DatabaseConstants {
	// Table names:
	public static final String TABLE_TREE_DATA = "treeData";
	public static final String TABLE_PAIR_DATA = "pairData";
	public static final String TABLE_TREE_USER_DATA = "treeUserData";
	public static final String TABLE_PAIR_USER_DATA = "pairUserData";

	// Index names:
	public static final String INDEX_TREE_DATA = "tree";
	public static final String INDEX_PAIR_DATA_PAIR = "treePair";
	public static final String INDEX_PAIR_DATA_TREE_B = "treeB";
	public static final String INDEX_PREFIX_USER_DATA = "u_";

	// Tree columns:
	public static final String COLUMN_TREE_INDEX = "tree";
	public static final String COLUMN_TERMINALS = "terminals";
	public static final String COLUMN_SPLITS = "splits";
	
	// Pair columns:
	public static final String COLUMN_TREE_INDEX_A = "treeA";
	public static final String COLUMN_TREE_INDEX_B = "treeB";
	public static final String COLUMN_MATCHING_SPLITS = "matchingSplits";
	public static final String COLUMN_CONFLICTING_SPLITS_AB = "conflictingSplitsAB";
	public static final String COLUMN_CONFLICTING_SPLITS_BA = "conflictingSplitsBA";
	public static final String COLUMN_NON_MATCHING_SPLITS_AB = "nonMatchingSplitsAB";
	public static final String COLUMN_NON_MATCHING_SPLITS_BA = "nonMatchingSplitsBA";
	public static final String COLUMN_SHARED_TERMINALS = "sharedTerminals";

	// User data columns:
	public static final String COLUMN_PREFIX_USER_DATA = "u_";
}
