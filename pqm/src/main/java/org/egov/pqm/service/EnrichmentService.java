package org.egov.pqm.service;


import static org.egov.pqm.util.Constants.SUBMITTED;
import static org.egov.pqm.web.model.TestResultStatus.PENDING;

import java.util.List;
import java.util.UUID;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pqm.config.ServiceConfiguration;
import org.egov.pqm.repository.IdGenRepository;
import org.egov.pqm.repository.TestRepository;
import org.egov.pqm.util.Constants;
import org.egov.pqm.util.ErrorConstants;
import org.egov.pqm.web.model.AuditDetails;
import org.egov.pqm.web.model.Document;
import org.egov.pqm.web.model.QualityCriteria;
import org.egov.pqm.web.model.Test;
import org.egov.pqm.web.model.TestRequest;
import org.egov.pqm.web.model.TestResultStatus;
import org.egov.pqm.web.model.Workflow;
import org.egov.pqm.web.model.idgen.IdResponse;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;


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
    setAuditDetails(testRequest, true);
    setWorkflowStatus(testRequest);
    setTestResultStatus(testRequest);
    setDocumentsIdAndTestId(testRequest);
  }

  private void setWorkflowStatus(TestRequest testRequest) {
    testRequest.getTests().get(0).setWfStatus(SUBMITTED);
  }

  public void enrichPQMCreateRequestForLabTest(TestRequest testRequest) {
    RequestInfo requestInfo = testRequest.getRequestInfo();
    setIdgenIds(testRequest);
    setAuditDetails(testRequest, true);
    testRequest.getTests().get(0).setStatus(PENDING);
    setInitialWorkflowAction(testRequest.getTests().get(0));
    setDocumentsIdAndTestId(testRequest);
  }

  public void enrichPQMUpdateRequest(TestRequest testRequest) {
    RequestInfo requestInfo = testRequest.getRequestInfo();
    setAuditDetails(testRequest, false);
    setDocumentsIdAndTestId(testRequest);
  }

  private void setDocumentsIdAndTestId(TestRequest testRequest) {
    List<Document> documentList = testRequest.getTests().get(0).getDocuments();
    for (Document doc : documentList) {
      doc.setTestId(testRequest.getTests().get(0).getId());
      doc.setId(String.valueOf(UUID.randomUUID()));
    }
  }

  void setTestResultStatus(TestRequest testRequest) {
    boolean pass = true;
    for (QualityCriteria criteria : testRequest.getTests().get(0).getQualityCriteria()) {
      if (criteria.getResultStatus() == TestResultStatus.FAIL) {
        pass = false;
      }
    }
    if (pass) {
      testRequest.getTests().get(0).setStatus(TestResultStatus.PASS);
    } else {
      testRequest.getTests().get(0).setStatus(TestResultStatus.FAIL);
    }
  }

  private void setInitialWorkflowAction(Test test) {
    if (test.getWorkflow() == null) {
      String action = Constants.WF_ACTION_SCHEDULE;
      test.setWorkflow(Workflow.builder().action(action).build());
    }
  }

  private void setAuditDetails(TestRequest testRequest, boolean isCreate) {
    RequestInfo requestInfo = testRequest.getRequestInfo();
    AuditDetails auditDetails = null;
    String createdBy = requestInfo.getUserInfo().getUuid();
    Long time = System.currentTimeMillis();
    if (isCreate) {
      auditDetails = AuditDetails.builder().createdBy(createdBy).lastModifiedBy(createdBy)
          .createdTime(time)
          .lastModifiedTime(time)
          .build();
    } else {
      auditDetails = AuditDetails.builder().lastModifiedBy(createdBy).lastModifiedTime(time)
          .createdBy(testRequest.getTests().get(0).getAuditDetails().getCreatedBy())
          .createdTime(testRequest.getTests().get(0).getAuditDetails().getCreatedTime())
          .build();
    }
    testRequest.getTests().get(0).setAuditDetails(auditDetails);
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
      testRepository.saveAnomaly(config.getAnomalyCreateTopic(), testRequest);
    }
  }


}