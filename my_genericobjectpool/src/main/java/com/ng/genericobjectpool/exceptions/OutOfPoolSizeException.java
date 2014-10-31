package com.ng.genericobjectpool.exceptions;




public class OutOfPoolSizeException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OutOfPoolSizeException() {
	}
	
	public OutOfPoolSizeException(final String message) {
		super(message);
	}

}
