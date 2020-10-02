package info.bioinfweb.tta.exception;



public class DuplicateEntryException extends RuntimeException {
	private static final long serialVersionUID = 1L;


	public DuplicateEntryException() {
		super();
	}

	
	public DuplicateEntryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	
	public DuplicateEntryException(String message, Throwable cause) {
		super(message, cause);
	}

	
	public DuplicateEntryException(String message) {
		super(message);
	}

	
	public DuplicateEntryException(Throwable cause) {
		super(cause);
	}
}
