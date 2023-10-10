package org.egov.pqm.validator;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.egov.pqm.util.Constants;
import org.egov.pqm.util.ErrorConstants;
import org.egov.pqm.web.model.TestRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.util.CollectionUtils;

import java.util.*;
@Slf4j

public class MDMSValidator {
    private Map<String, Object> mdmsResMap;

    public void validateMdmsData(Object mdmsData) {

        this.mdmsResMap = getAttributeValues(mdmsData);
        String[] masterArray = { Constants.MDMS_PLANT,
                Constants.MDMS_PROCESS, Constants.MDMS_STAGE, Constants.MDMS_MATERIAL};

        validateIfMasterPresent(masterArray, this.mdmsResMap);
    }

    private void validateIfMasterPresent(String[] masterNames, Map<String, Object> codes) {
        Map<String, String> errorMap = new HashMap<>();
        for (String masterName : masterNames) {
            if (codes.get(masterName) == null || CollectionUtils.isEmpty((Collection<?>) codes.get(masterName))) {
                errorMap.put("MDMS DATA ERROR ", "Unable to fetch " + masterName + " codes from MDMS");
            }
        }
        if (!errorMap.isEmpty())
            throw new CustomException(errorMap);
    }


    public Map<String, Object> getAttributeValues(Object mdmsData)
    {
        String modulepath = Constants.PQM_JSONPATH_CODE;
        final Map<String, Object> mdmsResMapResponse = new HashMap<>();
        try {
            mdmsResMapResponse.putAll(JsonPath.read(mdmsData, modulepath));
        } catch (Exception e) {
            log.error("Error while fetching MDMS data", e);
            throw new CustomException(ErrorConstants.INVALID_TENANT_ID_MDMS_KEY,
                    ErrorConstants.INVALID_TENANT_ID_MDMS_MSG);
        }
        return mdmsResMapResponse;
    }
}