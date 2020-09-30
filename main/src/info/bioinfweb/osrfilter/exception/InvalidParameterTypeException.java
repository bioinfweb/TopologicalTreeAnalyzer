package info.bioinfweb.osrfilter.exception;



public class InvalidParameterTypeException extends AnalysisException {
	private static final long serialVersionUID = 1L;


	public InvalidParameterTypeException() {
		super();
	}

	
	public InvalidParameterTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	
	public InvalidParameterTypeException(String message) {
		super(message);
	}

	
	public InvalidParameterTypeException(Throwable cause) {
		super(cause);
	}
}
