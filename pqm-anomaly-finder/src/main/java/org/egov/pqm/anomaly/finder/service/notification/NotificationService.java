package org.egov.pqm.anomaly.finder.service.notification;


import org.springframework.stereotype.Service;

@Service
public class NotificationService {
//
//	@Autowired
//	private PqmAnomalyConfiguration config;
//
//	@Autowired
//	private NotificationUtil util;
//
//
//	
//	/**
//	 * Creates and send the sms based on the fsmRequest
//	 * 
//	 * @param request The fsmRequest listenend on the kafka topic
//	 */
//	public void process(TestRequest testRequest) {
//		if (null != config.getIsEventsNotificationEnabled() && config.getIsEventsNotificationEnabled()) {
//
//			EventRequest request = getEvents(testRequest);
//			if (null != request)
//				util.sendEventNotification(request);
//
//		}
//	}
////	
////	  public void prepareEventAndSend(TestRequest testRequest){
////	        List<SMSRequest> smsRequestList = new ArrayList<>();
////	        testRequest.getTests().forEach(test -> {
////	            SMSRequest smsRequest = SMSRequest.builder().mobileNumber(application.getApplicant().getMobileNumber()).message(getCustomMessage(smsTemplate, application)).build();
////	            smsRequestList.add(smsRequest);
////	        });
////	        for (SMSRequest smsRequest : smsRequestList) {
////	            producer.push(config.getSmsNotificationTopic(), smsRequest);
////	            log.info("Messages: " + smsRequest.getMessage());
////	        }
////	    }
//
//	/**
//	 * Creates and registers an event at the egov-user-event service at defined
//	 * trigger points as that of sms notifs.
//	 * 
//	 * Assumption - The fsmRequest received will always contain only one fsm.
//	 * 
//	 * @param request
//	 * @return
//	 */
//	public EventRequest getEvents(TestRequest testRequest) {
//
//		List<Event> events = new ArrayList<>();
//		RequestInfo requestInfo= testRequest.getRequestInfo();
//		List<Test> tests=testRequest.getTests();
//		for(Test test:tests)
//		{
//			
//			List<SMSRequest> smsRequests = new LinkedList<>();
//
//			enrichSMSRequest(test, smsRequests,requestInfo);
//
//			Set<String> mobileNumbers = smsRequests.stream().map(SMSRequest::getMobileNumber).collect(Collectors.toSet());
//			Map<String, String> mapOfPhnoAndUUIDs = fetchUserUUIDs(mobileNumbers, fsmRequest.getRequestInfo(),
//					fsmRequest.getFsm().getTenantId());
//
//			Map<String, String> mobileNumberToMsg = smsRequests.stream()
//					.collect(Collectors.toMap(SMSRequest::getMobileNumber, SMSRequest::getMessage));
//			for (String mobile : mobileNumbers) {
//				if (null == mapOfPhnoAndUUIDs.get(mobile) || null == mobileNumberToMsg.get(mobile)) {
//					log.error("No UUID/SMS for mobile {} skipping event", mobile);
//					continue;
//				}
//				List<String> toUsers = new ArrayList<>();
//				toUsers.add(mapOfPhnoAndUUIDs.get(mobile));
//				Recepient recepient = Recepient.builder().toUsers(toUsers).toRoles(null).build();
//				Action action = null;
//
//				events.add(Event.builder().tenantId(fsmApplication.getTenantId()).description(mobileNumberToMsg.get(mobile))
//						.eventType(FSMConstants.USREVENTS_EVENT_TYPE).name(FSMConstants.USREVENTS_EVENT_NAME)
//						.postedBy(FSMConstants.USREVENTS_EVENT_POSTEDBY).source(Source.WEBAPP).recepient(recepient)
//						.eventDetails(null).actions(action).build());
//			}
//		}
//		
//
//		if (!CollectionUtils.isEmpty(events)) {
//			return EventRequest.builder().requestInfo(fsmRequest.getRequestInfo()).events(events).build();
//		} else {
//			return null;
//		}
//
//	}
//
//	
//	/**
//	 * Enriches the smsRequest with the customized messages
//	 * 
//	 * @param request     The fsmRequest from kafka topic
//	 * @param smsRequests List of SMSRequets
//	 */
//	private void enrichSMSRequest(Test test, List<SMSRequest> smsRequests,RequestInfo requestInfo) {
//		String tenantId = test.getTenantId();
////		Test test = testRequest.getTests().get(0);
//		String localizationMessages = util.getLocalizationMessages(tenantId, requestInfo);
//		String messageCode = null;
//
//		if (test != null) {
//			String appCreatedMessage = "NOTIFICATION_CREATE";
//
//			String message = util.getCustomizedMsg(test, localizationMessages, appCreatedMessage,requestInfo);
//			UserDetailResponse userDetailResponse = getUserList(test,requestInfo);
//			Map<String, String> mobileNumberToOwner = new HashMap<>();
//			mobileNumberToOwner.put(userDetailResponse.getUser().get(0).getMobileNumber(),
//					userDetailResponse.getUser().get(0).getName());
//			smsRequests.addAll(util.createSMSRequest(message, mobileNumberToOwner));
//		}
//		
//		/**
//		 * To get the Users to whom we need to send the sms notifications or event
//		 * notifications.
//		 * 
//		 * @param fsmRequest
//		 * @return
//		 */
//		private UserDetailResponse getUserList(Test test, List<SMSRequest> smsRequests,RequestInfo requestInfo) {
//			String tenantId = fsmRequest.getFsm().getTenantId().split("\\.")[0];
//			String stakeUUID = fsmRequest.getFsm().getAccountId();
//			List<String> ownerId = new ArrayList<>();
//			ownerId.add(stakeUUID);
//			FSMSearchCriteria fsSearchCriteria = new FSMSearchCriteria();
//			fsSearchCriteria.setOwnerIds(ownerId);
//			fsSearchCriteria.setTenantId(tenantId);
//			return userService.getUser(fsSearchCriteria, fsmRequest.getRequestInfo());
//		}
//
//	}
//
//	
//
//	
//
//	/**
//	 * To get the Users to whom we need to send the sms notifications or event
//	 * notifications.
//	 * 
//	 * @param fsmRequest
//	 * @return
//	 */
////	private UserDetailResponse getUserList(FSMRequest fsmRequest) {
////		String tenantId = fsmRequest.getFsm().getTenantId().split("\\.")[0];
////		String stakeUUID = fsmRequest.getFsm().getAccountId();
////		List<String> ownerId = new ArrayList<>();
////		ownerId.add(stakeUUID);
////		FSMSearchCriteria fsSearchCriteria = new FSMSearchCriteria();
////		fsSearchCriteria.setOwnerIds(ownerId);
////		fsSearchCriteria.setTenantId(tenantId);
////		return userService.getUser(fsSearchCriteria, fsmRequest.getRequestInfo());
////	}
//
//
//
}
