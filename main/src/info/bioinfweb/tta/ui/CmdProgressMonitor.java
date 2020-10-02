package info.bioinfweb.tta.ui;


import info.bioinfweb.commons.progress.AbstractProgressMonitor;



public class CmdProgressMonitor extends AbstractProgressMonitor {
	public static final int ELEMENT_COUNT = 80; 
	public static final char ELEMENT_CHAR = '='; 
	

	private int writtenElements = 0;
	
	
	@Override
	public boolean isCanceled() {
		return false;
	}
	

	@Override
	protected void onProgress(double value, String text) {
		int elements = (int)Math.round(value * ELEMENT_COUNT);
		for (int i = writtenElements; i < elements; i++) {
			System.out.print(ELEMENT_CHAR);
		}
		writtenElements = elements;
	}
}
