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
package info.bioinfweb.tta.data.parameters;


import java.io.File;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import info.bioinfweb.commons.io.IOUtils;
import info.bioinfweb.tta.io.treeiterator.TreeSelector;



@XmlAccessorType(XmlAccessType.FIELD)
public abstract class ReferenceTreeDefinition {
	public static enum ReferenceType {
		ID,
		NAME,
		INDEX;
		
		
		public Class<? extends ReferenceTreeDefinition> getDefinitionClass() {
			switch (this) {
				case ID:
					return IDReferenceTreeDefinition.class;
				case NAME:
					return NameReferenceTreeDefinition.class;
				case INDEX:
					return IndexReferenceTreeDefinition.class;
				default:
					throw new InternalError("A class matching " + this.toString() + " is not known to this method.");
			}
		}
	}
	
	
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
	
	
	public TreeSelector createTreeSelector(File baseDirectory) {
		return new TreeSelector() {
			@Override
			public boolean selectTree(File file, String id, String label, int indexInFile) {
				return file.getAbsolutePath().equals(getAbsolutePath(baseDirectory)) && checkTree(id, label, indexInFile);
			}
		};
	}
}
