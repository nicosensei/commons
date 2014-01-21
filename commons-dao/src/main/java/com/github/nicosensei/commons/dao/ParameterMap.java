/**
 * 
 */
package com.github.nicosensei.commons.dao;

import java.util.HashMap;

/**
 * @author ngiraud
 *
 */
@SuppressWarnings("serial")
public final class ParameterMap extends HashMap<String, Object> {
	
	/**
	 * Final constant denoting the empty parameter map
	 */
	public static final ParameterMap EMPTY = new ParameterMap(); 
	
	/**
	 * Default empty constructor.
	 */
	public ParameterMap() {
		super();
	}
	
	/**
	 * Builds a map from an array containing key-value arrays. 
	 * This method is intended to enhance code readability when building
	 * parameter maps to feed the template.
	 * @param baseMap a base map that will be copied to the new one. 
	 * @param pairs the parameters in the form [key1, value1, ... , keyN, valueN], overwrites any 
	 * key defined in baseMap  
	 * @return the equivalent map
	 */
	public ParameterMap(
			final ParameterMap baseMap, 
			final Object... pairs) {
		super();
		putAll(baseMap);
		putAll(pairs);
	}
	
	/**
	 * Builds a map from an array containing key-value arrays. 
	 * This method is intended to enhance code readability when building
	 * parameter maps to feed the template. 
	 * @param pairs the parameters in the form [key1, value1, ... , keyN, valueN] 
	 * @return the equivalent map
	 */
	public ParameterMap(final Object... pairs) {
		super();
		putAll(pairs);
	}
	
	/**
	 * Builds a singleton map.
	 * @param key the key
	 * @param value the value
	 */
	public ParameterMap(String key, Object value) {
		super();
		put(key, value);
	}
	
	public void putAll(final Object... pairs) {
		for (int i = 0; i < pairs.length - 1; i+=2) {
			this.put((String) pairs[i], pairs[i + 1]);
		}
	}

}
