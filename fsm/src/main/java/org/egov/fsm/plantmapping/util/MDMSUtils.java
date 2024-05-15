package org.egov.fsm.plantmapping.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.fsm.plantmapping.config.PlantMappingConfiguration;
import org.egov.fsm.plantmapping.web.model.mdms.MdmsCriteriaRequest;
import org.egov.fsm.repository.ServiceRequestRepository;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsResponse;
import org.egov.mdms.model.ModuleDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class MDMSUtils {

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private PlantMappingConfiguration config;

    @Autowired
    private ObjectMapper objectMapper;

    public static final String filterCode = "$.*.code";

    public Object fetchMdmsData(RequestInfo requestInfo, String tenantId, String moduleName,
                                List<String> masterNameList) {
        org.egov.mdms.model.MdmsCriteriaReq mdmsCriteriaReq = getMdmsRequest(requestInfo, tenantId, moduleName, masterNameList);
        Object response = new HashMap<>();
        MdmsResponse mdmsResponse = new MdmsResponse();
        try {
            response = serviceRequestRepository.fetchResult(getMdmsSearchUrl(), mdmsCriteriaReq);
        } catch (Exception e) {
            log.error("Exception occurred while fetching category lists from mdms: ", e);
        }

        return response;

    }

    private org.egov.mdms.model.MdmsCriteriaReq getMdmsRequest(RequestInfo requestInfo, String tenantId,
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

        org.egov.mdms.model.MdmsCriteria mdmsCriteria = new org.egov.mdms.model.MdmsCriteria();
        mdmsCriteria.setTenantId(tenantId.split("\\.")[0]);
        mdmsCriteria.setModuleDetails(moduleDetailList);

        org.egov.mdms.model.MdmsCriteriaReq mdmsCriteriaReq = new org.egov.mdms.model.MdmsCriteriaReq();
        mdmsCriteriaReq.setMdmsCriteria(mdmsCriteria);
        mdmsCriteriaReq.setRequestInfo(requestInfo);

        return mdmsCriteriaReq;
    }

    /**
     * Returns the url for mdms search v1 endpoint
     *
     * @return url for mdms search v1 endpoint
     */
    public StringBuilder getMdmsSearchUrl() {
        return new StringBuilder().append(config.getMdmsHost()).append(config.getMdmsEndPoint());
    }

    /**
     * Returns the url for mdms search v2 endpoint
     *
     * @return url for mdms search v2 endpoint
     */
    public StringBuilder getMdmsSearchUrl2() {
        return new StringBuilder().append(config.getMdmsHostv2()).append(config.getMdmsv2EndPoint());
    }

    public Object mdmsCallV2(RequestInfo requestInfo, String tenantId, String schemaCode, List<String> uniqueIdentifiers){
        MdmsCriteriaRequest mdmsCriteriaRequest = getMDMSRequestV2(requestInfo, tenantId, schemaCode, uniqueIdentifiers);
        StringBuilder uri = getMdmsSearchUrl2();
        Object result = serviceRequestRepository.fetchResult(uri, mdmsCriteriaRequest);
        return result;
    }

    public MdmsCriteriaRequest getMDMSRequestV2(RequestInfo requestInfo , String  tenantId ,
                                                String schemaCode, List<String> uniqueIdentifiers){
        org.egov.fsm.plantmapping.web.model.mdms.MdmsCriteria mdmsCriteria = org.egov.fsm.plantmapping.web.model.mdms.MdmsCriteria.builder().tenantId(tenantId).schemaCode(schemaCode).limit(config.getMdmsv2MaxLimit()).isActive(true).build();

        if(!uniqueIdentifiers.isEmpty())
            mdmsCriteria.setUniqueIdentifiers(uniqueIdentifiers);

        MdmsCriteriaRequest mdmsCriteriaRequest =
                MdmsCriteriaRequest.builder().mdmsCriteria(mdmsCriteria).requestInfo(requestInfo).build();
        return mdmsCriteriaRequest;
    }


}