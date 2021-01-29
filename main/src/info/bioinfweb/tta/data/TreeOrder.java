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
package info.bioinfweb.tta.data;


import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;



public class TreeOrder implements Iterable<TreeIdentifier> {
	private List<TreeIdentifier> identifierByIndexList;
	private Map<TreeIdentifier, Integer> indexByIdentifierMap;
	
	
	public TreeOrder(List<TreeIdentifier> inputOrder) {
		super();
		indexByIdentifierMap = new HashedMap<>();
		setInputOrder(inputOrder);
	}
	
	
	public TreeIdentifier identifierByIndex(int index) {
		return identifierByIndexList.get(index);
	}
	
	
	public int indexByIdentifier(TreeIdentifier identifier) {
		return indexByIdentifierMap.get(identifier);
	}


	public boolean isEmpty() {
		return identifierByIndexList.isEmpty();
	}


	public Iterator<TreeIdentifier> iterator() {
		return identifierByIndexList.iterator();
	}


	public int size() {
		return identifierByIndexList.size();
	}
	
	
	public void setInputOrder(List<TreeIdentifier> list) {
		identifierByIndexList = Collections.unmodifiableList(list);
		
		indexByIdentifierMap.clear();
		for (int i = 0; i < list.size(); i++) {
			indexByIdentifierMap.put(list.get(i), i);
		}
	}
}
