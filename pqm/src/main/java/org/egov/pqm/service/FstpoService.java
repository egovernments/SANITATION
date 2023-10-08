package org.egov.pqm.service;

import java.util.LinkedHashMap;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pqm.config.TestConfiguration;
import org.egov.pqm.repository.ServiceRequestRepository;
import org.egov.pqm.web.model.RequestInfoWrapper;
import org.egov.pqm.web.model.plantMapping.PlantMapping;
import org.egov.pqm.web.model.plantMapping.PlantMappingResponse;
import org.egov.pqm.web.model.plantMapping.PlantMappingSearchCriteria;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class FstpoService {


	@Autowired
	private TestConfiguration config;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private ServiceRequestRepository serviceRequestRepository;


	public PlantMapping getPlantMapping(PlantMappingSearchCriteria plantMappingSearchCriteria, RequestInfo requestInfo) {

		StringBuilder uri = new StringBuilder(config.getPlantMappingHost()).append(config.getPlantMappingContextPath())
				.append(config.getPlantMappingSearchEndpoint()).append("?tenantId=")
				.append(plantMappingSearchCriteria.getTenantId());

		if (!CollectionUtils.isEmpty(plantMappingSearchCriteria.getEmployeeUuid())) {

			uri.append("&employeeuuid=" + String.join(",", plantMappingSearchCriteria.getEmployeeUuid()));
		}

		RequestInfoWrapper requestInfoWrpr = new RequestInfoWrapper();
		requestInfoWrpr.setRequestInfo(requestInfo);
		try {

			LinkedHashMap responseMap = (LinkedHashMap) serviceRequestRepository.fetchResult(uri, requestInfoWrpr);
			PlantMappingResponse plantMappingResponse = mapper.convertValue(responseMap, PlantMappingResponse.class);
			if (!CollectionUtils.isEmpty(plantMappingResponse.getPlantMapping())) {
				return plantMappingResponse.getPlantMapping().get(0);
			} else {
				return null;
			}

		} catch (IllegalArgumentException e) {
			throw new CustomException("IllegalArgumentException",
					"ObjectMapper not able to convertValue in validateDSO");
		}

	}

	



}
