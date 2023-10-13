package org.egov.pqm.service;

import static org.egov.pqm.util.Constants.PQM_BUSINESS_SERVICE;
import static org.egov.pqm.util.ErrorConstants.UPDATE_ERROR;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import static org.egov.pqm.util.Constants.MASTER_NAME_BENCHMARK_RULES;
import static org.egov.pqm.util.Constants.MASTER_NAME_QUALITY_CRITERIA;
import static org.egov.pqm.util.Constants.MASTER_NAME_TESTING_STANDARD;
import static org.egov.pqm.util.Constants.MODULE_NAME;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestInfo;
import org.egov.pqm.util.ErrorConstants;
import org.egov.pqm.util.MDMSUtils;
import org.egov.pqm.util.QualityCriteriaEvaluation;
import org.egov.pqm.web.model.QualityCriteria;
import org.egov.common.contract.request.Role;
import org.egov.pqm.repository.TestRepository;
import org.egov.pqm.util.Constants;
import org.egov.pqm.util.MDMSUtils;
import org.egov.pqm.util.QualityCriteriaEvaluation;
import org.egov.pqm.web.model.Document;
import org.egov.pqm.web.model.DocumentResponse;
import org.egov.pqm.web.model.QualityCriteria;
import org.egov.pqm.web.model.Test;
import org.egov.pqm.web.model.TestRequest;
import org.egov.pqm.web.model.TestRequest;
import org.egov.pqm.web.model.TestResponse;
import org.egov.pqm.web.model.TestSearchCriteria;
import org.egov.pqm.web.model.TestSearchCriteria;
import org.egov.pqm.web.model.TestSearchRequest;
import org.egov.pqm.web.model.TestType;
import org.egov.pqm.web.model.workflow.BusinessService;
import org.egov.pqm.workflow.ActionValidator;
import org.egov.pqm.workflow.WorkflowIntegrator;
import org.egov.pqm.workflow.WorkflowService;
import org.egov.tracer.model.CustomException;
import org.egov.pqm.web.model.mdms.MDMSQualityCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.egov.pqm.web.model.mdms.MDMSQualityCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	private MDMSUtils mdmsUtils;

  @Autowired
  private QualityCriteriaEvaluation qualityCriteriaEvaluation;

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

		}

	}


  public Test create(TestRequest testRequest) {
    //updating workflow during create
    workflowIntegrator.callWorkFlow(testRequest);
    qualityCriteriaEvaluation.evalutateQualityCriteria(testRequest);
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

    Test test = testRequest.getTests().get(0);

    //Fetching actions from businessService
    BusinessService businessService = workflowService.getBusinessService(test, testRequest, PQM_BUSINESS_SERVICE, null);
    actionValidator.validateUpdateRequest(testRequest, businessService);

    //updating workflow during update
    workflowIntegrator.callWorkFlow(testRequest);

    if (test.getId() == null) {
      throw new CustomException(UPDATE_ERROR,
          "Application Not found in the System" + test);
    }

    if (test.getTestType().equals(TestType.LAB)) {
      if (test.getWorkflow() == null || test.getWorkflow().getAction() == null) {
        throw new CustomException(UPDATE_ERROR,
            "Workflow action cannot be null." + String.format("{Workflow:%s}",
                test.getWorkflow()));
      }
    }

    return testRequest.getTests().get(0);
  }

}
