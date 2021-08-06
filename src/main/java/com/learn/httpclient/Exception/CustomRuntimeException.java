package com.learn.httpclient.Exception;

public class CustomRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private String errorMessage;

	private static final long serialVersionUID = 1L;

	public CustomRuntimeException() {
		super();
	}

	public CustomRuntimeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.errorMessage = message;
	}

	public CustomRuntimeException(String message, Throwable cause) {
		super(message, cause);
		this.errorMessage = message;
	}

	public CustomRuntimeException(String message) {
		super(message);
		this.errorMessage = message;
	}

	public CustomRuntimeException(Throwable cause) {
		super(cause);
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
