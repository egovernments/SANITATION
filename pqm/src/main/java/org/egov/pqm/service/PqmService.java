package org.egov.pqm.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.pqm.repository.PqmRepository;
import org.egov.pqm.util.TestConstants;
import org.egov.pqm.web.model.Document;
import org.egov.pqm.web.model.DocumentResponse;
import org.egov.pqm.web.model.Test;
import org.egov.pqm.web.model.TestResponse;
import org.egov.pqm.web.model.TestSearchRequest;
import org.egov.pqm.web.model.plantMapping.PlantMapping;
import org.egov.pqm.web.model.plantMapping.PlantMappingSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class PqmService {

	@Autowired
	private PqmRepository repository;
	
	@Autowired
	private FstpoService fstpoService;

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

//		testResponse.setTests(testList);

		if (!testList.isEmpty()) {
			// enrichmentService.enrichFSMSearch(fsmList, requestInfo,
			// criteria.getTenantId());
		}

		return testResponse;
		
//		List<Test> testList = new LinkedList<>();
//		List<Document> documentList = new LinkedList<>();
//		TestResponse testResponse = null;
//		DocumentResponse documentResponse = null;
//		testResponse = repository.getPqmData(criteria);
//		
//		List<String> idList = testResponse.getTests().stream().map(Test::getId).collect(Collectors.toList());
//		
//		documentResponse = repository.getDocumentData(idList);
//		testList = testResponse.getTests();
//		System.out.println(testList.toString());
//		documentList=documentResponse.getDocuments();
//		for (Test test : testList) {
//			List<Document> documents = new ArrayList<>();
//			for (Document documet : documentList) {
//				if (test.getId().equalsIgnoreCase(documet.getTestId())) {
//					documents.add(documet);
//				}
//					
//				
//			}
//			test.setDocuments(documents);
//
//		}
////		testResponse.setTests(testList);
//		if (!testList.isEmpty()) {
////			enrichmentService.enrichFSMSearch(fsmList, requestInfo, criteria.getTenantId());
//		}
//
//		return testResponse;
	}
	
	private void checkRoleInValidateSearch(TestSearchRequest criteria, RequestInfo requestInfo) {
		List<Role> roles = requestInfo.getUserInfo().getRoles();
		if (roles.stream().anyMatch(role -> Objects.equals(role.getCode(), TestConstants.FSTPO_EMPLOYEE))) {
			List<String> employeeUuid = new ArrayList<>();
			employeeUuid.add(requestInfo.getUserInfo().getUuid());

			PlantMappingSearchCriteria plantMappingSearchCriteria = PlantMappingSearchCriteria.builder()
					.employeeUuid(employeeUuid).tenantId(criteria.getTestSearchCriteria().getTenantId()).build();

			PlantMapping plantMapping = fstpoService.getPlantMapping(plantMappingSearchCriteria, requestInfo);
			
		if(plantMapping != null ){
			List<String> plantcodes = new ArrayList<>();
			plantcodes.add(plantMapping.getPlantCode());
			criteria.getTestSearchCriteria().setPlantCodes(plantcodes);
		}

		}

	}

}
