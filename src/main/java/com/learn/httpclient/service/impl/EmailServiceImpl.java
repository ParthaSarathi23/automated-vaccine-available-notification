package com.learn.httpclient.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.learn.httpclient.dto.VaccineCenterVO;
import com.learn.httpclient.service.EmailService;
import com.learn.httpclient.service.ServiceResponse;

@Service
public class EmailServiceImpl implements EmailService {
	
//	"asline.anwesha@gmail.com", 
	private static final String receiverEmail = "partha.jagan96@gmail.com";
	private static final String senderEmail = "jaganstar1997@gmail.com";
	private static final String password = "hnahsjstnmlcexzz";
	private static final String emailSubject = "VACCINE AVAILABLE PLEASE CHECK";
	private static final String[] recipientEmailArray = { "aiswarya.payal1993@gmail.com",
			"madhusmita.ankita@gmail.com", "sudipta.sundar95@gmail.com", "ansumandas66@gmail.com",
			"laxman.narla@gmail.com", "Sharmisthapatnaik55@gmail.com" };

	@Override
	public ServiceResponse<Map<String, Object>> sendEmailToEmailAddress(String recipientEmail,
			List<VaccineCenterVO> VaccineCenterVOList) {
		String emailBody = construcEmailBody(VaccineCenterVOList);
		Map<String, Object> sendEmailResponse = new HashMap<>();
		ServiceResponse<Map<String, Object>> serviceResponse = new ServiceResponse<>();
		try {
			Map<String, Object> configurationMap = getEmailConfiguration();
			Properties properties = (Properties) configurationMap.get("EMAIL_PROPERTIES");
			Authenticator authenticator = (Authenticator) configurationMap.get("AUTH_OBJECT");
			Session session = Session.getDefaultInstance(properties, authenticator);
			MimeMessage mimeMessage = getMessageObject(session, recipientEmail, emailSubject, emailBody,
					recipientEmailArray);
			Transport.send(mimeMessage);
			sendEmailResponse.put("STATUS", HttpStatus.OK);
			sendEmailResponse.put("MESSAGE", "EMAIL SENT");
			serviceResponse.setResult(sendEmailResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return serviceResponse;
	}

	protected String construcEmailBody(List<VaccineCenterVO> vaccineCenterVOList) {
		StringBuilder createEmailBody = new StringBuilder();
		createEmailBody.append("<html><body>" + "<p>" + "Hi," + "</p>" + "<p>"
				+ "There is availablity of vaccine in your state" + "</p>" + "<p>" + "Please visit : " + "<a " + "href"
				+ "=https://www.cowin.gov.in/home" + ">" + "https://www.cowin.gov.in/home" + "</a>"
				+ " to book your slot." + "</p>" + "<table width='100%' border='1' align='center'>" + "<thead>" + "<tr>"
				+ "<th>" + "CENTER NAME" + "</th>" + "<th>" + "BLOCK NAME" + "</th>" + "<th>" + "PINCODE" + "</th>"
				+ "<th>" + "SEAT AVAILABLE" + "</th>" + "<th>" + "DATE" + "</th>" + "</tr>" + "<thead>" + "<tbody>");
		for (VaccineCenterVO vaccineCenterVo : vaccineCenterVOList) {
			String name = vaccineCenterVo.getCenterName();
			String blockName = vaccineCenterVo.getBlockName();
			Double pincode = vaccineCenterVo.getPincode();
			Double seatAvailable = vaccineCenterVo.getAvailableCapacity();
			String date = vaccineCenterVo.getAvailableDate();
			createEmailBody.append("<tr>");
			createEmailBody.append("<td align='center'>" + name + "</td>");
			createEmailBody.append("<td align='center'>" + blockName + "</td>");
			createEmailBody.append("<td align='center'>" + pincode + "</td>");
			createEmailBody.append("<td align='center'>" + seatAvailable + "</td>");
			createEmailBody.append("<td align='center'>" + date + "</td>");
			createEmailBody.append("</tr>");
		}
		createEmailBody.append("</tbody></table>");
		createEmailBody.append("</body></html>");
		return createEmailBody.toString();
	}

	private MimeMessage getMessageObject(Session session, String recipientEmail, String emailSubject, String emailBody,
			String[] recipientEmailArray) {
		MimeMessage mimeMessage = new MimeMessage(session);
		System.out.println(emailBody);
		try {
			mimeMessage.addHeader("Content-type", "text/HTML; charset=UTF-8");
			mimeMessage.addHeader("format", "flowed");
			mimeMessage.addHeader("Content-Transfer-Encoding", "8bit");
			mimeMessage.setFrom(new InternetAddress(senderEmail));
			mimeMessage.addRecipients(Message.RecipientType.TO, receiverEmail);
			InternetAddress[] address = new InternetAddress[recipientEmailArray.length];
			for (int i = 0; i < recipientEmailArray.length; i++) {
				address[i] = new InternetAddress(recipientEmailArray[i]);
			}
			mimeMessage.addRecipients(Message.RecipientType.CC, address);
			mimeMessage.setSubject(emailSubject);
			mimeMessage.setContent(emailBody.toString(), "text/html");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return mimeMessage;
	}

	private Map<String, Object> getEmailConfiguration() {
		Map<String, Object> emailConfigurationMap = new HashMap<>();

		Properties properties = new Properties();
		properties.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
		properties.put("mail.smtp.port", "587"); // TLS Port
		properties.put("mail.smtp.auth", "true"); // enable authentication
		properties.put("mail.smtp.starttls.enable", "true");

		Authenticator authenticator = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(senderEmail, password);
			}
		};

		emailConfigurationMap.put("EMAIL_PROPERTIES", properties);
		emailConfigurationMap.put("AUTH_OBJECT", authenticator);

		return emailConfigurationMap;
	}

}
