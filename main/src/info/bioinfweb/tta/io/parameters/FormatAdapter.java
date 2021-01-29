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


import java.io.IOException;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import info.bioinfweb.jphyloio.formats.JPhyloIOFormatIDs;
import info.bioinfweb.tta.io.TreeWriter;



public class FormatAdapter extends XmlAdapter<String, String> {
	@Override
	public String marshal(String jPhyloIOFormatID) throws Exception {
		if (jPhyloIOFormatID != null) {
			if (jPhyloIOFormatID.startsWith(JPhyloIOFormatIDs.FORMAT_ID_PREFIX)) {
				return jPhyloIOFormatID.substring(JPhyloIOFormatIDs.FORMAT_ID_PREFIX.length());
			}
			else {
				throw new IOException("The specified ID \"" + jPhyloIOFormatID + "\" is not a valid JPhyloIO format ID. (Such IDs always start with \"" + 
						JPhyloIOFormatIDs.FORMAT_ID_PREFIX + "\".)");
			}
		}
		else {
			return null;
		}
	}

	
	@Override
	public String unmarshal(String xmlFormatID) throws Exception {
		String result = JPhyloIOFormatIDs.FORMAT_ID_PREFIX + xmlFormatID;
		if (TreeWriter.READER_WRITER_FACTORY.getFormatIDsSet().contains(result)) {
			return result;
		}
		else {
			throw new IOException("The specified tree format \"" + xmlFormatID + "\" is not supported.");
		}
	}
}
