package arenashooter.engine.json;

import com.github.cliftonlabs.json_simple.JsonKey;
import com.github.cliftonlabs.json_simple.JsonObject;

public interface StrongJsonKey extends JsonKey {
	/**
	 * Function implementing how to use this key to create <code>object</code> 
	 * @param object Object to create
	 * @param json From this Json representation
	 * @throws Exception 
	 */
	public void useKey(JsonObject json) throws Exception;
}
