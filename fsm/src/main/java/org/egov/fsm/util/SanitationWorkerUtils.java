package org.egov.fsm.util;

import org.egov.fsm.web.model.garima.SanitationWorker;
import org.egov.fsm.web.model.garima.SanitationWorkerSearch;
import org.springframework.stereotype.Component;

@Component
public class SanitationWorkerUtils {

    public SanitationWorker createSanitationWorker(SanitationWorkerSearch sanitationWorkerSearch) {
    	SanitationWorker sanitationWorker = new SanitationWorker();
        sanitationWorker.setProvisional(sanitationWorkerSearch.isProvisional());
        sanitationWorker.setGarima_id(sanitationWorkerSearch.getGarima_id());
        sanitationWorker.setName(sanitationWorkerSearch.getName());
        sanitationWorker.setDob(sanitationWorkerSearch.getDob());
        sanitationWorker.setGender(sanitationWorkerSearch.getGender());
        sanitationWorker.setCity_id(sanitationWorkerSearch.getCity_id());
        sanitationWorker.setDistrict_id(sanitationWorkerSearch.getDistrict_id());
        sanitationWorker.setMobile_number(sanitationWorkerSearch.getMobile_number());
//        sanitationWorker.setAadhar_digits(sanitationWorkerSearch.getAadhar_digits());
		return sanitationWorker;
        
    }
}
