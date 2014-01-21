/**
 * 
 */
package com.github.nicosensei.commons.dao;

/**
 * @author ngiraud
 *
 */
public class DatabaseError extends RuntimeException {

	public DatabaseError(String message, Throwable cause) {
		super(message, cause);
	}

	public DatabaseError(String message) {
		super(message);
	}

}
