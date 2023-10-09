package org.egov.pqm.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.pqm.repository.PqmRepository;
import org.egov.pqm.util.Constants;
import org.egov.pqm.util.MDMSUtils;
import org.egov.pqm.web.model.Document;
import org.egov.pqm.web.model.DocumentResponse;
import org.egov.pqm.web.model.Test;
import org.egov.pqm.web.model.TestResponse;
import org.egov.pqm.web.model.TestSearchCriteria;
import org.egov.pqm.web.model.TestSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PqmService {

	@Autowired
	private PqmRepository repository;

	@Autowired
	private MDMSUtils mdmsUtils;

	/**
	 * search the PQM applications based on the search criteria
	 * 
	 * @param criteria
	 * @param requestInfo
	 * @return
	 */
	public TestResponse testSearch(TestSearchRequest criteria, RequestInfo requestInfo) {

		List<Test> testList = new LinkedList<>();

		if (requestInfo.getUserInfo().getType().equalsIgnoreCase("Employee")) {
			checkRoleInValidateSearch(criteria, requestInfo);
		}
		TestResponse testResponse = repository.getPqmData(criteria);
		List<String> idList = testResponse.getTests().stream().map(Test::getId).collect(Collectors.toList());

		DocumentResponse documentResponse = repository.getDocumentData(idList);
		List<Document> documentList = documentResponse.getDocuments();

		testList = testResponse.getTests().stream().map(test -> {
			List<Document> documents = documentList.stream()
					.filter(document -> test.getId().equalsIgnoreCase(document.getTestId()))
					.collect(Collectors.toList());
			test.setDocuments(documents);
			return test;
		}).collect(Collectors.toList());

		return testResponse;

	}

	private void checkRoleInValidateSearch(TestSearchRequest criteria, RequestInfo requestInfo) {
		List<Role> roles = requestInfo.getUserInfo().getRoles();
		TestSearchCriteria testSearchCriteria = criteria.getTestSearchCriteria();
		List<String> masterNameList = new ArrayList<>();
		masterNameList.add(null);
		if (roles.stream().anyMatch(role -> Objects.equals(role.getCode(), Constants.FSTPO_EMPLOYEE))) {

			// fetch list of quality standards for the test
//			List<String> masterList = new ArrayList<>(
//					Arrays.asList(
//							
//							MASTER_NAME_QUALITY_CRITERIA
//					));
//			Map<String, Map<String, JSONArray>> response = mdmsUtils.fetchMdmsData(requestInfo, requestInfo.getUserInfo().getTenantId(), "PQM", masterList);
//			String jsondata = response.get("PQM").get("MASTER_NAME_QUALITY_CRITERIA").toString();
//			PlantMappingSearchCriteria plantMappingSearchCriteria = PlantMappingSearchCriteria.builder()
//					.employeeUuid(employeeUuid).tenantId(criteria.getTestSearchCriteria().getTenantId()).build();
//
//			PlantMapping plantMapping = fstpoService.getPlantMapping(plantMappingSearchCriteria, requestInfo);

//		if(plantMapping != null ){
//			List<String> plantcodes = new ArrayList<>();
//			plantcodes.add(plantMapping.getPlantCode());
//			criteria.getTestSearchCriteria().setPlantCodes(plantcodes);
//		}

		}

	}

}
