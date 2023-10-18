package org.egov.pqm.service;

import static org.egov.pqm.util.Constants.PQM_BUSINESS_SERVICE;
import static org.egov.pqm.util.Constants.UPDATE_RESULT;
import static org.egov.pqm.util.ErrorConstants.TEST_NOT_IN_DB;
import static org.egov.pqm.util.ErrorConstants.UPDATE_ERROR;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.pqm.repository.TestRepository;
import org.egov.pqm.util.Constants;
import org.egov.pqm.util.MDMSUtils;
import org.egov.pqm.util.QualityCriteriaEvaluation;
import org.egov.pqm.validator.MDMSValidator;
import org.egov.pqm.web.model.Document;
import org.egov.pqm.web.model.DocumentResponse;
import org.egov.pqm.web.model.QualityCriteria;
import org.egov.pqm.web.model.Test;
import org.egov.pqm.web.model.TestRequest;
import org.egov.pqm.web.model.TestResponse;
import org.egov.pqm.web.model.TestSearchCriteria;
import org.egov.pqm.web.model.TestSearchRequest;
import org.egov.pqm.web.model.TestType;
import org.egov.pqm.web.model.workflow.BusinessService;
import org.egov.pqm.workflow.ActionValidator;
import org.egov.pqm.workflow.WorkflowIntegrator;
import org.egov.pqm.workflow.WorkflowService;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PqmService {

	@Autowired
	private WorkflowIntegrator workflowIntegrator;

	@Autowired
	private WorkflowService workflowService;

	@Autowired
	private ActionValidator actionValidator;

	@Autowired
	private TestRepository repository;

	@Autowired
	private EnrichmentService enrichmentService;

	@Autowired
	private MDMSUtils mdmsUtils;

	@Autowired
	private QualityCriteriaEvaluation qualityCriteriaEvaluation;

	@Autowired
	private MDMSValidator mdmsValidator;

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

		List<QualityCriteria> qualityCriteriaList =repository.getQualityCriteriaData(idList);

		testList = testResponse.getTests().stream().map(test -> {
			List<QualityCriteria> QualityCriterias = qualityCriteriaList.stream()
					.filter(qualityCriteria -> test.getId().equalsIgnoreCase(qualityCriteria.getTestId()))
					.collect(Collectors.toList());
			test.setQualityCriteria(QualityCriterias);
			return test;
		}).collect(Collectors.toList());

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

		}

	}

	public Test create(TestRequest testRequest) {
		mdmsValidator.validateMdmsData(testRequest);
		qualityCriteriaEvaluation.evalutateQualityCriteria(testRequest);
		enrichmentService.enrichPQMCreateRequest(testRequest);
		enrichmentService.pushToAnomalyDetectorIfTestResultStatusFail(testRequest);
		repository.save(testRequest);
		return testRequest.getTests().get(0);
	}

	public Test scheduleTest(TestRequest testRequest) {
		mdmsValidator.validateMdmsData(testRequest);
		enrichmentService.enrichPQMCreateRequestForLabTest(testRequest);
		workflowIntegrator.callWorkFlow(testRequest);
		repository.save(testRequest);
		return testRequest.getTests().get(0);
	}

	/**
	 * Updates the Test
	 *
	 * @param testRequest The update Request
	 * @return Updated Test
	 */
	@SuppressWarnings("unchecked")
	public Test update(TestRequest testRequest) {

		List<Test> tests = testRequest.getTests();
		Test test = tests.get(0);
		if (test.getId() == null) { // validate if application exists
			throw new CustomException(UPDATE_ERROR, "Application Not found in the System" + test);
		}
		if (test.getTestType().equals(TestType.LAB)) {
			if (test.getWorkflow() == null || test.getWorkflow().getAction() == null) {
				throw new CustomException(UPDATE_ERROR,
						"Workflow action cannot be null." + String.format("{Workflow:%s}", test.getWorkflow()));
			}
		}
		List<Test> oldTests = repository.fetchFromDB(testRequest); // fetching tests from DB
		if (tests.size() != oldTests.size()) // checking for the list of all ids to be present in DB
		{
			throw new CustomException(TEST_NOT_IN_DB, "test not present in database which we want to update ");
		}
		// Fetching actions from businessService
		BusinessService businessService = workflowService.getBusinessService(test, testRequest, PQM_BUSINESS_SERVICE,
				null);
		actionValidator.validateUpdateRequest(testRequest, businessService);
		workflowIntegrator.callWorkFlow(testRequest);// updating workflow during update
		enrichmentService.enrichPQMUpdateRequest(testRequest); // enrich update request
		if (test.getWorkflow().getAction().equals(UPDATE_RESULT)) { // calculate test result
			qualityCriteriaEvaluation.evalutateQualityCriteria(testRequest);
			enrichmentService.setTestResultStatus(testRequest);
			enrichmentService.pushToAnomalyDetectorIfTestResultStatusFail(testRequest);
		}
		repository.update(testRequest);
		return testRequest.getTests().get(0);
	}
}
