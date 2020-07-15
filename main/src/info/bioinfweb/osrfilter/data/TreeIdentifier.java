package info.bioinfweb.osrfilter.data;


import java.io.File;



public class TreeIdentifier {
	private File file;
	private String id;
	
	
	public TreeIdentifier(File file, String id) {
		super();
		this.file = file;
		this.id = id;
	}


	public File getFile() {
		return file;
	}
	
	
	public void setFile(File file) {
		this.file = file;
	}
	
	
	public String getID() {
		return id;
	}
	
	
	public void setID(String id) {
		this.id = id;
	}
}
