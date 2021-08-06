package com.learn.httpclient.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.learn.httpclient.service.EmailService;

@RestController
public class SendEmailController extends BaseController {

	@Autowired
	EmailService emailService;

//	@PostMapping(value = "/sendEmail")
//	public ResponseEntity<Map<String, Object>> sendEmailToUser(@RequestBody String requestBody) {
//		String email = (String) extractRequestParameter(requestBody,"email");
//		return handleServiceResponse(emailService.sendEmailToEmailAddress(email));
//	}

}
