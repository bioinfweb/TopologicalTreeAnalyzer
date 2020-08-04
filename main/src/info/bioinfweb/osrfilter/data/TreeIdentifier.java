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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TreeIdentifier other = (TreeIdentifier) obj;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "\"" + file.toString() + "\":" + id;
	}
}
