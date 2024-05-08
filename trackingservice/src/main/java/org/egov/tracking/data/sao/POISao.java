package org.egov.tracking.data.sao;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracking.data.dao.PoiDao;
import org.openapitools.model.POI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//TripSao is the service access object to manage trip data operations (search, update). This SAO interacts with DIGIT FSM APIs
@Service
@Slf4j
public class POISao {

    @Autowired
    PoiDao poiDao;
    //Destination location POI id of a trip is from the FS treatment plant for the tenant id
    public String getDesitationLocation(String tenantId, String authToken, String mdmsUrl) {
        log.info("## getDesitationLocation" );
        String poiIdDestination = "";
        //TODO - Since there destination to tenant mapping is evolving, we are using a fixed POI from database. This has to change to REST API call
        List<POI> poiList = poiDao.fetchPOIbyFilters("FSTP", null, tenantId);
        if (!poiList.isEmpty()) {
            //Fetch the first element in location since this ia POINT spatial with single part of LatLong
            poiIdDestination = poiList.get(0).getId();
        }
        return poiIdDestination;
    }
}
