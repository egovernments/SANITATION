package org.egov.pqm.anomaly.finder.util;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pqm.anomaly.finder.web.model.Test;
import org.egov.pqm.anomaly.finder.web.model.TestRequest;
import org.egov.pqm.anomaly.finder.web.model.notification.EventRequest;
import org.egov.pqm.anomaly.finder.web.model.notification.SMSRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class NotificationUtil {
//
//	
//
////	private ServiceRequestRepository serviceRequestRepository;
//
//	
//
//	private RestTemplate restTemplate;
//
//	
//
//	@Autowired
//	public NotificationUtil(FSMConfiguration config, ServiceRequestRepository serviceRequestRepository,
//			FSMProducer producer, DSOService dsoService, RestTemplate restTemplate, ) {
//		this.config = config;
//		this.serviceRequestRepository = serviceRequestRepository;
//		this.producer = producer;
//		this.restTemplate = restTemplate;
//	}
//
//	static final String RECEIPT_NUMBER_KEY = "receiptNumber";
//
//
//	/**
//	 * Creates customized message based on fsm
//	 * 
//	 * @param fsm                 The fsm for which message is to be sent
//	 * @param localizationMessage The messages from localization
//	 * @return customized message based on fsm
//	 */
//	@SuppressWarnings("unchecked")
//	public String getCustomizedMsg(Test test, String localizationMessage, String messageCode,RequestInfo requestInfo) {
//		String message = null;
//
//		Test test = testRequest.getTests().get(0);
//
//		String messageTemplate = getMessageTemplate(messageCode, localizationMessage);
//
//		if (null != messageTemplate && !StringUtils.isEmpty(messageTemplate)) {
//			message = getInitiatedMsg(test, messageTemplate);
//
//
//			message = callReceiptDetails(message, test, testRequest);
//		}
//		return message;
//	}
//
//
//	private String callReceiptDetails(String message, Test test, TestRequest testRequest) {
//		if (message.contains("{APPLICATION_ID}")) {
//			message = message.replace("{APPLICATION_ID}", test.getId());
//		}
//
////		if (message.contains("{RECEIPT_LINK}")) {
////
////			String actionLink = config.getDownloadLink().replace("$mobile", fsm.getCitizen().getMobileNumber())
////					.replace("$consumerCode", fsm.getApplicationNo()).replace("$tenantId", fsm.getTenantId())
////					.replace("$receiptNumber", getPaymentData(FSMConstants.RECEIPT_NUMBER, fsmRequest))
////					.replace("$businessService", FSMConstants.FSM_PAY_BUSINESS_SERVICE);
////			message = message.replace("{RECEIPT_LINK}", getShortenedUrl(config.getUiAppHost() + actionLink));
////
////		}
////
////		if (message.contains("{RECEIPT_NO}")) {
////			message = message.replace("{RECEIPT_NO}", getPaymentData(FSMConstants.RECEIPT_NUMBER, fsmRequest));
////		}
////
////		if (message.contains("{FSM_APPL_LINK}")) {
////			message = message.replace("{FSM_APPL_LINK}",
////					getShortenedUrl(config.getUiAppHost() + config.getFsmAppLink() + fsm.getApplicationNo()));
////		}
////
////		if (message.contains("{NEW_FSM_LINK}")) {
////			message = message.replace("{NEW_FSM_LINK}",
////					getShortenedUrl(config.getUiAppHost() + config.getNewFsmLink()));
////
////		}
////		if (message.contains("{NO_OF_TRIPS}") && fsm.getNoOfTrips() != null) {
////
////			message = message.replace("{NO_OF_TRIPS}", fsm.getNoOfTrips().toString());
////		}
//		return message;
//	}
//
//
//
//
//	/**
//	 * Creates customized message for initiate
//	 * 
//	 * @param test     tenantId of the test
//	 * @param message Message from localization for initiate
//	 * @return customized message for initiate
//	 */
//	@SuppressWarnings("unchecked")
//	private String getInitiatedMsg(Test test, String message) {
//		message = message.replace("{2}", test.getId());
//		return message;
//	}
//
//	/**
//	 * Extracts message for the specific code
//	 * 
//	 * @param notificationCode    The code for which message is required
//	 * @param localizationMessage The localization messages
//	 * @return message for the specific code
//	 */
//	@SuppressWarnings("rawtypes")
//	public String getMessageTemplate(String notificationCode, String localizationMessage) {
//		String path = "$..messages[?(@.code==\"{}\")].message";
//		String message = null;
//		log.info("notificationCode :::  {} " + notificationCode);
//		if (null != notificationCode) {
//			try {
//				path = path.replace("{}", notificationCode.trim());
//				List data = JsonPath.parse(localizationMessage).read(path);
//				if (!CollectionUtils.isEmpty(data))
//					message = data.get(0).toString();
//				else
//					log.error("Fetching from localization failed with code " + notificationCode);
//			} catch (Exception e) {
//				log.warn("Fetching from localization failed", e);
//			}
//		}
//		return message;
//	}
//
//	
//
//	
//	/**
//	 * Returns the uri for the localization call
//	 * 
//	 * @param tenantId TenantId of the propertyRequest
//	 * @return The uri for localization search call
//	 */
//	public StringBuilder getUri(String tenantId, RequestInfo requestInfo) {
//
//		if (config.getIsLocalizationStateLevel())
//			tenantId = tenantId.split("\\.")[0];
//
//		String locale = "en_IN";
//		if (!StringUtils.isEmpty(requestInfo.getMsgId()) && requestInfo.getMsgId().split("\\|").length >= 2)
//			locale = requestInfo.getMsgId().split("\\|")[1];
//
//		StringBuilder uri = new StringBuilder();
//		uri.append(config.getLocalizationHost()).append(config.getLocalizationContextPath())
//				.append(config.getLocalizationSearchEndpoint()).append("?").append("locale=").append(locale)
//				.append("&tenantId=").append(tenantId).append("&module=").append(FSMConstants.SEARCH_MODULE).append(",")
//				.append(FSMConstants.FSM_LOC_SEARCH_MODULE);
//		return uri;
//	}
//
//	/**
//	 * Fetches messages from localization service
//	 * 
//	 * @param tenantId    tenantId of the fsm
//	 * @param requestInfo The requestInfo of the request
//	 * @return Localization messages for the module
//	 */
//	@SuppressWarnings("rawtypes")
//	public String getLocalizationMessages(String tenantId, RequestInfo requestInfo) {
//
//		LinkedHashMap responseMap = (LinkedHashMap) serviceRequestRepository.fetchResult(getUri(tenantId, requestInfo),
//				requestInfo);
//		return new JSONObject(responseMap).toString();
//	}
//
//
//	/**
//	 * Creates sms request for the each owners
//	 * 
//	 * @param message                 The message for the specific fsm
//	 * @param mobileNumberToOwnerName Map of mobileNumber to OwnerName
//	 * @return List of SMSRequest
//	 */
//	public List<SMSRequest> createSMSRequest(String message, Map<String, String> mobileNumberToOwner) {
//		List<SMSRequest> smsRequest = new LinkedList<>();
//
//		for (Map.Entry<String, String> entryset : mobileNumberToOwner.entrySet()) {
//			String customizedMsg = message.replace("{1}", entryset.getValue());
//			smsRequest.add(new SMSRequest(entryset.getKey(), customizedMsg));
//		}
//		return smsRequest;
//	}
//
//	/**
//	 * Pushes the event request to Kafka Queue.
//	 * 
//	 * @param request
//	 */
//	public void sendEventNotification(EventRequest request) {
//		producer.push(config.getSaveUserEventsTopic(), request);
//
//		log.debug("STAKEHOLDER:: " + request.getEvents().get(0).getDescription());
//	}
//
//	/**
//	 * Method to shortent the url returns the same url if shortening fails
//	 * 
//	 * @param url
//	 */
////	public String getShortenedUrl(String url) {
////		String res = null;
////		HashMap<String, String> body = new HashMap<>();
////		body.put("url", url);
////		StringBuilder builder = new StringBuilder(config.getUrlShortnerHost());
////		builder.append(config.getUrlShortnerEndpoint());
////		try {
////			res = restTemplate.postForObject(builder.toString(), body, String.class);
////
////		} catch (Exception e) {
////			log.error("Error while shortening the url: " + url, e);
////
////		}
////		if (StringUtils.isEmpty(res)) {
////			log.error("URL_SHORTENING_ERROR", "Unable to shorten url: " + url);
////			return url;
////		} else {
////			return res;
////		}
////	}
//
}