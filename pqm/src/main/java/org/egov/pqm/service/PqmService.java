package org.egov.pqm.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pqm.repository.PqmRepository;
import org.egov.pqm.web.model.Document;
import org.egov.pqm.web.model.DocumentResponse;
import org.egov.pqm.web.model.Test;
import org.egov.pqm.web.model.TestResponse;
import org.egov.pqm.web.model.TestSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PqmService {

	@Autowired
	private PqmRepository repository;

	public TestResponse testSearch(TestSearchRequest criteria, RequestInfo requestInfo) {
		
		List<Test> testList = new LinkedList<>();
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

}
