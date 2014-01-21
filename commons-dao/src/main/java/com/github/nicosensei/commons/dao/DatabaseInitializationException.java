/**
 * 
 */
package com.github.nicosensei.commons.dao;

/**
 * @author ngiraud
 *
 */
public class DatabaseInitializationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6910992559169010410L;

	public DatabaseInitializationException(String message, Throwable cause) {
		super(message, cause);
	}

	public DatabaseInitializationException(String message) {
		super(message);
	}

}
