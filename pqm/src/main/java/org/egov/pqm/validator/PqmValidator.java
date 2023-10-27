package org.egov.pqm.validator;

import org.egov.pqm.util.ErrorConstants;
import org.egov.pqm.web.model.QualityCriteria;
import org.egov.pqm.web.model.TestRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;

import static org.egov.pqm.util.Constants.QUALITY_CRITERIA_NOT_PRESENT;

@Component
public class PqmValidator {

    public void validateQualityCriteria(TestRequest testRequest){
        if(testRequest.getTests().get(0).getQualityCriteria().isEmpty()){
            throw new CustomException(QUALITY_CRITERIA_NOT_PRESENT,
                    " testCriteria is empty in test object");        }
    }
}
