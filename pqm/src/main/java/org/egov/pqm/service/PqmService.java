package org.egov.pqm.service;

import static org.egov.pqm.util.Constants.*;
import static org.egov.pqm.util.ErrorConstants.TEST_NOT_IN_DB;
import static org.egov.pqm.util.ErrorConstants.UPDATE_ERROR;
import static org.egov.pqm.util.MDMSUtils.parseJsonToTestList;
import static org.egov.pqm.web.model.Pagination.SortOrder.DESC;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pqm.util.ErrorConstants;
import org.egov.pqm.util.MDMSUtils;
import org.egov.common.contract.request.Role;
import org.egov.pqm.repository.TestRepository;
import org.egov.pqm.util.Constants;
import org.egov.pqm.validator.MDMSValidator;
import org.egov.pqm.web.model.*;
import org.egov.pqm.web.model.Pagination.SortBy;
import org.egov.pqm.web.model.mdms.MdmsTest;
import org.egov.pqm.validator.PqmValidator;
import org.egov.pqm.web.model.workflow.BusinessService;
import org.egov.pqm.workflow.ActionValidator;
import org.egov.pqm.workflow.WorkflowIntegrator;
import org.egov.pqm.workflow.WorkflowService;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.egov.pqm.validator.PqmValidator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

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
  private PqmValidator pqmValidator;

  @Autowired
  private MDMSUtils mdmsUtils;

  @Autowired
  private QualityCriteriaEvaluationService qualityCriteriaEvaluation;

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
    List<String> idList = testResponse.getTests().stream().map(Test::getId)
        .collect(Collectors.toList());

    List<QualityCriteria> qualityCriteriaList = repository.getQualityCriteriaData(idList);

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

  
  /**
   * Creates Test
   *
   * @param testRequest The Create Request
   * @return New Test
   */
  public Test create(TestRequest testRequest) {
    pqmValidator.validateTestRequestBody(testRequest);
    mdmsValidator.validateMdmsData(testRequest);
    qualityCriteriaEvaluation.evalutateQualityCriteria(testRequest);
    enrichmentService.enrichPQMCreateRequest(testRequest);
    enrichmentService.pushToAnomalyDetectorIfTestResultStatusFail(testRequest);
    repository.save(testRequest);
    return testRequest.getTests().get(0);
  }

  
   public Test createTestViaScheduler(TestRequest testRequest) {
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
      throw new CustomException(TEST_NOT_IN_DB,
          "test not present in database which we want to update ");
    }
    mdmsValidator.validateMdmsData(testRequest);
    // Fetching actions from businessService
    BusinessService businessService = workflowService.getBusinessService(test, testRequest,
        PQM_BUSINESS_SERVICE,
        null);
    actionValidator.validateUpdateRequest(testRequest, businessService);
    if (test.getWorkflow().getAction().equals(UPDATE_RESULT)) { 
      // calculate test result
			qualityCriteriaEvaluation.evalutateQualityCriteria(testRequest);
      enrichmentService.setTestResultStatus(testRequest);
      enrichmentService.pushToAnomalyDetectorIfTestResultStatusFail(testRequest);
    }
    enrichmentService.enrichPQMUpdateRequest(testRequest); // enrich update request
    workflowIntegrator.callWorkFlow(testRequest);// updating workflow during update
    repository.update(testRequest);
    return testRequest.getTests().get(0);
  }

  /**
   * Schedules Test
   *
   */
  public void scheduleTest(RequestInfo requestInfo) {

    // get mdms TestStandardData
    //fetch mdms data for TestStandard Master
    Object jsondata = mdmsUtils.mdmsCallV2(requestInfo,
        "pg", SCHEMA_CODE_TEST_STANDARD);
    String jsonString = "";

    try {
      ObjectMapper objectMapper = new ObjectMapper();
      jsonString = objectMapper.writeValueAsString(jsondata);
    } catch (Exception e) {
      throw new CustomException(ErrorConstants.PARSING_ERROR,
          "Unable to parse QualityCriteria mdms data ");
    }

    List<MdmsTest> mdmsTestList = parseJsonToTestList(jsonString);

    for(MdmsTest mdmsTest: mdmsTestList)
    {
      TestSearchCriteria testSearchCriteria = TestSearchCriteria.builder().testType(
          String.valueOf(TestType.LAB)).wfStatus(Arrays.asList(WFSTATUS_PENDINGRESULTS, WFSTATUS_SCHEDULED)).testCode(Collections.singletonList(mdmsTest.getCode())).build();
      Pagination pagination = Pagination.builder().limit(1).sortBy(SortBy.scheduledDate)
          .sortOrder(DESC).build();
      TestSearchRequest testSearchRequest = TestSearchRequest.builder().requestInfo(requestInfo)
          .testSearchCriteria(testSearchCriteria).pagination(pagination).build();

      //search from DB for any pending tests
      List<Test> testListFromDb = testSearch(testSearchRequest, requestInfo).getTests();

      int frequency = Integer.parseInt(mdmsTest.getFrequency().split("_")[0]);

      LocalDate currentDate = LocalDate.now();
      LocalDate calculatedDate = currentDate.plusDays(frequency);
      Instant instant = calculatedDate.atStartOfDay(ZoneId.systemDefault()).toInstant();


      List<QualityCriteria> qualityCriteriaList = new ArrayList<>();

      for(String mdmsQualityCriteria : mdmsTest.getQualityCriteria())
      {
        QualityCriteria qualityCriteria = QualityCriteria.builder().criteriaCode(mdmsQualityCriteria).resultStatus(TestResultStatus.PENDING).isActive(Boolean.TRUE).build();
        qualityCriteriaList.add(qualityCriteria);
      }

      if(CollectionUtils.isEmpty(testListFromDb))
      {
        //case 1: when no pending tests exist in DB
        Test createTest = Test.builder()
            .testCode(mdmsTest.getCode())
            .tenantId("pg.citya")
            .plantCode(mdmsTest.getPlant())
            .processCode(mdmsTest.getProcess())
            .stageCode(mdmsTest.getStage())
            .materialCode(mdmsTest.getMaterial())
            .qualityCriteria(qualityCriteriaList)
            .testType(TestType.LAB)
            .isActive(Boolean.TRUE)
            .scheduledDate(instant.toEpochMilli())
            .build();

        TestRequest testRequest = TestRequest.builder().tests(Collections.singletonList(createTest)).requestInfo(requestInfo).build();

        //send to create function
        createTestViaScheduler(testRequest);
      }
      else {
        //case 2: when pending test exist in DB
        Test testFromDb = testListFromDb.get(0);

        Long scheduleDate = testFromDb.getScheduledDate();

        if(isPastScheduledDate(scheduleDate))
        {
          Test createTest = Test.builder()
              .tenantId(testFromDb.getTenantId())
              .testCode(mdmsTest.getCode())
              .plantCode(testFromDb.getPlantCode())
              .processCode(testFromDb.getProcessCode())
              .stageCode(testFromDb.getStageCode())
              .materialCode(testFromDb.getMaterialCode())
              .qualityCriteria(qualityCriteriaList)
              .testType(TestType.LAB)
              .isActive(Boolean.TRUE)
              .scheduledDate(instant.toEpochMilli())
              .build();

          TestRequest testRequest = TestRequest.builder().tests(Collections.singletonList(createTest)).requestInfo(requestInfo)
              .build();

          //send to create function
          createTestViaScheduler(testRequest);
        }
      }


    }

  }

  public static boolean isPastScheduledDate(Long scheduleDateEpoch) {
    // Convert epoch time to LocalDate for scheduleDate
    LocalDate scheduledDate = Instant.ofEpochMilli(scheduleDateEpoch)
        .atZone(ZoneId.systemDefault())
        .toLocalDate();

    // Get today's date
    LocalDate currentDate = LocalDate.now();

    // Check if the currentDate date is after or equal to the scheduled date
    return (currentDate.isAfter(scheduledDate) || currentDate.isEqual(scheduledDate));
  }

  /**
   * Creates Scheduled Tests
   *
   * @param testRequest The Create Request
   * @return New Test
   */
  public Test buildTestObject(TestRequest testRequest, Object mdmsData) {
    RequestInfo requestInfo = testRequest.getRequestInfo();
    repository.save(testRequest);
    return testRequest.getTests().get(0);
  }


}
