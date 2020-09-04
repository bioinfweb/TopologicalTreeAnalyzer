package info.bioinfweb.osrfilter.io.parameters;


import java.io.IOException;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import info.bioinfweb.jphyloio.formats.JPhyloIOFormatIDs;
import info.bioinfweb.osrfilter.io.TreeWriter;



public class FormatAdapter extends XmlAdapter<String, String> {
	@Override
	public String marshal(String jPhyloIOFormatID) throws Exception {
		return jPhyloIOFormatID.substring(JPhyloIOFormatIDs.FORMAT_ID_PREFIX.length());
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
