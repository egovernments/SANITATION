package org.egov.pqm.service;

import static org.egov.pqm.util.Constants.MASTER_NAME_BENCHMARK_RULES;
import static org.egov.pqm.util.Constants.MASTER_NAME_QUALITY_CRITERIA;
import static org.egov.pqm.util.Constants.MASTER_NAME_TESTING_STANDARD;
import static org.egov.pqm.util.Constants.MODULE_NAME;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import java.util.Map;
import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestInfo;
import org.egov.pqm.util.MDMSUtils;
import org.egov.pqm.util.QualityCriteriaEvaluation;
import org.egov.pqm.web.model.QualityCriteria;
import org.egov.common.contract.request.Role;
import org.egov.pqm.repository.TestRepository;
import org.egov.pqm.util.Constants;
import org.egov.pqm.web.model.Document;
import org.egov.pqm.web.model.DocumentResponse;
import org.egov.pqm.web.model.Test;
import org.egov.pqm.web.model.TestRequest;
import org.egov.pqm.web.model.TestResponse;
import org.egov.pqm.web.model.TestSearchCriteria;
import org.egov.pqm.web.model.TestSearchCriteria;
import org.egov.pqm.web.model.TestSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.egov.pqm.web.model.mdms.MDMSQualityCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PqmService {

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

  public void evalutateCriteria(TestRequest testRequest) {
    Test test = testRequest.getTests().get(0);
    //fetch list of quality standards for the test
    List<String> masterList = new ArrayList<>(
        Arrays.asList(
            MASTER_NAME_TESTING_STANDARD,
            MASTER_NAME_QUALITY_CRITERIA,
            MASTER_NAME_BENCHMARK_RULES
        ));

    Map<String, Map<String, JSONArray>> response = mdmsUtils.fetchMdmsData(
        testRequest.getRequestInfo(), testRequest.getTests().get(0).getTenantId(), MODULE_NAME,
        masterList);

    String jsondata = response.get(MODULE_NAME).get(MASTER_NAME_QUALITY_CRITERIA).toString();

    // Parse JSON and create the map
    Map<String, MDMSQualityCriteria> codeToQualityCriteriaMap = parseJsonToMap(jsondata);

    List<QualityCriteria> evaluatedqualityCriteriaList = new ArrayList<>();
    for (QualityCriteria qualityCriteria : test.getQualityCriteria()) {

      QualityCriteria evaluatedqualityCriteria = qualityCriteriaEvaluation.evaluateQualityCriteria(
          codeToQualityCriteriaMap.get(qualityCriteria.getCriteriaCode()),
          qualityCriteria.getValue());
      evaluatedqualityCriteriaList.add(evaluatedqualityCriteria);
    }
    test.setQualityCriteria(evaluatedqualityCriteriaList);
  }


  public static Map<String, MDMSQualityCriteria> parseJsonToMap(String jsonData) {
    Map<String, MDMSQualityCriteria> codeToQualityCriteriaMap = new HashMap<>();

    try {
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode jsonNode = objectMapper.readTree(jsonData);
      JsonNode qualityCriteriaArray = jsonNode.get("QualityCriteria");

      for (JsonNode criteriaNode : qualityCriteriaArray) {
        String code = criteriaNode.get("code").asText();
        MDMSQualityCriteria qualityCriteria = objectMapper.treeToValue(criteriaNode,
            MDMSQualityCriteria.class);
        codeToQualityCriteriaMap.put(code, qualityCriteria);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return codeToQualityCriteriaMap;
  }

}
