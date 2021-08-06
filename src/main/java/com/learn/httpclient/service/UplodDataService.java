package com.learn.httpclient.service;

import java.util.List;
import java.util.Map;

import com.learn.httpclient.dto.VaccineCenterVO;

public interface UplodDataService {
	
	Map<String,Object> writeDataToExcelSheet(List<VaccineCenterVO> vaccineCenterList);
}
