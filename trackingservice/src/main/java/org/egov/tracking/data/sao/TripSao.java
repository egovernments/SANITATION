package org.egov.tracking.data.sao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracking.data.model.FsmApplication;
import org.egov.tracking.data.model.FsmVehicleTrip;
import org.egov.tracking.util.Constants;
import org.egov.tracking.util.JsonUtil;
import org.egov.tracking.util.SaoUtil;
import org.egov.tracking.util.exception.FsmInvalidTokenException;
import org.egov.tracking.util.exception.RestTemplateResponseErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

//TripSao is the service access object to manage trip data operations (search, update). This SAO interacts with DIGIT FSM APIs
@Service
@Slf4j
public class TripSao {
    RestTemplate restTemplate = new RestTemplate();
    RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
    @Autowired
    public TripSao() {
        this.restTemplate = this.restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
        this.restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

    }

    public List<FsmApplication> searchFsmApplicationsForDriver(String workerId, String tenantId, String authToken, String fsmUrl) {
         log.info("## searchFsmApplicationsForDriver is invoked");
        List<FsmApplication> fsmApplicationList = new ArrayList<>();

        HttpEntity<Map<String, Object>> entity = SaoUtil.getMapHttpEntity(authToken, null);
        StringBuilder searchUrl = new StringBuilder().append(fsmUrl).append("/").append("_search?tenantId=").append(tenantId);
        if(workerId != null) {
            searchUrl.append("&individualIds=").append(workerId);
        }
         log.info("## " + searchUrl);

        ResponseEntity<String> response = restTemplate.postForEntity(searchUrl.toString(), entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
             log.info(response.getBody());
            fsmApplicationList = JsonUtil.getFSMObjectFromJson(response.getBody());
        } else {
             log.error("## Request Failed");
             log.error(response.getStatusCode().toString());
             log.error(response.getBody());
            //In case the issue is due to invalid auth token, send a HTTP 401 so that client can retry with proper token
            if (Objects.requireNonNull(response.getBody()).contains("InvalidAccessTokenException")) {
                throw new FsmInvalidTokenException("InvalidAccessTokenException");
            }
        }
        return fsmApplicationList;
    }

    public String fetchFsmTrips(String referenceApplicationNo, String tripId, String tenantId, String authToken, String vehicleTripUrl) {
         log.info("## fetchFsmTripsForApplication is invoked");
        HttpEntity<Map<String, Object>> entity = SaoUtil.getMapHttpEntity(authToken, null);
        StringBuilder searchUrl = new StringBuilder().append(vehicleTripUrl).append("/").append("_search?tenantId=").append(tenantId).append("&applicationStatus=").append(
            Constants.FSM_TRIP_SEARCH_STATUS_FILTER);
        if (referenceApplicationNo != null) {
            searchUrl.append("&refernceNos=").append(referenceApplicationNo);
        }
        if (tripId != null) {
            searchUrl.append("&applicationNos=").append(tripId);
        }

         log.info("## " + searchUrl);

        ResponseEntity<String> response = restTemplate.postForEntity(searchUrl.toString(), entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
             log.info(response.getBody());
            return response.getBody();
        } else {
             log.error("## Request Failed");
             log.error(response.getStatusCode().toString());
             log.error(response.getBody());
            //In case the issue is due to invalid auth token, send a HTTP 401 so that client can retry with proper token
            if (Objects.requireNonNull(response.getBody()).contains("InvalidAccessTokenException")) {
                throw new FsmInvalidTokenException("InvalidAccessTokenException");
            }
            return null;
        }
    }

    //Update vehicle trip status in FSM
    public List<FsmVehicleTrip> updateFsmEndTripForApplication(Map<String, Object> vehicleTripMap, String authToken, String vehicleTripUrl) {
         log.info("## updateFsmEndTripForApplication ");
        List<FsmVehicleTrip> fsmVehicleTripList = new ArrayList<>();

        //Create http header and request body
        HttpEntity<Map<String, Object>> entity = SaoUtil.getMapHttpEntity(authToken, vehicleTripMap);

        StringBuilder updateUrl = new StringBuilder().append(vehicleTripUrl).append("/").append("_update?_=1698562201046");
         log.info("## " + updateUrl);

        ResponseEntity<String> response = restTemplate.postForEntity(updateUrl.toString(), entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
             log.info(response.getBody());
            fsmVehicleTripList = JsonUtil.getFSMVehicleTripObjectFromJson(response.getBody());
        } else {
             log.error("## Request Failed");
             log.error(response.getStatusCode().toString());
             log.error(response.getBody());
            //In case the issue is due to invalid auth token, send a HTTP 401 so that client can retry with proper token
            if (Objects.requireNonNull(response.getBody()).contains("InvalidAccessTokenException")) {
                throw new FsmInvalidTokenException("InvalidAccessTokenException");
            }
        }
        return fsmVehicleTripList;
    }
}
