package com.learn.httpclient.service;

import java.util.Map;

public interface HttpClientService {
	
//	Map<String,Object> httpClientReturnResponse(String phoneNumber);

	Map<String, Object> getVaccineCenterByPin();
	
	Map<String, Object> getVaccineCenterByDistrict();
	Map<String, Object> getVaccineCenterForHyderabadState();

	Map<String, Object> sendOtpToGivenNumber(String phoneNumber);

}
