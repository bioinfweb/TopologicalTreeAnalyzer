package info.bioinfweb.osrfilter.data.parameters;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;



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
}
