package info.bioinfweb.tta.data;


import java.io.File;



public class TreeIdentifier {
	private File file;
	private String id;
	private String name;
	
	
	public TreeIdentifier(File file, String id, String name) {
		super();
		setFile(file);
		setID(id);
		setName(name);
	}


	public File getFile() {
		return file;
	}
	
	
	public void setFile(File file) {
		if (file == null) {
			throw new IllegalArgumentException("file must not be null.");
		}
		else {
			this.file = file;
		}
	}
	
	
	public String getID() {
		return id;
	}
	
	
	public void setID(String id) {
		if (id == null) {
			throw new IllegalArgumentException("id must not be null.");
		}
		else {
			this.id = id;
		}
	}


	/**
	 * Returns the name/label of the tree that was found in the file.
	 * <p>
	 * Note that this property is not considered by {@link #hashCode()} and {@link #equals(Object)}.
	 * 
	 * @return the label of the tree or {@code null} if none was specified
	 */
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((file == null) ? 0 : file.getAbsoluteFile().hashCode());  // Note that this condition is different from the auto-generated method.
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
		} else if (!file.getAbsoluteFile().equals(other.file.getAbsoluteFile()))  // Note that this condition is different from the auto-generated method.
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
