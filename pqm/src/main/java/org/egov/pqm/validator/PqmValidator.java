package org.egov.pqm.validator;

import org.egov.pqm.util.ErrorConstants;
import org.egov.pqm.web.model.QualityCriteria;
import org.egov.pqm.web.model.TestRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;

import static org.egov.pqm.util.Constants.QualityCriteria_Not_Present;
@Component
public class PqmValidator {

    public void validateQualityCriteria(TestRequest testRequest){
        if(testRequest.getTests().get(0).getQualityCriteria().isEmpty()){
            throw new CustomException(QualityCriteria_Not_Present,
                    " QualityCriteria is empty");        }
    }

    public void validateTestType(TestRequest testRequest){
        if(testRequest.getTests().get(0).getTestType().toString() != "LAB"){

        }
    }
}
