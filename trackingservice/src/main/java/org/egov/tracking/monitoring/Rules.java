package org.egov.tracking.monitoring;

import static org.egov.tracking.util.Constants.MONITORING_USER_ID;
import static org.egov.tracking.util.Constants.POI_MATCH_THRESHOLD_METERS;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracking.data.dao.PoiDao;
import org.egov.tracking.data.dao.RouteDao;
import org.egov.tracking.data.dao.TripAlertDao;
import org.egov.tracking.data.dao.TripDao;
import org.egov.tracking.data.dao.TripProgressDao;
import org.egov.tracking.data.model.TripAlert;
import org.egov.tracking.data.sao.TripSao;
import org.egov.tracking.service.helper.TripServiceHelper;
import org.egov.tracking.util.Constants;
import org.openapitools.model.Location;
import org.openapitools.model.POI;
import org.openapitools.model.Route;
import org.openapitools.model.Trip;
import org.openapitools.model.TripProgressResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//Rules class hold the methods that are required to load event specific information from database.
//Rules class also has the actual methods required to execute various rules
@Service
@Slf4j
public class Rules {
    //RuleModel ruleModel = new RuleModel();

    @Autowired
    TripProgressDao tripProgressDao;
    @Autowired
    TripDao tripDao;
    @Autowired
    TripAlertDao tripAlertDao;

    @Autowired
    PoiDao poiDao;

    @Autowired
    RouteDao routeDao;

    @Autowired
    TripServiceHelper tripServiceHelper;
    @Autowired
    TripSao tripSao;

    //Stage 1 - Fetch data from relevant sources and populate the RuleMode. This model is then used by rest of rule action methods
    public RuleModel loadModel(String progressId, RuleModel ruleModel) {
         log.info("## loadModel method");

        //Step 1 - Fetch trip progress details based on progress id. Since a progress id is input, max 1 record is fetched
        List<TripProgressResponse> tripProgressResponseList = tripProgressDao.getTripProgress(progressId, null);

        if (tripProgressResponseList == null || tripProgressResponseList.size() != 1){
             log.info("## No match found for progress id in db : " + progressId);
            return null;
        }
        //Step 2 - Search nearby POIs for the location belonging to the progress id
        //Since this is a single ping from user, there is only one record in tripProgressList.
        //Since this is a spatial POINT, there is only one element in getProgressData.
        Location userLocation = tripProgressResponseList.get(0).getLocation();
        String tripId = tripProgressResponseList.get(0).getTripId();

        List<POI> matchingPoiList = new ArrayList<>();
        matchingPoiList = poiDao.searchNearby(userLocation, POI_MATCH_THRESHOLD_METERS);

        if (matchingPoiList == null || matchingPoiList.isEmpty()) {
             log.info("## No POI found near the user location");
            return null;
        }

        //Step 3 - Fetch corresponding trip data
        Trip trip = tripDao.fetchTripbyId(tripId);

        //Step 4 - Fetch route info since start poi and end poi are required
        Route route = routeDao.fetchRoutebyId(trip.getRouteId());

        //Step Final - Load rule model entity for further usage in rule execution
        //Fetch the first record in matched POI list since that is the closest one to user location
        ruleModel.setMatchedPoi(matchingPoiList.get(0).getId());
        ruleModel.setAlert(matchingPoiList.get(0).getAlert());
        ruleModel.setDistanceFromPoiMeters(matchingPoiList.get(0).getDistanceMeters());
        ruleModel.setProgressId(progressId);
        ruleModel.setTripId(tripId);
        ruleModel.setTenantId(trip.getTenantId());
        ruleModel.setRouteEndPoi(route.getEndPoi());

         log.info("## Matched POI, Distance from POI : " + ruleModel.getMatchedPoi() + " " + ruleModel.getDistanceFromPoiMeters());

        return ruleModel;
    }

    //Stage 2 - Implement rule action methods which have to be invoked

    //Rule - If a trip progress update (lat long of a moving asset) is matching an existing POI in the trip, update the progress entry with that POI
    public void ruleUpdateMatchedPoi(RuleModel ruleModel) {
         log.info("## ruleUpdatedMatchedPoi method");
        if (ruleModel.getMatchedPoi() != null){
            tripProgressDao.updateTripProgress(ruleModel.getProgressId(), MONITORING_USER_ID, ruleModel.getMatchedPoi());
        }
    }

    //Rule - If the moving asset is spotted at a POI with alert mapped, update trip progress and send notification
    public void ruleUpdateTripProgressAndNotifyAlert(RuleModel ruleModel) {
         log.info("## ruleUpdateTripProgressAndNotifyAlert method");
        if (ruleModel.getAlert() != null && !ruleModel.getAlert().isEmpty()){
            //Update trip alert record with alert string
            TripAlert tripAlert = new TripAlert();
            tripAlert.setTripProgressId(ruleModel.getProgressId());
            tripAlert.setAlert(ruleModel.getAlert());
            tripAlert.setTripId(ruleModel.getTripId());

            OffsetDateTime offsetDateTime = OffsetDateTime.now();
            String currentDateString = offsetDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
            tripAlert.setAlertDateTime(currentDateString);

            tripAlertDao.updateTripAlert(tripAlert, ruleModel.getTenantId());
        }
    }

    //Rule - If the moving asset is spotted at a POI which is the trip's end POI, update the trip status and send notification
    public void ruleUpdateTripAndNotifyTripEnd(RuleModel ruleModel) {
         log.info("## ruleUpdateTripAndNotifyTripEnd method");
        if (ruleModel.getMatchedPoi() != null && ruleModel.getMatchedPoi().equals(ruleModel.getRouteEndPoi())){
            //POI at the progress location is matching with the route end POI. Update the trip status
            Trip trip = new Trip();
            trip.setId(ruleModel.getTripId());
            trip.setStatus(Trip.StatusEnum.COMPLETED);
            trip.setTripEndType(Constants.TRIP_CLOSE_SYSTEM);
            trip.tenantId(ruleModel.getTenantId());
            //Update trip stats in FSM
            tripServiceHelper.updateFSMTripStatus(trip, ruleModel.getAuthToken(), tripSao);

            //Update trip status in VTS
            tripDao.updateTrip(trip);
        }
    }

}
