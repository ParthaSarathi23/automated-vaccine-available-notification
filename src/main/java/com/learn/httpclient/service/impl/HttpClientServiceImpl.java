package com.learn.httpclient.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.learn.httpclient.Exception.CustomRuntimeException;
import com.learn.httpclient.dto.VaccineCenterVO;
import com.learn.httpclient.service.EmailService;
import com.learn.httpclient.service.HttpClientService;
import com.learn.httpclient.service.ServiceResponse;
import com.learn.httpclient.service.UplodDataService;

@Service
public class HttpClientServiceImpl implements HttpClientService {

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UplodDataService uploadDataService;

	private static final Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger(HttpClientServiceImpl.class);
	private static final String url = "https://cdn-api.co-vin.in/api/v2/auth/public/generateOTP";
	private static final String urlSearchByPin = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/calendarByPin?";
	private static final String urlSearchByDistrict = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?";
	private static final String pinCode = "751021";
	private static final String date = "11-05-2021";

	@Override
	public Map<String, Object> getVaccineCenterByPin() {
		Map<String, Object> httpParamMap = new HashMap<>();
		httpParamMap.put("pincode", "500090");
		httpParamMap.put("date", new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime()));
		Map<String, Object> httpResponseMap = processHttpGetRequest(urlSearchByPin, httpParamMap);
		Map<String, Object> centerAvailable = (Map<String, Object>) httpResponseMap.get("DATA");
		return sendAutomatemailIfSlotisAvailable(centerAvailable);
	}
	
	protected String returnDayPlus5() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date()); // Using today's date
		calendar.add(Calendar.DATE, 0); // Adding 5 days
		return dateFormat.format(calendar.getTime());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getVaccineCenterByDistrict() {
		Map<String, Object> httpParamMap = new HashMap<>();
		httpParamMap.put("district_id", 446);
		httpParamMap.put("date", returnDayPlus5());
		Map<String, Object> httpResponseMap = processHttpGetRequest(urlSearchByDistrict, httpParamMap);
		Map<String, Object> centerAvailable = (Map<String, Object>) httpResponseMap.get("DATA");
		return sendAutomatemailIfSlotisAvailable(centerAvailable);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getVaccineCenterForHyderabadState() {
		Map<String, Object> httpParamMap = new HashMap<>();
		httpParamMap.put("district_id", 581);
		httpParamMap.put("date", new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
		Map<String, Object> httpResponseMap = processHttpGetRequest(urlSearchByDistrict, httpParamMap);
		Map<String, Object> centerAvailable = (Map<String, Object>) httpResponseMap.get("DATA");
		return sendAutomatemailIfSlotisAvailable(centerAvailable);
	}

	@Override
	public Map<String, Object> sendOtpToGivenNumber(String phoneNumber) {
		List<NameValuePair> urlParameters = new ArrayList<>();
		urlParameters.add(new BasicNameValuePair("mobile", phoneNumber));
		Map<String, Object> httpResponseMap = processHttpPostRequest(url, urlParameters);
		return httpResponseMap;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	protected Map<String, Object> sendAutomatemailIfSlotisAvailable(Map<String, Object> centerAvailable) {
		Map<String, Object> serviceResponseMap = new HashMap<>();
		List<Map<String, Object>> sessionListMaps = new ArrayList<>();
		if (centerAvailable != null) {
			List<Map<String, Object>> inCenterList = (List<Map<String, Object>>) centerAvailable.get("centers");
			List<VaccineCenterVO> vaccineCenterList = new ArrayList<>();
			if (!inCenterList.isEmpty()) {
				for (Map<String, Object> inHospitalMap : inCenterList) {
					String centerName = (String) inHospitalMap.get("name");
					Double pincode = (Double) inHospitalMap.get("pincode");
					String blockName = (String) inHospitalMap.get("block_name");
					sessionListMaps = (List<Map<String, Object>>) inHospitalMap.get("sessions");
					Double seatAvailable = Double.valueOf(0);
					String availableDate = "";
					Double miniumAgeLimit = Double.valueOf(0);
					for (Map<String, Object> isessionItem : sessionListMaps) {
						seatAvailable = (Double) isessionItem.get("available_capacity");
						availableDate = (String) isessionItem.get("date");
						miniumAgeLimit = (Double) isessionItem.get("min_age_limit");
						if (seatAvailable > Double.valueOf(0) && miniumAgeLimit == Double.valueOf(18)) {
							VaccineCenterVO vaccineCenterVO = new VaccineCenterVO();
							vaccineCenterVO.setCenterName(centerName);
							vaccineCenterVO.setPincode(pincode);
							vaccineCenterVO.setBlockName(blockName);
							vaccineCenterVO.setAvailableDate(availableDate);
							vaccineCenterVO.setAvailableCapacity(seatAvailable);
							vaccineCenterList.add(vaccineCenterVO);
						}
					}
				}
				if (!vaccineCenterList.isEmpty()) {
					LOGGER.info("Vaccine center available");
//					uploadDataService.writeDataToExcelSheet(vaccineCenterList);
					ServiceResponse<Map<String, Object>> emailServiceResponse = emailService
							.sendEmailToEmailAddress("partha.jagan96@gmail.com", vaccineCenterList);
					Map<String, Object> emailServiceMap = emailServiceResponse.getResult();
					serviceResponseMap.put("STATUS", emailServiceMap.get("STATUS"));
					serviceResponseMap.put("MESSAGE", emailServiceMap.get("MESSAGE"));
					serviceResponseMap.put("vaccineCenteList", vaccineCenterList);
				} else {
					LOGGER.info("NO Vaccine center available");
					serviceResponseMap.put("MESSAGE", "ZERO CENTER AVAILABILITY");
				}
			}
			LOGGER.info("NO Vaccine center available");
			serviceResponseMap.put("MESSAGE", "ZERO CENTER AVAILABILITY");
		}
		return serviceResponseMap;
	}

	private Map<String, Object> processHttpGetRequest(final String urlName, Map<String, Object> httpParamsMap) {
		String httpUrl = urlName;
		String httpParamsInString = getHttpGetParams(httpParamsMap);
		String httpUrlWithParam = httpUrl.concat(httpParamsInString);
		HttpGet httpRequest = new HttpGet(httpUrlWithParam);
		httpRequest.addHeader("User-Agent",
				"Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Mobile Safari/537.36");
		httpRequest.addHeader("Content-Type", "application/json");
		httpRequest.addHeader("accept", "application/json");
		Map<String, Object> requestMap = processHttpRequest(httpRequest);
		return requestMap;
	}

	private Map<String, Object> processHttpPostRequest(final String urlName, List<NameValuePair> urlParameters) {
		String httpUrl = urlName;
		HttpPost httpRequest = new HttpPost(httpUrl);
		httpRequest.addHeader("User-Agent",
				"Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Mobile Safari/537.36");
		httpRequest.addHeader("Content-Type", "application/json");
		httpRequest.addHeader("accept", "application/json");
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(urlParameters));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new CustomRuntimeException(e);
		}
		return processHttpRequest(httpRequest);
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> processHttpRequest(final HttpUriRequest request) {
		Map<String, Object> httpResponseMap = new HashMap<>();
		CloseableHttpResponse httpResponse = null;
		try (CloseableHttpClient httpClient = HttpClients.createDefault();) {
			httpResponse = httpClient.execute(request);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			httpResponseMap.put("STATUS_CODE", statusCode);
			if (statusCode == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					String jsonResponse = EntityUtils.toString(entity);
					if (jsonResponse != null) {
						Map<String, Object> fromJson = new Gson().fromJson(jsonResponse, Map.class);
						httpResponseMap.put("DATA", fromJson);
					}
				}
			} else {
				httpResponseMap.put("STATUS_CODE", statusCode);
			}

		} catch (HttpHostConnectException e) {

		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			closeHttpResponse(httpResponse);
		}
		return httpResponseMap;
	}

	private void closeHttpResponse(CloseableHttpResponse httpResponse) {
		try {
			if (httpResponse != null) {
				httpResponse.close();
			}
		} catch (IOException e) {
			LOGGER.info(e.getMessage());
		}
	}

	private String getHttpGetParams(Map<String, Object> httpParamMap) {
		StringBuilder paramBuilder = new StringBuilder();
		if (httpParamMap == null || httpParamMap.isEmpty()) {
			return paramBuilder.toString();
		}
		String amp = "&";
		int counter = 0;
		for (Map.Entry<String, Object> httpParam : httpParamMap.entrySet()) {
			if (counter == 0) {
				amp = "";
			} else
				amp = "&";
			String key = httpParam.getKey();
			Object value = httpParam.getValue();
			paramBuilder.append(amp).append(key).append("=").append(value);
			counter++;
		}
		return paramBuilder.toString();
	}

}
