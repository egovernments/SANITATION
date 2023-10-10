package org.egov.pqm.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.pqm.config.ServiceConfiguration;
import org.egov.pqm.repository.IdGenRepository;
import org.egov.pqm.util.ErrorConstants;
import org.egov.pqm.web.model.Test;
import org.egov.pqm.web.model.TestRequest;
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

    public void enrichPQMCreateRequest(TestRequest testRequest)
    {
        RequestInfo requestInfo = testRequest.getRequestInfo();
        setIdgenIds(testRequest);
    }

    private void setIdgenIds(TestRequest testRequest) {
        RequestInfo requestInfo = testRequest.getRequestInfo();
        String tenantId = testRequest.getTests().get(0).getTenantId();
        Test test = testRequest.getTests().get(0);
        String id = getId(requestInfo, tenantId, config.getIdName(), config.getIdFormat());
        test.setId(id);
    }


    private String getId(RequestInfo requestInfo, String tenantId, String idKey, String idFormat) {
        IdResponse idResponse = idGenRepository.getId(requestInfo, tenantId, idKey, idFormat).getIdResponses().get(0);
        if (idResponse == null) {
            throw new CustomException(ErrorConstants.IDGEN_ERROR, "No ids returned from idgen Service");
        }
        return idResponse.getId();
    }

}
