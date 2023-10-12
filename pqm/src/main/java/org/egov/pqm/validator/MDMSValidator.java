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

  private void validateIfPlantMasterPresent(TestRequest testRequest, String tenantId,
      String plant_schemaCode, String plant_uniqueId) {
    RequestInfo requestInfo = testRequest.getRequestInfo();
    Object mdmsData = mdmsUtils.mdmsCallV2(requestInfo, tenantId, plant_schemaCode);
    List<Object> result = JsonPath.read(mdmsData,
        "$.mdms[?(@.uniqueIdentifier == '" + plant_uniqueId + "')]");
    if (result.isEmpty()) {
      throw new CustomException(ErrorConstants.INVALID_PLANT_MASTER,
          "plant code is not present in mdms");
    }

  }

  private void validateIfProcessMasterPresent(TestRequest testRequest, String tenantId,
      String process_schemaCode, String process_uniqueId) {
    RequestInfo requestInfo = testRequest.getRequestInfo();
    Object mdmsData = mdmsUtils.mdmsCallV2(requestInfo, tenantId, process_schemaCode);
    List<Object> result = JsonPath.read(mdmsData,
        "$.mdms[?(@.uniqueIdentifier == '" + process_uniqueId + "')]");
    if (result.isEmpty()) {
      throw new CustomException(ErrorConstants.INVALID_PROCESS_MASTER,
          "process code is not present in mdms");
    }
  }

  private void validateIfStageMasterPresent(TestRequest testRequest, String tenantId,
      String stage_schemaCode, String stage_uniqueId) {
    RequestInfo requestInfo = testRequest.getRequestInfo();
    Object mdmsData = mdmsUtils.mdmsCallV2(requestInfo, tenantId, stage_schemaCode);
    List<Object> result = JsonPath.read(mdmsData,
        "$.mdms[?(@.uniqueIdentifier == '" + stage_uniqueId + "')]");
    if (result.isEmpty()) {
      throw new CustomException(ErrorConstants.INVALID_STAGE_MASTER,
          "stage code is not present in mdms");
    }
  }

  private void validateIfMaterialMasterPresent(TestRequest testRequest, String tenantId,
      String material_schemaCode, String material_uniqueId) {
    RequestInfo requestInfo = testRequest.getRequestInfo();
    Object mdmsData = mdmsUtils.mdmsCallV2(requestInfo, tenantId, material_schemaCode);
    List<Object> result = JsonPath.read(mdmsData,
        "$.mdms[?(@.uniqueIdentifier == '" + material_uniqueId + "')]");
    if (result.isEmpty()) {
      throw new CustomException(ErrorConstants.INVALID_MATERIAL_MASTER,
          "material code is not present in mdms");
    }
  }

  private void validateIfQualityCriteriaMasterPresent(TestRequest testRequest, String tenantId,
      String criteria_schemaCode, List<QualityCriteria> criteriaList) {
    RequestInfo requestInfo = testRequest.getRequestInfo();
    Object mdmsData = mdmsUtils.mdmsCallV2(requestInfo, tenantId, criteria_schemaCode);
    for (QualityCriteria criteria : criteriaList) {
      String criteria_uniqueId = criteria.getCriteriaCode();
      List<Object> result = JsonPath.read(mdmsData,
          "$.mdms[?(@.uniqueIdentifier == '" + criteria_uniqueId + "')]");
      if (result.isEmpty()) {
        throw new CustomException(ErrorConstants.INVALID_QUALITY_CRITERIA_MASTER,
            " qaulity criteria code not present in mdms");
      }
    }


  }


  public void validateMdmsData(TestRequest testRequest) {
    RequestInfo requestInfo = testRequest.getRequestInfo();
    String tenantId = testRequest.getTests().get(0).getTenantId();
    validateIfPlantMasterPresent(testRequest, tenantId, PQM_SCHEMA_CODE_PLANT,
        testRequest.getTests().get(0).getPlantCode());
    validateIfProcessMasterPresent(testRequest, tenantId, PQM_SCHEMA_CODE_PROCESS,
        testRequest.getTests().get(0).getProcessCode());
    validateIfStageMasterPresent(testRequest, tenantId, PQM_SCHEMA_CODE_STAGE,
        testRequest.getTests().get(0).getStageCode());
    validateIfMaterialMasterPresent(testRequest, tenantId, PQM_SCHEMA_CODE_MATERIAL,
        testRequest.getTests().get(0).getMaterialCode());
    validateIfQualityCriteriaMasterPresent(testRequest, tenantId, PQM_SCHEMA_CODE_CRITERIA,
        testRequest.getTests().get(0).getQualityCriteria());
  }


}