package com.learn.httpclient.controller;

import java.io.IOException;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.httpclient.service.ServiceResponse;

public class BaseController {
	
	private static final Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger(BaseController.class);
	
	@SuppressWarnings("unchecked")
	protected Map<String, Object> extractRequestParameterMap(String requestBody, String requestParameterName) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> requestparamMap = objectMapper.readValue(requestBody, Map.class);
			return (Map<String, Object>) requestparamMap.get(requestParameterName);
		} catch (IOException e) {
			LOGGER.error(e);
		} 
		return null;
	}
	
	@SuppressWarnings("unchecked")
	protected Object extractRequestParameter(String requestBody, String requestParameterName) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> requestparamMap = objectMapper.readValue(requestBody, Map.class);
			return requestparamMap.get(requestParameterName);
		} catch (IOException e) {
			LOGGER.error(e);
		} 
		return null;
	}
	
	protected <T> ResponseEntity<T> handleServiceResponse(ServiceResponse<T> serviceResponse) {
		if(serviceResponse == null) {
//			throw new CustomRuntimeException(getMlTranslatorService().toLocale(MLMessageCode.ML_ERR_INTERNAL_SERVER_ERROR_03));
		}
		if (!serviceResponse.isSuccess()) {
//			throw new CustomRuntimeException(serviceResponse.getAllErrorMessages());
		}
		return ResponseEntity.ok(serviceResponse.getResult());
	}
	
	protected String getStringFromRequestParameter(Object paramValue) {
		if(paramValue == null) {
			return "";
		}
		return paramValue.toString().trim();
	}
}
