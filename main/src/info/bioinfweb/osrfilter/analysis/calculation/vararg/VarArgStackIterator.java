package info.bioinfweb.osrfilter.analysis.calculation.vararg;


import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;



public class VarArgStackIterator<T> implements Iterator<T> {
	private Stack<Object> stack;
	private int numberOfElements;
	private int returnedElements;
	
	
	public VarArgStackIterator(Stack<Object> stack, int numberOfElements) {
		super();
		this.stack = stack;
		this.numberOfElements = numberOfElements;
		returnedElements = 0;
	}


	@Override
	public boolean hasNext() {
		return (numberOfElements > returnedElements);
	}


	@Override
	public T next() {
		if (hasNext()) {
			returnedElements++;
			return (T)stack.pop();
		}
		else {
			throw new NoSuchElementException();
		}
	}
	
	
}
