package org.egov.pqm.service;

import static org.egov.pqm.util.Constants.PQM_BUSINESS_SERVICE;
import static org.egov.pqm.util.Constants.PQM_MODULE_NAME;
import static org.egov.pqm.util.ErrorConstants.UPDATE_ERROR;

import java.util.ArrayList;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import java.util.Objects;
import java.util.stream.Collectors;
import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.pqm.repository.TestRepository;
import org.egov.pqm.util.Constants;
import org.egov.pqm.util.ErrorConstants;
import org.egov.pqm.util.MDMSUtils;
import org.egov.pqm.web.model.Document;
import org.egov.pqm.web.model.DocumentResponse;
import org.egov.common.contract.request.Role;
import org.egov.pqm.repository.TestRepository;
import org.egov.pqm.util.Constants;
import org.egov.pqm.util.MDMSUtils;
import org.egov.pqm.web.model.Document;
import org.egov.pqm.web.model.DocumentResponse;
import org.egov.pqm.repository.TestRepository;
import org.egov.common.contract.request.Role;
import org.egov.pqm.repository.TestRepository;
import org.egov.pqm.util.Constants;
import org.egov.pqm.util.MDMSUtils;
import org.egov.pqm.web.model.Document;
import org.egov.pqm.web.model.DocumentResponse;
import org.egov.pqm.web.model.Test;
import org.egov.pqm.web.model.TestRequest;
import org.egov.pqm.web.model.TestResponse;
import org.egov.pqm.web.model.TestSearchCriteria;
import org.egov.pqm.web.model.TestSearchCriteria;
import org.egov.pqm.web.model.TestSearchRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PqmService {

  @Autowired
  private TestRepository repository;

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

		}

	}





	public Test create(TestRequest testRequest) {
		RequestInfo requestInfo = testRequest.getRequestInfo();
//		Map<String, Map<String, JSONArray>> mdmsData = mdmsUtils.fetchMdmsData(requestInfo,
//				testRequest.getTests().get(0).getTenantId(),PQM_MODULE_NAME,);
//		if (fsmRequest.getFsm().getTenantId().split("\\.").length == 1) {
//			throw new CustomException(FSMErrorConstants.INVALID_TENANT, " Application cannot be created at StateLevel");
//		}
//		testValidator.validateCreate(fsmRequest, mdmsData);
//		enrichmentService.enrichFSMCreateRequest(fsmRequest);
//		wfIntegrator.callWorkFlow(fsmRequest);
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

		RequestInfo requestInfo = testRequest.getRequestInfo();
		Test test = testRequest.getTests().get(0);

//		Map<String, Map<String, JSONArray>> mdmsData = mdmsUtils.fetchMdmsData(requestInfo, test.getTenantId());

		if (test.getId() == null) {
			throw new CustomException(UPDATE_ERROR,
					"Application Not found in the System" + test);
		}

		if (test.getTestType() == "LAB") {
			if (testRequest.getWorkflow() == null || testRequest.getWorkflow().getAction() == null) {
				throw new CustomException(UPDATE_ERROR,
						"Workflow action cannot be null." + String.format("{Workflow:%s}",
								testRequest.getWorkflow()));
			}
		}


		//add qualitycriteria check
		//BusinessService businessService = workflowService.getBusinessService(test, testRequest.getRequestInfo(),"TQM");
		//actionValidator.validateUpdateRequest(fsmRequest, businessService);
		//testValidator.validateUpdate(testRequest, mdmsData);
		//enrichmentService.enrichFSMUpdateRequest(testRequest, oldFSM);
		//wfIntegrator.callWorkFlow(fsmRequest);

//		repository.update(testRequest, workflowService.isStateUpdatable(test.getStatus(), PQM_BUSINESS_SERVICE));
		return testRequest.getTests().get(0);
	}


}
