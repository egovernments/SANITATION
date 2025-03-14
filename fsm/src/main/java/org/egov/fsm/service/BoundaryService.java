package org.egov.fsm.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.egov.fsm.config.FSMConfiguration;
import org.egov.fsm.repository.ServiceRequestRepository;
import org.egov.fsm.util.FSMErrorConstants;
import org.egov.fsm.web.model.FSM;
import org.egov.fsm.web.model.FSMRequest;
import org.egov.fsm.web.model.RequestInfoWrapper;
import org.egov.fsm.web.model.location.Boundary;
import org.egov.tracer.model.CustomException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BoundaryService {

	private ServiceRequestRepository serviceRequestRepository;

	private ObjectMapper mapper;

	private FSMConfiguration config;

	@Autowired
	public BoundaryService(ServiceRequestRepository serviceRequestRepository, ObjectMapper mapper,
			FSMConfiguration config) {
		this.serviceRequestRepository = serviceRequestRepository;
		this.mapper = mapper;
		this.config = config;
	}

	/**
	 * Enriches the locality object by calling the location service
	 * 
	 * @param request
	 *            FSMRequest for create
	 * 
	 */
	public void getAreaType(FSMRequest request) {

		if (request.getFsm() == null) {
			return;
		}

		String hierarchyTypeCode = getHierarchyTypeCode(request);

		FSM fsm = request.getFsm();

		String tenantId = request.getFsm().getTenantId();

		if (fsm.getAddress() == null || fsm.getAddress().getLocality() == null) {
			throw new CustomException(FSMErrorConstants.INVALID_ADDRES, "The address or locality cannot be null");
		}

		StringBuilder uri = new StringBuilder(config.getLocationHost());
		uri.append(config.getLocationContextPath()).append(config.getLocationEndpoint());
		uri.append("?").append("tenantId=").append(tenantId);

		if (hierarchyTypeCode != null) {
			uri.append("&").append("hierarchyType=").append(hierarchyTypeCode);
		}
		/*
		 * commented to enable urc
		 */
//		uri.append("&").append("boundaryType=").append("Locality");
		uri.append("&").append("boundaryType=");

		Object additionalDetail = fsm.getAddress().getAdditionalDetails();
		Map<String, String> additionalDetails = null;
		if (additionalDetail instanceof Map) {
			additionalDetails = additionalDetail != null ? (Map<String, String>) additionalDetail : new HashMap<>();
		}
		if (additionalDetails != null && additionalDetails.get("boundaryType") != null) {
			String boundaryType = (String) additionalDetails.get("boundaryType");
			uri.append(boundaryType);
		} else {

			uri.append("Locality");
		}
		uri.append("&").append("codes=").append(fsm.getAddress().getLocality().getCode());

		RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(request.getRequestInfo()).build();
		LinkedHashMap responseMap = (LinkedHashMap) serviceRequestRepository.fetchResult(uri, wrapper);

		if (CollectionUtils.isEmpty(responseMap)) {
			throw new CustomException(FSMErrorConstants.BOUNDARY_ERROR,
					"The response from location service is empty or null");
		}


		String jsonString1 = new JSONObject(responseMap).toString();

		DocumentContext context = JsonPath.parse(jsonString1);

		List<Boundary> boundaryResponse = context
				.read("$..boundary[?(@.code==\"{}\")]".replace("{}", fsm.getAddress().getLocality().getCode()));
		if (boundaryResponse.isEmpty() && CollectionUtils.isEmpty((boundaryResponse))) {
			log.debug("The boundary data was not found");
			throw new CustomException(FSMErrorConstants.BOUNDARY_MDMS_DATA_ERROR, "The boundary data was not found");
		}

		Boundary boundary = mapper.convertValue(boundaryResponse.stream().findFirst(), Boundary.class);

		if (boundary.getCode().isEmpty()) {

			throw new CustomException(FSMErrorConstants.INVALID_BOUNDARY_DATA,
					"The boundary data for the code " + fsm.getAddress().getLocality().getCode() + " is not available");
		}

		fsm.getAddress().setLocality(boundary);

	}

	private String getHierarchyTypeCode(FSMRequest fsmRequest) {
		
		String hierarchyTypeCode = null;
		
		if (fsmRequest.getFsm() != null && fsmRequest.getFsm().getAddress() != null) {
			Object additionalDetails = fsmRequest.getFsm().getAddress().getAdditionalDetails();

			if (additionalDetails instanceof HashMap) {
				HashMap<String, String> detailsMap = (HashMap<String, String>) additionalDetails;
				String boundaryType = detailsMap.get("boundaryType");

				if ("Locality".equals(boundaryType)) {
					hierarchyTypeCode= config.getHierarchyTypeLocalityCode();
				} else {
					hierarchyTypeCode=config.getHierarchyTypeGpCode();
				}
			}
		}
		return hierarchyTypeCode;
	}

}
