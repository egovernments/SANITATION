package org.egov.pqm.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.MdmsResponse;
import org.egov.mdms.model.ModuleDetail;
import org.egov.pqm.config.ServiceConfiguration;
import org.egov.pqm.repository.ServiceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@Slf4j
public class MDMSUtils {

  @Autowired
  private ServiceRequestRepository serviceRequestRepository;

  @Autowired
  private ServiceConfiguration config;

  @Autowired
  private ObjectMapper objectMapper;

  public static final String filterCode = "$.*.code";

  public Map<String, Map<String, JSONArray>> fetchMdmsData(RequestInfo requestInfo, String tenantId, String moduleName,
      List<String> masterNameList) {
    MdmsCriteriaReq mdmsCriteriaReq = getMdmsRequest(requestInfo, tenantId, moduleName, masterNameList);
    Object response = new HashMap<>();
    Integer rate = 0;
    MdmsResponse mdmsResponse = new MdmsResponse();
    try {
      Object result = serviceRequestRepository.fetchResult(getMdmsSearchUrl(), mdmsCriteriaReq);
      mdmsResponse = objectMapper.convertValue(result, MdmsResponse.class);
    } catch (Exception e) {
      log.error("Exception occurred while fetching category lists from mdms: ", e);
    }

    return mdmsResponse.getMdmsRes();

  }

  private MdmsCriteriaReq getMdmsRequest(RequestInfo requestInfo, String tenantId,
      String moduleName, List<String> masterNameList) {
    List<MasterDetail> masterDetailList = new ArrayList<>();
    for (String masterName : masterNameList) {
      MasterDetail masterDetail = new MasterDetail();
      masterDetail.setName(masterName);
      masterDetailList.add(masterDetail);
    }

    ModuleDetail moduleDetail = new ModuleDetail();
    moduleDetail.setMasterDetails(masterDetailList);
    moduleDetail.setModuleName(moduleName);
    List<ModuleDetail> moduleDetailList = new ArrayList<>();
    moduleDetailList.add(moduleDetail);

    MdmsCriteria mdmsCriteria = new MdmsCriteria();
    mdmsCriteria.setTenantId(tenantId.split("\\.")[0]);
    mdmsCriteria.setModuleDetails(moduleDetailList);

    MdmsCriteriaReq mdmsCriteriaReq = new MdmsCriteriaReq();
    mdmsCriteriaReq.setMdmsCriteria(mdmsCriteria);
    mdmsCriteriaReq.setRequestInfo(requestInfo);

    return mdmsCriteriaReq;
  }

  public StringBuilder getMdmsSearchUrl() {
    return new StringBuilder().append(config.getMdmsHost()).append(config.getMdmsEndPoint());
  }

  private ModuleDetail getTenantModuleRequestData() {
    List<MasterDetail> tenantMasterDetails = new ArrayList<>();

//    MasterDetail tenantMasterDetail = MasterDetail.builder().name(MASTER_TENANTS)
//        .filter(filterCode).build();
//
//    tenantMasterDetails.add(tenantMasterDetail);
//
//    ModuleDetail tenantModuleDetail = ModuleDetail.builder().masterDetails(tenantMasterDetails)
//        .moduleName(MDMS_TENANT_MODULE_NAME).build();

    ModuleDetail tenantModuleDetail =null;
    return tenantModuleDetail;
  }

}
