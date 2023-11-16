package org.egov.pqm.service;


import static org.egov.pqm.util.Constants.UPDATE_RESULT;
import static org.egov.pqm.util.Constants.WFSTATUS_SUBMITTED;
import static org.egov.pqm.web.model.TestResultStatus.PENDING;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
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
import org.springframework.util.CollectionUtils;


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
    Test test = testRequest.getTests().get(0);
    UUID uuid = UUID.randomUUID();
    test.setId(uuid.toString());
    setIdgenIds(testRequest);
    setAuditDetails(testRequest, true);
    setWorkflowStatus(testRequest);
    enrichDocument(testRequest, true);
    setTestCriteriaDetails(testRequest);
    setScheduledDate(testRequest);
  }

  private void setScheduledDate(TestRequest testRequest) {
    Long time = System.currentTimeMillis();
    testRequest.getTests().get(0).setScheduledDate(time);
  }

  private void setWorkflowStatus(TestRequest testRequest) {
    testRequest.getTests().get(0).setWfStatus(WFSTATUS_SUBMITTED);
  }

  public void enrichPQMCreateRequestForLabTest(TestRequest testRequest) {
    RequestInfo requestInfo = testRequest.getRequestInfo();
    Test test = testRequest.getTests().get(0);
    UUID uuid = UUID.randomUUID();
    test.setId(uuid.toString());
    setIdgenIds(testRequest);
    setAuditDetails(testRequest, true);
    testRequest.getTests().get(0).setStatus(PENDING);
    setInitialWorkflowAction(testRequest.getTests().get(0));
    setTestCriteriaDetails(testRequest);
  }

  public void enrichPQMUpdateRequest(TestRequest testRequest) {
    RequestInfo requestInfo = testRequest.getRequestInfo();
    setAuditDetails(testRequest, false);
    if (Objects.equals(testRequest.getTests().get(0).getWorkflow().getAction(), UPDATE_RESULT)) {
      enrichDocument(testRequest, false);
    }
  }

  private void enrichDocument(TestRequest testRequest, boolean isCreate) {
    if (testRequest.getTests().get(0).getDocuments() != null && !testRequest.getTests().get(0)
        .getDocuments().isEmpty()) {
      for (Document document : testRequest.getTests().get(0).getDocuments()) {
        if (document.getFileStoreId() != null) {
          AuditDetails auditDetails = setAuditDetails(testRequest, isCreate);
          document.setTestId(testRequest.getTests().get(0).getTestId());
          document.setId(String.valueOf(UUID.randomUUID()));
          document.setAuditDetails(auditDetails);
        }
      }
    }
  }

  private void setTestCriteriaDetails(TestRequest testRequest) {
    List<QualityCriteria> qualityCriteriaList = testRequest.getTests().get(0).getQualityCriteria();
    for (QualityCriteria qualityCriteria : qualityCriteriaList) {
      qualityCriteria.setTestId(testRequest.getTests().get(0).getTestId());
      qualityCriteria.setId(String.valueOf(UUID.randomUUID()));
      qualityCriteria.setResultStatus(PENDING);
    }
  }

  public void setTestResultStatus(TestRequest testRequest) {
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

  private AuditDetails setAuditDetails(TestRequest testRequest, boolean isCreate) {
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

    //setting quality criteria AuditDetails
    for (QualityCriteria criteria : testRequest.getTests().get(0).getQualityCriteria()) {
      criteria.setAuditDetails(auditDetails);
    }
    return auditDetails;
  }


  private void setIdgenIds(TestRequest testRequest) {
    String id = getId(testRequest.getRequestInfo(), testRequest.getTests().get(0).getTenantId(),
        config.getIdName(), config.getIdFormat());
    testRequest.getTests().get(0).setTestId(id);
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