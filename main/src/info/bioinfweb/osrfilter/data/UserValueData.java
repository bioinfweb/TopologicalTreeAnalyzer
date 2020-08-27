package info.bioinfweb.osrfilter.data;


import java.util.Map;



/**
 * Interface to be implemented by all model classes that provide a map with user values.
 * 
 * @author Ben St&ouml;ver
 */
public interface UserValueData {
	public Map<String, Object> getUserValues();
}
