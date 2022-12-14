package org.egov.fsm.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.math.NumberUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.fsm.config.FSMConfiguration;
import org.egov.fsm.producer.Producer;
import org.egov.fsm.repository.FSMRepository;
import org.egov.fsm.util.FSMAuditUtil;
import org.egov.fsm.util.FSMConstants;
import org.egov.fsm.util.FSMErrorConstants;
import org.egov.fsm.util.FSMUtil;
import org.egov.fsm.validator.FSMValidator;
import org.egov.fsm.web.model.FSM;
import org.egov.fsm.web.model.FSMAudit;
import org.egov.fsm.web.model.FSMAuditSearchCriteria;
import org.egov.fsm.web.model.FSMRequest;
import org.egov.fsm.web.model.FSMResponse;
import org.egov.fsm.web.model.FSMSearchCriteria;
import org.egov.fsm.web.model.PeriodicApplicationRequest;
import org.egov.fsm.web.model.Workflow;
import org.egov.fsm.web.model.dso.Vendor;
import org.egov.fsm.web.model.dso.VendorSearchCriteria;
import org.egov.fsm.web.model.notification.SMSRequest;
import org.egov.fsm.web.model.user.User;
import org.egov.fsm.web.model.user.UserDetailResponse;
import org.egov.fsm.web.model.vehicle.Vehicle;
import org.egov.fsm.web.model.workflow.BusinessService;
import org.egov.fsm.workflow.ActionValidator;
import org.egov.fsm.workflow.WorkflowIntegrator;
import org.egov.fsm.workflow.WorkflowService;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.egov.fsm.service.notification.NotificationService;

import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FSMService {
	/**
	 * does all the validations required to create fsm Record in the system
	 * 
	 * @param fsmRequest
	 * @return
	 */
	@Autowired
	private FSMUtil util;

	@Autowired
	private EnrichmentService enrichmentService;

	@Autowired
	private FSMValidator fsmValidator;

	@Autowired
	private WorkflowIntegrator wfIntegrator;

	@Autowired
	private ActionValidator actionValidator;

	@Autowired
	private WorkflowService workflowService;

	@Autowired
	private UserService userService;

	@Autowired
	private VehicleTripService vehicleTripService;

	@Autowired
	private CalculationService calculationService;

	@Autowired
	private DSOService dsoService;

	@Autowired
	private FSMConfiguration config;

	@Autowired
	VehicleService vehicleService;

	@Autowired
	FSMUtil fsmUtil;

	@Autowired
	FSMRepository fsmRepository;

	@Autowired
	private FSMRepository repository;
	
	@Autowired
	private NotificationService notificationService;

	public FSM create(FSMRequest fsmRequest) {
		RequestInfo requestInfo = fsmRequest.getRequestInfo();
//		String tenantId = fsmRequest.getFsm().getTenantId().split("\\.")[0];
		Object mdmsData = util.mDMSCall(requestInfo, fsmRequest.getFsm().getTenantId());
		if (fsmRequest.getFsm().getTenantId().split("\\.").length == 1) {
			throw new CustomException(FSMErrorConstants.INVALID_TENANT, " Application cannot be create at StateLevel");
		}
		fsmValidator.validateCreate(fsmRequest, mdmsData);
		enrichmentService.enrichFSMCreateRequest(fsmRequest, mdmsData);

		wfIntegrator.callWorkFlow(fsmRequest);
		repository.save(fsmRequest);
		if (!FSMConstants.FSM_PAYMENT_PREFERENCE_POST_PAY.equalsIgnoreCase(fsmRequest.getFsm().getPaymentPreference())
				&& (requestInfo.getUserInfo().getType().equalsIgnoreCase(FSMConstants.EMPLOYEE)
						|| requestInfo.getUserInfo().getType().equalsIgnoreCase(FSMConstants.SYSTEM))) {
			calculationService.addCalculation(fsmRequest, FSMConstants.APPLICATION_FEE);
		}

		return fsmRequest.getFsm();
	}

	/**
	 * Updates the FSM
	 * 
	 * @param fsmRequest The update Request
	 * @return Updated FSM
	 */
	@SuppressWarnings("unchecked")
	public FSM update(FSMRequest fsmRequest) {

		RequestInfo requestInfo = fsmRequest.getRequestInfo();
//		String tenantId = fsmRequest.getFsm().getTenantId().split("\\.")[0];
		Object mdmsData = util.mDMSCall(requestInfo, fsmRequest.getFsm().getTenantId());
		FSM fsm = fsmRequest.getFsm();

		if (fsm.getId() == null) {
			throw new CustomException(FSMErrorConstants.UPDATE_ERROR, "Application Not found in the System" + fsm);
		}

		if (fsmRequest.getWorkflow() == null || fsmRequest.getWorkflow().getAction() == null) {
			throw new CustomException(FSMErrorConstants.UPDATE_ERROR,
					"Workflow action cannot be null." + String.format("{Workflow:%s}", fsmRequest.getWorkflow()));
		}

		boolean isDsoRole = hasDsoOrEditorRole(fsmRequest);

		List<String> ids = new ArrayList<String>();
		ids.add(fsm.getId());
		FSMSearchCriteria criteria = FSMSearchCriteria.builder().ids(ids).tenantId(fsm.getTenantId()).build();
		FSMResponse fsmResponse = repository.getFSMData(criteria, null);
		List<FSM> fsms = fsmResponse.getFsm();

		String businessServiceName = null;
		if (FSMConstants.FSM_PAYMENT_PREFERENCE_POST_PAY.equalsIgnoreCase(fsmRequest.getFsm().getPaymentPreference()))
			businessServiceName = FSMConstants.FSM_POST_PAY_BusinessService;
		else
			businessServiceName = FSMConstants.FSM_BusinessService;

		BusinessService businessService = workflowService.getBusinessService(fsm, fsmRequest.getRequestInfo(),
				businessServiceName, null);
		actionValidator.validateUpdateRequest(fsmRequest, businessService);

		FSM oldFSM = fsms.get(0);

		if (fsmRequest.getWorkflow().getAction().equalsIgnoreCase(FSMConstants.WF_ACTION_REJECT)
				|| fsmRequest.getWorkflow().getAction().equalsIgnoreCase(FSMConstants.WF_ACTION_CANCEL)) {
			handleRejectCancel(fsmRequest, oldFSM);
		} else {
			fsmValidator.validateUpdate(fsmRequest, fsms, mdmsData, isDsoRole);
		}

		// SAN-843: Payment demand should be generated only for Pay now payment
		// preference on Submit Action
		if (fsmRequest.getFsm().getPaymentPreference() != null
				&& !(FSMConstants.FSM_PAYMENT_PREFERENCE_POST_PAY
						.equalsIgnoreCase(fsmRequest.getFsm().getPaymentPreference()))
				&& fsmRequest.getWorkflow().getAction().equalsIgnoreCase(FSMConstants.WF_ACTION_SUBMIT)) {
			handleApplicationSubmit(fsmRequest, oldFSM);
		}

		if (fsmRequest.getWorkflow().getAction().equalsIgnoreCase(FSMConstants.WF_ACTION_ASSIGN_DSO)
				|| fsmRequest.getWorkflow().getAction().equalsIgnoreCase(FSMConstants.WF_ACTION_REASSIGN_DSO)) {
			handleAssignDSO(fsmRequest);
		}

		if (fsmRequest.getWorkflow().getAction().equalsIgnoreCase(FSMConstants.WF_ACTION_DSO_ACCEPT)
				|| fsmRequest.getWorkflow().getAction().equalsIgnoreCase(FSMConstants.WF_ACTION_SCHEDULE)) {
			handleDSOAccept(fsmRequest, oldFSM);
		}
		/*
		 * if (fsmRequest.getWorkflow().getAction().equalsIgnoreCase(FSMConstants.
		 * WF_ACTION_SCHEDULE)) { handleDSOAccept(fsmRequest,oldFSM); }
		 */

		if (fsmRequest.getWorkflow().getAction().equalsIgnoreCase(FSMConstants.WF_ACTION_DSO_REJECT)) {
			handleDSOReject(fsmRequest, oldFSM);
		}

		if (fsmRequest.getWorkflow().getAction().equalsIgnoreCase(FSMConstants.WF_ACTION_COMPLETE)) {
			handleFSMComplete(fsmRequest, oldFSM);
		}

		if (fsmRequest.getWorkflow().getAction().equalsIgnoreCase(FSMConstants.WF_ACTION_SUBMIT_FEEDBACK)) {
			handleFSMSubmitFeeback(fsmRequest, oldFSM, mdmsData);
		}

		if (fsmRequest.getWorkflow().getAction().equalsIgnoreCase(FSMConstants.WF_ACTION_ADDITIONAL_PAY_REQUEST)) {
			handleAdditionalPayRequest(fsmRequest, oldFSM);
		}

		if (fsmRequest.getWorkflow().getAction().equalsIgnoreCase(FSMConstants.WF_ACTION_SEND_BACK)) {
			handleSendBack(fsmRequest, oldFSM);
		}

		enrichmentService.enrichFSMUpdateRequest(fsmRequest, mdmsData, oldFSM);

		wfIntegrator.callWorkFlow(fsmRequest);

		enrichmentService.postStatusEnrichment(fsmRequest);
		
		notificationService.process(fsmRequest,oldFSM);

		repository.update(fsmRequest, workflowService.isStateUpdatable(fsm.getApplicationStatus(), businessService));
		return fsmRequest.getFsm();
	}

	/**
	 * 
	 * @param fsmRequest
	 */
	private void handleAssignDSO(FSMRequest fsmRequest) {

		FSM fsm = fsmRequest.getFsm();
		if (!StringUtils.hasLength(fsm.getDsoId())) {
			throw new CustomException(FSMErrorConstants.INVALID_DSO, " DSO is invalid");
		}

		if (fsm.getPossibleServiceDate() != null) {
			Calendar psd = Calendar.getInstance(TimeZone.getDefault());
			psd.setTimeInMillis(fsm.getPossibleServiceDate());
			Calendar today = Calendar.getInstance(TimeZone.getDefault());
			today.set(Calendar.HOUR_OF_DAY, 0);
			today.set(Calendar.MINUTE, 0);
			today.set(Calendar.SECOND, 0);
			today.set(Calendar.MILLISECOND, 0);
			psd.set(Calendar.HOUR_OF_DAY, 0);
			psd.set(Calendar.MINUTE, 0);
			psd.set(Calendar.SECOND, 0);
			psd.set(Calendar.MILLISECOND, 0);
			if (today.after(psd)) {
				throw new CustomException(FSMErrorConstants.INVALID_POSSIBLE_DATE,
						" Possible service Date  is invalid");
			}
		} else {
			throw new CustomException(FSMErrorConstants.INVALID_POSSIBLE_DATE, " Possible service Date  is invalid");
		}
		dsoService.validateDSO(fsmRequest);

	}

	private void handleDSOAccept(FSMRequest fsmRequest, FSM oldFSM) {
		FSM fsm = fsmRequest.getFsm();
		org.egov.common.contract.request.User dsoUser = fsmRequest.getRequestInfo().getUserInfo();

		String dsoOwnerId = null;

		Boolean isDso = util.isRoleAvailale(dsoUser, FSMConstants.ROLE_FSM_DSO,
				fsmRequest.getRequestInfo().getUserInfo().getTenantId().split("\\.")[0]);
		if (isDso) {
			dsoOwnerId = dsoUser.getUuid();
		} else if (!util.isRoleAvailale(dsoUser, FSMConstants.FSM_EDITOR_EMP,
				fsmRequest.getRequestInfo().getUserInfo().getTenantId())) {
			throw new CustomException(FSMErrorConstants.INVALID_VEHICLE_ASSIGN_ACTION,
					" Only Employee with FSM_EDITOR role and/or assigned DSO can take this action. ");
		}
		VendorSearchCriteria vendorSearchCriteria = new VendorSearchCriteria();
		if (null != oldFSM.getDsoId()) {
			vendorSearchCriteria.setIds(Arrays.asList(oldFSM.getDsoId()));
		}
		if (null != dsoOwnerId) {
			vendorSearchCriteria.setOwnerIds(Arrays.asList(dsoOwnerId));
		}
		vendorSearchCriteria.setTenantId(fsm.getTenantId());
		Vendor vendor = dsoService.getVendor(vendorSearchCriteria, fsmRequest.getRequestInfo());

		if (vendor == null) {
			throw new CustomException(FSMErrorConstants.INVALID_DSO,
					" DSO is invalid, cannot take an action, Application is not assigned to current logged in user !");
		}
		fsm.setDso(vendor);
		if (!StringUtils.hasLength(fsm.getVehicleId())) {
			throw new CustomException(FSMErrorConstants.INVALID_DSO_VEHICLE,
					"Vehicle should be assigned to accept the Request !");
		} else {

			Vehicle vehicle = vehicleService.validateVehicle(fsmRequest);
			Map<String, Vehicle> vehilceIdMap = vendor.getVehicles().stream()
					.collect(Collectors.toMap(Vehicle::getId, Function.identity()));
			if (!CollectionUtils.isEmpty(vehilceIdMap) && vehilceIdMap.get(fsm.getVehicleId()) == null) {
				throw new CustomException(FSMErrorConstants.INVALID_DSO_VEHICLE, " Vehicle Does not belong to DSO!");
			} else {
				vehicle = vehilceIdMap.get(fsm.getVehicleId());
				fsm.setVehicle(vehicle);
			}

		}
		if (fsmRequest.getWorkflow().getAction().equalsIgnoreCase(FSMConstants.WF_ACTION_DSO_ACCEPT)) {
			ArrayList<String> uuids = new ArrayList<String>();
			uuids.add(fsm.getDso().getOwner().getUuid());
			fsmRequest.getWorkflow().setAssignes(uuids);
		}

		if (fsmRequest.getFsm().getPaymentPreference() != null && !(FSMConstants.FSM_PAYMENT_PREFERENCE_POST_PAY
				.equalsIgnoreCase(fsmRequest.getFsm().getPaymentPreference()))) {
			vehicleTripService.scheduleVehicleTrip(fsmRequest);
		} else if (FSMConstants.FSM_PAYMENT_PREFERENCE_POST_PAY
				.equalsIgnoreCase(fsmRequest.getFsm().getPaymentPreference())
				&& fsmRequest.getWorkflow().getAction().equalsIgnoreCase(FSMConstants.WF_ACTION_SCHEDULE)) {
			calculationService.addCalculation(fsmRequest, FSMConstants.APPLICATION_FEE);
			vehicleTripService.scheduleVehicleTrip(fsmRequest);
		}

	}

	private void handleDSOReject(FSMRequest fsmRequest, FSM oldFSM) {
		FSM fsm = fsmRequest.getFsm();
		// org.egov.common.contract.request.User dsoUser =
		// fsmRequest.getRequestInfo().getUserInfo();
		fsm.setDsoId(null);
		fsm.setVehicleId(null);
		Workflow workflow = fsmRequest.getWorkflow();
		if (!StringUtils.hasLength(workflow.getComments())) {
			throw new CustomException(FSMErrorConstants.INVALID_COMMENT_CANCEL_REJECT,
					" Comment is mandatory to reject or cancel the application !.");
		}
	}

	private void handleFSMComplete(FSMRequest fsmRequest, FSM oldFSM) {
		FSM fsm = fsmRequest.getFsm();
		Vehicle vehicle = vehicleService.getVehicle(fsm.getVehicleId(), fsm.getTenantId(), fsmRequest.getRequestInfo());
		if (fsm.getWasteCollected() == null || fsm.getWasteCollected() <= 0 || (vehicle != null
				&& vehicle.getTankCapacity() != null && fsm.getWasteCollected() > vehicle.getTankCapacity())) {
			throw new CustomException(FSMErrorConstants.INVALID_WASTER_COLLECTED,
					" Wastecollected is invalid to complete the application !.");
		}

		if (fsm.getCompletedOn() != null) {
			Calendar cmpOn = Calendar.getInstance(TimeZone.getDefault());
			cmpOn.setTimeInMillis(fsm.getCompletedOn());
			Calendar strtDt = Calendar.getInstance(TimeZone.getDefault());
			strtDt.setTimeInMillis(fsm.getAuditDetails().getCreatedTime());
			Calendar today = Calendar.getInstance(TimeZone.getDefault());
			strtDt.set(Calendar.HOUR_OF_DAY, 0);
			strtDt.set(Calendar.MINUTE, 0);
			strtDt.set(Calendar.SECOND, 0);
			strtDt.set(Calendar.MILLISECOND, 0);
			today.set(Calendar.HOUR_OF_DAY, 0);
			today.set(Calendar.MINUTE, 0);
			today.set(Calendar.SECOND, 0);
			today.set(Calendar.MILLISECOND, 0);
			cmpOn.set(Calendar.HOUR_OF_DAY, 0);
			cmpOn.set(Calendar.MINUTE, 0);
			cmpOn.set(Calendar.SECOND, 0);
			cmpOn.set(Calendar.MILLISECOND, 0);
			cmpOn.add(Calendar.DAY_OF_YEAR, 1);
			today.add(Calendar.DAY_OF_YEAR, 2);
			if (!(cmpOn.after(strtDt) && cmpOn.before(today))) {
				throw new CustomException(FSMErrorConstants.INVALID_COMPLETED_DATE, " Completed  Date  is invalid");
			}
		} else {
			throw new CustomException(FSMErrorConstants.COMPLETED_DATE_NOT_NULL, " Completed On Cannot be empty !");
		}

		ArrayList<String> assignes = new ArrayList<String>();
		assignes.add(fsm.getAccountId());
		FSMSearchCriteria fsmsearch = new FSMSearchCriteria();
		ArrayList<String> accountIds = new ArrayList<String>();
		accountIds.add(fsm.getAccountId());
		fsmsearch.setTenantId(fsm.getTenantId());
		fsmsearch.setOwnerIds(accountIds);
		UserDetailResponse userDetailResponse = userService.getUser(fsmsearch, fsmRequest.getRequestInfo());
		fsmsearch.setMobileNumber(userDetailResponse.getUser().get(0).getMobileNumber());
		fsmsearch.setOwnerIds(null);
		userDetailResponse = userService.getUser(fsmsearch, null);
		fsmRequest.getWorkflow()
				.setAssignes(userDetailResponse.getUser().stream().map(User::getUuid).collect(Collectors.toList()));

		if (fsmRequest.getFsm().getPaymentPreference() != null && !(FSMConstants.FSM_PAYMENT_PREFERENCE_POST_PAY
				.equalsIgnoreCase(fsmRequest.getFsm().getPaymentPreference()))) {
			vehicleTripService.vehicleTripReadyForDisposal(fsmRequest);

		} else if (fsmRequest.getFsm().getPaymentPreference() != null && (FSMConstants.FSM_PAYMENT_PREFERENCE_POST_PAY
				.equalsIgnoreCase(fsmRequest.getFsm().getPaymentPreference()))) {
			vehicleTripService.updateVehicleTrip(fsmRequest);
		}
	}

	private void handleFSMSubmitFeeback(FSMRequest fsmRequest, FSM oldFSM, Object mdmsData) {
		// FSM fsm = fsmRequest.getFsm();
		org.egov.common.contract.request.User citizen = fsmRequest.getRequestInfo().getUserInfo();
		if (!citizen.getUuid().equalsIgnoreCase(fsmRequest.getRequestInfo().getUserInfo().getUuid())) {
			throw new CustomException(FSMErrorConstants.INVALID_UPDATE,
					" Only owner of the application can submit the feedback !.");
		}
		if (fsmRequest.getWorkflow().getRating() == null) {
			throw new CustomException(FSMErrorConstants.INVALID_UPDATE, " Rating has to be provided!");
		} else if (config.getAverageRatingCommentMandatory() != null
				&& Integer.parseInt(config.getAverageRatingCommentMandatory()) > fsmRequest.getWorkflow().getRating()) {
			if (!StringUtils.hasLength(fsmRequest.getWorkflow().getComments())) {
				throw new CustomException(FSMErrorConstants.INVALID_UPDATE,
						" Comment mandatory for rating " + fsmRequest.getWorkflow().getRating());
			}
		}
		fsmValidator.validateCheckList(fsmRequest, mdmsData);

		// TODO handle the citizen rating and checklist.
	}

	private void handleAdditionalPayRequest(FSMRequest fsmRequest, FSM oldFSM) {
		FSM fsm = fsmRequest.getFsm();
		// TODO if additionalcharge is allowed then allow this action and then call
		// calculator
	}

	private void handleRejectCancel(FSMRequest fsmRequest, FSM oldFSM) {
		Workflow workflow = fsmRequest.getWorkflow();
		if (!StringUtils.hasLength(workflow.getComments())) {
			throw new CustomException(FSMErrorConstants.INVALID_COMMENT_CANCEL_REJECT,
					" Comment is mandatory to reject or cancel the application !.");
		}
	}

	private void handleSendBack(FSMRequest fsmRequest, FSM oldFSM) {
		FSM fsm = fsmRequest.getFsm();
		// TODO based on the old application Status DSO or vehicle has to removed
	}

	/**
	 * search the fsm applications based on the search criteria
	 * 
	 * @param criteria
	 * @param requestInfo
	 * @return
	 */
	public FSMResponse FSMsearch(FSMSearchCriteria criteria, RequestInfo requestInfo) {

		List<FSM> fsmList = new LinkedList<>();
		FSMResponse fsmResponse = null;
		List<String> uuids = new ArrayList<String>();
		UserDetailResponse usersRespnse;
		String dsoId = null;

		fsmValidator.validateSearch(requestInfo, criteria);

		if (requestInfo.getUserInfo().getType().equalsIgnoreCase(FSMConstants.CITIZEN)) {
			List<Role> roles = requestInfo.getUserInfo().getRoles();
			if (roles.stream().anyMatch(role -> Objects.equals(role.getCode(), FSMConstants.ROLE_FSM_DSO))) {

				VendorSearchCriteria vendorSearchCriteria = new VendorSearchCriteria();
				vendorSearchCriteria = VendorSearchCriteria.builder()
						.mobileNumber(requestInfo.getUserInfo().getMobileNumber()).tenantId(criteria.getTenantId())
						.build();

				Vendor dso = dsoService.getVendor(vendorSearchCriteria, requestInfo);

				if (dso != null && org.apache.commons.lang3.StringUtils.isNotEmpty(dso.getId())) {
					dsoId = dso.getId();
				}
			} else if (criteria.tenantIdOnly()) {
				criteria.setMobileNumber(requestInfo.getUserInfo().getMobileNumber());
			}

		}

		if (criteria.getMobileNumber() != null && StringUtils.hasText(criteria.getMobileNumber())) {
			usersRespnse = userService.getUser(criteria, requestInfo);
			if (usersRespnse != null && usersRespnse.getUser() != null && usersRespnse.getUser().size() > 0) {
				uuids = usersRespnse.getUser().stream().map(User::getUuid).collect(Collectors.toList());
				if (CollectionUtils.isEmpty(criteria.getOwnerIds())) {
					criteria.setOwnerIds(uuids);
				} else {
					criteria.getOwnerIds().addAll(uuids);
				}

			} else {
				return FSMResponse.builder().fsm(fsmList).build();
			}
		}

		fsmResponse = repository.getFSMData(criteria, dsoId);
		fsmList = fsmResponse.getFsm();
		if (!fsmList.isEmpty()) {
			enrichmentService.enrichFSMSearch(fsmList, requestInfo, criteria.getTenantId());
		}

		return fsmResponse;
	}

	/**
	 * Handles the application submit, identify the tripAmount and call calculation
	 * service.
	 * 
	 * @param fsmRequest
	 * @param oldFSM
	 */
	public void handleApplicationSubmit(FSMRequest fsmRequest, FSM oldFSM) {

		calculationService.addCalculation(fsmRequest, FSMConstants.APPLICATION_FEE);
	}

	public List<FSMAudit> auditSearch(FSMAuditSearchCriteria criteria, RequestInfo requestInfo) {
		fsmValidator.validateAudit(criteria);
		List<FSMAudit> auditList = null;
		List<FSMAuditUtil> sourceObjects = repository.getFSMActualData(criteria);
		if (!CollectionUtils.isEmpty(sourceObjects)) {
			FSMAuditUtil sourceObject = sourceObjects.get(NumberUtils.INTEGER_ZERO);
			List<FSMAuditUtil> targetObjects = repository.getFSMAuditData(criteria);
			auditList = enrichmentService.enrichFSMAudit(sourceObject, targetObjects);
		}
		return auditList;

	}

	public List<FSM> searchFSMPlainSearch(@Valid FSMSearchCriteria criteria, RequestInfo requestInfo) {
		List<FSM> fsmList = getFsmPlainSearch(criteria, requestInfo);
		if (!fsmList.isEmpty()) {
			enrichmentService.enrichFSMSearch(fsmList, requestInfo, criteria.getTenantId());
		}
		return fsmList;
	}

	private List<FSM> getFsmPlainSearch(@Valid FSMSearchCriteria criteria, RequestInfo requestInfo) {
		if (criteria.getLimit() != null && criteria.getLimit() > config.getMaxSearchLimit())
			criteria.setLimit(config.getMaxSearchLimit());

		List<String> ids = null;

		if (criteria.getIds() != null && !criteria.getIds().isEmpty())
			ids = criteria.getIds();
		else
			ids = repository.fetchFSMIds(criteria);

		if (ids.isEmpty())
			return Collections.emptyList();

		FSMSearchCriteria FSMcriteria = FSMSearchCriteria.builder().ids(ids).build();

		List<FSM> listFSM = repository.getFsmPlainSearch(FSMcriteria);
		return listFSM;
	}

	/***
	 * This method will return the applicationo's list which all crossed the time
	 * limit for that tenantid
	 * 
	 * @param tenantId
	 * @return
	 */

	public List<String> getFSMApplicationsForPeriodicServices(String tenantId, RequestInfo requestInfo) {

		Object result = fsmUtil.getMasterData(FSMConstants.PERIODIC_MASTER_NAME, tenantId, requestInfo);

		List<Map> periodicData = JsonPath.read(result, FSMConstants.PERIODIC_SERVICE_PATH);

		if (periodicData != null && periodicData.get(0) != null) {
			long timeLimit = Long.valueOf(periodicData.get(0).get("timeLimit").toString());
			List<String> applicationList = fsmRepository.getPeriodicEligiableApplicationList(tenantId, timeLimit);

			return applicationList;

		}

		return new ArrayList<>();

	}

	public void scheduleperiodicapplications(RequestInfo requestInfo) {

		List<String> tenantIdList = fsmRepository.getTenants();
		for (String tenantId : tenantIdList) {
			Object result = fsmUtil.getMasterData(FSMConstants.PERIODIC_MASTER_NAME, tenantId, requestInfo);
			List<Map> periodicData = null;

			try {
				periodicData = JsonPath.read(result, FSMConstants.PERIODIC_SERVICE_PATH);
			} catch (Exception e) {
				log.info("Exception occured while creataing application: " + e.getMessage());
			}

			if (periodicData != null && periodicData.get(0) != null) {

				boolean isSchedular = Boolean.valueOf(periodicData.get(0).get("isSchedularConfiguration").toString());

				if (isSchedular) {

					List<String> applicationNoList = getFSMApplicationsForPeriodicServices(tenantId, requestInfo);
					PeriodicApplicationRequest periodicApplicationRequest = new PeriodicApplicationRequest();
					periodicApplicationRequest.setRequestInfo(requestInfo);
					periodicApplicationRequest.setApplicationNoList(applicationNoList);
					periodicApplicationRequest.setTenantId(tenantId);
					createperiodicapplications(periodicApplicationRequest);

				}

			}
		}

	}

	public List<String> createperiodicapplications(PeriodicApplicationRequest periodicApplicationRequest) {

		List<String> applicationList = new ArrayList<>();

		for (String applicationNo : periodicApplicationRequest.getApplicationNoList()) {

			List<String> applicationNoList = fsmRepository.getOldPeriodicApplications(applicationNo,
					periodicApplicationRequest.getTenantId());

			if (applicationNoList.size() == 0 || applicationNoList.get(0) == applicationNo) {
				String newApplicationNo = createPeriodicapplication(applicationNo,
						periodicApplicationRequest.getTenantId(), periodicApplicationRequest.getRequestInfo());
				applicationList.add(newApplicationNo);

			}

		}

		return applicationList;
		// TODO Auto-generated method stub

	}

	private String createPeriodicapplication(String applicationNo, String tenantId, RequestInfo requestInfo) {
		FSMSearchCriteria fsmSearchCreCriteria = new FSMSearchCriteria();
		List<String> applicationNoList = new ArrayList<String>();
		applicationNoList.add(applicationNo);
		fsmSearchCreCriteria.setApplicationNos(applicationNoList);
		fsmSearchCreCriteria.setTenantId(tenantId);
		FSMResponse fsmResponse = FSMsearch(fsmSearchCreCriteria, requestInfo);
		FSM fsm = fsmResponse.getFsm().get(0);
		fsm.setApplicationType(FSMConstants.PERIODIC_SERVICE);
		fsm.setOldApplicationNo(fsm.getApplicationNo());
		fsm.setApplicationNo(null);
		fsm.getAddress().setId(null);
		fsm.getAddress().getGeoLocation().setId(null);
		fsm.setId(null);
		fsm.getPitDetail().setId(null);
		FSMRequest fsmRequest = new FSMRequest();
		fsmRequest.setFsm(fsm);
		fsmRequest.setRequestInfo(requestInfo);
		FSM fsmData = create(fsmRequest);
		return fsmData.getApplicationNo();

	}

	/**
	 * Check if user has DSO role or not
	 * 
	 * @param fsmRequest
	 * @return
	 */
	private boolean hasDsoOrEditorRole(FSMRequest fsmRequest) {
		org.egov.common.contract.request.User dsoUser = fsmRequest.getRequestInfo().getUserInfo();
		boolean isDsoOrEditorAccess = util.isRoleAvailale(dsoUser, FSMConstants.ROLE_FSM_DSO,
				fsmRequest.getRequestInfo().getUserInfo().getTenantId().split("\\.")[0]);
		if (!isDsoOrEditorAccess) {
			isDsoOrEditorAccess = util.isRoleAvailale(dsoUser, FSMConstants.FSM_EDITOR_EMP,
					fsmRequest.getRequestInfo().getUserInfo().getTenantId());
		}
		return isDsoOrEditorAccess;
	}

}
