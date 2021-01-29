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
package info.bioinfweb.tta.io.data;


import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.NoSuchElementException;



public abstract class DataIterator<E> implements Closeable {
	public static final String COLUMN_SEPARATOR = "\\t";
	
	
	private int elementCount;
	private FileReader fileReader;
	private BufferedReader bufferedReader;
	private E nextElement;
	
	
	public DataIterator(File file, int elementCount) throws IOException {
		super();
		
		if (file == null) {
			throw new IllegalArgumentException("file must not be null.");
		}
		else if (elementCount < 1) {
			throw new IllegalArgumentException("elementCount must not be below one.");
		}
		else {
			this.elementCount = elementCount;
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			bufferedReader.readLine();  // Skip heading line.
			readNextElement();
		}
	}

	
	private String[] splitLine(String line) {
		String[] result = line.split(COLUMN_SEPARATOR);
		if (line.endsWith(COLUMN_SEPARATOR)) {  // A trailing empty string is not returned.
			String[] newValues = new String[result.length + 1];
			for (int i = 0; i < result.length; i++) {
				newValues[i] = result[i];
			}
			newValues[newValues.length - 1] = "";
			result = newValues;
		}
		return result;
	}
	
	
	protected abstract E parseElement(String[] values);
	

	private void readNextElement() throws IOException {
		String line = bufferedReader.readLine();
		if (line == null) {  // eof
			nextElement = null;
		}
		else {
			String[] values = splitLine(line);
			if (values.length != elementCount) {
				throw new IOException("Invalid table structure found. Expected " + elementCount + " elements per line but found " + values.length + ".");
			}
			else {
				nextElement = parseElement(values);
			}
		}
	}
	
	
	public boolean hasNext() {
		return nextElement != null;
	}

	
	public E next() throws IOException {
		if (hasNext()) {
			E result = nextElement;
			readNextElement();
			return result;
		}
		else {
			throw new NoSuchElementException("No more elements in the file.");
		}
	}


	@Override
	public void close() throws IOException {
		if (bufferedReader != null) {
			bufferedReader.close();
		}
		if (fileReader != null) {
			fileReader.close();
		}
	}
}
