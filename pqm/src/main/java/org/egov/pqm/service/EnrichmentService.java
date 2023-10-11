package org.egov.pqm.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.pqm.config.ServiceConfiguration;
import org.egov.pqm.repository.IdGenRepository;
import org.egov.pqm.util.ErrorConstants;
import org.egov.pqm.util.PQMUtil;
import org.egov.pqm.web.model.AuditDetails;
import org.egov.pqm.web.model.Test;
import org.egov.pqm.web.model.TestRequest;
import org.egov.pqm.web.model.idgen.IdResponse;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EnrichmentService {
    @Autowired
    private ServiceConfiguration config;
    @Autowired
    private PQMUtil pqmUtil;
    @Autowired
    private IdGenRepository idGenRepository;

    public void enrichPQMCreateRequest(TestRequest testRequest)
    {
        RequestInfo requestInfo = testRequest.getRequestInfo();
        setIdgenIds(testRequest);
        setAuditDetails(testRequest);
    }
    private void setAuditDetails(TestRequest testRequest)
    {
        RequestInfo requestInfo = testRequest.getRequestInfo();
        AuditDetails auditDetails = pqmUtil.getAuditDetails(requestInfo.getUserInfo().getUuid(),true);
        testRequest.getTests().get(0).setAuditDetails(auditDetails);
    }

    private void setIdgenIds(TestRequest testRequest) {
        log.info(testRequest.toString(),"xxxxxxxxxxxxx");
        RequestInfo requestInfo = testRequest.getRequestInfo();
        String tenantId = testRequest.getTests().get(0).getTenantId();
       List<Test> test = testRequest.getTests();
        String id = getId(requestInfo, tenantId, config.getIdName(), config.getIdFormat());
        test.get(0).setId(id);
    }


    private String getId(RequestInfo requestInfo, String tenantId, String idKey, String idFormat) {
        IdResponse idResponse = idGenRepository.getId(requestInfo, tenantId, idKey, idFormat).getIdResponses().get(0);
        if (idResponse == null) {
            throw new CustomException(ErrorConstants.IDGEN_ERROR, "No ids returned from idgen Service");
        }
        return idResponse.getId();
    }

}
