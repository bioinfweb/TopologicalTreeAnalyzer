package info.bioinfweb.osrfilter.data.parameters;


import java.io.File;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import info.bioinfweb.commons.io.IOUtils;
import info.bioinfweb.osrfilter.io.treeiterator.OptionalLoadingTreeIterator;



@XmlAccessorType(XmlAccessType.FIELD)
public abstract class ReferenceTreeDefinition {
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class IDReferenceTreeDefinition extends ReferenceTreeDefinition {
		@XmlAttribute
		private String id;

		@SuppressWarnings("unused")
		private IDReferenceTreeDefinition() {}  // For JAXB.
		
		public IDReferenceTreeDefinition(String file, String id) {
			super(file);
			this.id = id;
		}
		
		public String getID() {
			return id;
		}

		@Override
		protected boolean checkTree(String id, String label, int indexInFile) {
			return getID().equals(id);
		}
	}
	
	
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class NameReferenceTreeDefinition extends ReferenceTreeDefinition {
		@XmlAttribute
		private String name;
		
		@SuppressWarnings("unused")
		private NameReferenceTreeDefinition() {}  // For JAXB.
		
		public NameReferenceTreeDefinition(String file, String name) {
			super(file);
			this.name = name;
		}
		
		public String getName() {
			return name;
		}

		@Override
		protected boolean checkTree(String id, String label, int indexInFile) {
			return getName().equals(label);
		}
	}
	
	
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class IndexReferenceTreeDefinition extends ReferenceTreeDefinition {
		@XmlAttribute
		private int index;
		
		@SuppressWarnings("unused")
		private IndexReferenceTreeDefinition() {}  // For JAXB.
		
		public IndexReferenceTreeDefinition(String file, int index) {
			super(file);
			this.index = index;
		}
		
		public int getIndex() {
			return index;
		}

		@Override
		protected boolean checkTree(String id, String label, int indexInFile) {
			return getIndex() == indexInFile;
		}
	}
	
	
	@XmlAttribute
	private String file;

	
	private ReferenceTreeDefinition() {}  // For JAXB.
	
	
	public ReferenceTreeDefinition(String file) {
		super();
		this.file = file;
	}

	
	public String getFile() {
		return file;
	}
	
	
	public String getAbsolutePath(File baseDirectory) {
		return IOUtils.absoluteFilePath(new File(getFile()), baseDirectory);
	}
	
	
	protected abstract boolean checkTree(String id, String label, int indexInFile);
	
	
	public OptionalLoadingTreeIterator.TreeSelector createTreeSelector(File baseDirectory) {
		return new OptionalLoadingTreeIterator.TreeSelector() {
			@Override
			public boolean selectTree(File file, String id, String label, int indexInFile) {
				return file.getAbsolutePath().equals(getAbsolutePath(baseDirectory)) && checkTree(id, label, indexInFile);
			}
		};
	}
}
