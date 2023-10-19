package org.egov.pqm.validator;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.pqm.util.Constants;
import org.egov.pqm.util.Constants.*;
import org.egov.pqm.util.ErrorConstants;
import org.egov.pqm.util.MDMSUtils;
import org.egov.pqm.web.model.QualityCriteria;
import org.egov.pqm.web.model.Test;
import org.egov.pqm.web.model.TestRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static org.egov.pqm.util.Constants.*;


@Slf4j
@Component

public class MDMSValidator {

  private Map<String, Object> mdmsResMap;
  @Autowired
  private MDMSUtils mdmsUtils;

  private void validateIfMasterPresent(TestRequest testRequest, String tenantId,
      String schemaCode, String uniqueId) {
    Object mdmsData = mdmsUtils.mdmsCallV2(testRequest.getRequestInfo(), tenantId, schemaCode);
    List<Object> result = JsonPath.read(mdmsData,
        "$.mdms[?(@.uniqueIdentifier == '" + uniqueId + "')]");
    if (result.isEmpty()) {
      throw new CustomException(
          "INVALID" + " " + schemaCode.substring(schemaCode.lastIndexOf('.') + 1).toUpperCase(),
          schemaCode.substring(schemaCode.lastIndexOf('.') + 1).toLowerCase()
              + " " + "code is not present in mdms");
    }
  }

  public void validateMdmsData(TestRequest testRequest) {
    RequestInfo requestInfo = testRequest.getRequestInfo();
    String tenantId = testRequest.getTests().get(0).getTenantId();
    validateIfMasterPresent(testRequest, tenantId, PQM_SCHEMA_CODE_PLANT,
        testRequest.getTests().get(0).getPlantCode());
    validateIfMasterPresent(testRequest, tenantId, PQM_SCHEMA_CODE_PROCESS,
        testRequest.getTests().get(0).getProcessCode());
    validateIfMasterPresent(testRequest, tenantId, PQM_SCHEMA_CODE_STAGE,
        testRequest.getTests().get(0).getStageCode());
    validateIfMasterPresent(testRequest, tenantId, PQM_SCHEMA_CODE_MATERIAL,
        testRequest.getTests().get(0).getMaterialCode());
    List<QualityCriteria> criteriaList = testRequest.getTests().get(0).getQualityCriteria();
    for (QualityCriteria criteria : criteriaList) {
      validateIfMasterPresent(testRequest, tenantId, PQM_SCHEMA_CODE_CRITERIA,
          criteria.getCriteriaCode());
    }
  }
}
