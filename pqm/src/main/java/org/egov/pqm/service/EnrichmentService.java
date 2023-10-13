package org.egov.pqm.service;


import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.pqm.config.ServiceConfiguration;
import org.egov.pqm.repository.IdGenRepository;
import org.egov.pqm.repository.TestRepository;
import org.egov.pqm.util.Constants;
import org.egov.pqm.util.ErrorConstants;
import org.egov.pqm.web.model.AuditDetails;
import org.egov.pqm.web.model.QualityCriteria;
import org.egov.pqm.web.model.QualityCriteria.StatusEnum;
import org.egov.pqm.web.model.Test;
import org.egov.pqm.web.model.TestRequest;
import org.egov.pqm.web.model.TestResultStatus;
import org.egov.pqm.web.model.Workflow;
import org.egov.pqm.web.model.idgen.IdResponse;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class EnrichmentService {

  @Autowired
  private ServiceConfiguration config;

  @Autowired
  private IdGenRepository idGenRepository;
  @Autowired
  private TestRepository testRepository;

  public void enrichPQMCreateRequest(TestRequest testRequest) {
    RequestInfo requestInfo = testRequest.getRequestInfo();
    setIdgenIds(testRequest);
    setAuditDetails(testRequest);
    setWorkflow(testRequest.getTests().get(0));
    setTestResultStatus(testRequest);
  }

  private void setTestResultStatus(TestRequest testRequest) {
    boolean pass = true;
    for (QualityCriteria criteria : testRequest.getTests().get(0).getQualityCriteria()) {
      if (criteria.getStatus() == StatusEnum.FAIL) {
        pass = false;
      }
    }
    if (pass) {
      testRequest.getTests().get(0).setStatus(TestResultStatus.PASS);
    } else {
      testRequest.getTests().get(0).setStatus(TestResultStatus.FAIL);
    }
  }

  private void setWorkflow(Test test) {
    if (test.getWorkflow() == null) {
      String action = Constants.WF_ACTION_SCHEDULE;
      test.setWorkflow(Workflow.builder().action(action).build());
    }
  }

  private void setAuditDetails(TestRequest testRequest) {
    RequestInfo requestInfo = testRequest.getRequestInfo();
    AuditDetails auditDetails = getAuditDetails(requestInfo.getUserInfo().getUuid(), true);
    testRequest.getTests().get(0).setAuditDetails(auditDetails);
  }

  public AuditDetails getAuditDetails(String by, Boolean isCreate) {
    Long time = System.currentTimeMillis();
    if (isCreate) {
      return AuditDetails.builder().createdBy(by).lastModifiedBy(by).createdTime(time)
          .lastModifiedTime(time)
          .build();
    } else {
      return AuditDetails.builder().lastModifiedBy(by).lastModifiedTime(time).build();
    }
  }

  private void setIdgenIds(TestRequest testRequest) {
    String id = getId(testRequest.getRequestInfo(), testRequest.getTests().get(0).getTenantId(),
        config.getIdName(), config.getIdFormat());
    testRequest.getTests().get(0).setId(id);
  }


  private String getId(RequestInfo requestInfo, String tenantId, String idKey, String idFormat) {
    IdResponse idResponse = idGenRepository.getId(requestInfo, tenantId, idKey, idFormat)
        .getIdResponses().get(0);
    if (idResponse == null) {
      throw new CustomException(ErrorConstants.IDGEN_ERROR, "No ids returned from idgen Service");
    }
    return idResponse.getId();
  }

  public void pushToAnomalyDetectorIfTestResultStatusFail(TestRequest testRequest) {
    if (testRequest.getTests().get(0).getStatus() == TestResultStatus.FAIL) {
      testRepository.save(config.getAnomalyCreateTopic(), testRequest);
    }
  }

}