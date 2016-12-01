package com.similan.framework.importexport.csv;

/**
 * Exception for signaling import errors.
 * @author pablo
 */
public class BeanImportingException extends RuntimeException {

	/**
	 * Serial version.
	 */
	private static final long serialVersionUID = 5815137500377006232L;

	public BeanImportingException(String message) {
		super(message);
	}

}
