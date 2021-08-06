package com.learn.httpclient.service;

import java.util.List;
import java.util.Map;

import com.learn.httpclient.dto.VaccineCenterVO;

public interface EmailService {

	ServiceResponse<Map<String, Object>> sendEmailToEmailAddress(String email, List<VaccineCenterVO> vaccineCenterVO);

}
