package com.learn.httpclient.controller;

import java.util.Calendar;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learn.httpclient.service.HttpClientService;

@RestController
@CrossOrigin("*")
public class HttpClientController {
	
	private static final Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger(HttpClientController.class);
	
	@Autowired
	private HttpClientService httpClientService;
	
	@GetMapping(value="/getvaccineAvailableByDistrict")
	private Map<String,Object> getVaccineCenterForOdisha(){
		return httpClientService.getVaccineCenterByDistrict();
	}
	
	@GetMapping(value="/getvaccineAvailableForHyderabad")
	private Map<String,Object> getVaccineCenterForHyderabad(){
		return httpClientService.getVaccineCenterForHyderabadState();
	}
	
	@GetMapping(value="/getvaccineAvailableByPin")
	private Map<String,Object> getVaccineCenterByPin(){
		return httpClientService.getVaccineCenterByPin();
	}
	
	@PostMapping(value="/getOtp/{phoneNumber}")
	private Map<String,Object> sendOTPByNumber(@PathVariable String phoneNumber){
		return httpClientService.sendOtpToGivenNumber(phoneNumber);
	}
	
	@Scheduled( fixedDelay = 100000)
	public void getVaccineAvailabilityForOdisha() {
		LOGGER.info("Auto scheduled JOB for Odisha :: "+ Calendar.getInstance().getTime());
		getVaccineCenterForOdisha();
//		LOGGER.info("Auto scheduled JOB For Hyderbad :: "+ Calendar.getInstance().getTime());
//		getVaccineCenterForHyderabad();
//		getVaccineCenterByPin();
	}
	
}
