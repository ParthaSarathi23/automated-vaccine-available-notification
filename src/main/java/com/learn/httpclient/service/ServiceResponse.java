package com.learn.httpclient.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ServiceResponse<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private T result;
	private List<String> errorList;

	public boolean isSuccess() {
		return errorList == null || errorList.isEmpty();
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public List<String> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<String> errorList) {
		this.errorList = errorList;
	}

	public void addError(String errorMessage) {
		if (errorList == null) {
			errorList = new ArrayList<>();
		}
		errorList.add(errorMessage);
	}

	public String getAllErrorMessages() {
		if (errorList == null || errorList.isEmpty()) {
			return "";
		}
		StringBuilder messageBuilder = new StringBuilder();
		String comma = "";
		for (String error : errorList) {
			messageBuilder.append(comma).append(error);
			comma = ".";
		}
		return messageBuilder.toString();
	}
}
