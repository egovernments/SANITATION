package org.egov.tracking.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import java.util.List;
import java.util.Optional;
import javax.annotation.Generated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracking.service.TripAlertService;
import org.egov.tracking.service.TripService;
import org.egov.tracking.util.Constants;
import org.egov.tracking.util.JsonUtil;
import org.egov.tracking.util.TrackingApiUtil;
import org.openapitools.api.TripApi;
import org.openapitools.model.ACK;
import org.openapitools.model.AlertInfoResponse;
import org.openapitools.model.Trip;
import org.openapitools.model.TripProgress;
import org.openapitools.model.TripProgressResponse;
import org.openapitools.model.TripSearchFsmRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-07-29T19:38:13.286370500+05:30[Asia/Calcutta]")
@Controller
@RequestMapping("${openapi.trackingService.base-path:/api/v3}")
@CrossOrigin(maxAge = 3600)
@Slf4j
public class TripController implements TripApi {

    @Autowired
    TripService tripService;

    @Autowired
    TripAlertService tripAlertService;

    private final NativeWebRequest request;

    @Autowired
    public TripController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<ACK> createTrip(
            @Parameter(name = "Trip", description = "Create a new Trip in the system", required = true) @Valid @RequestBody Trip trip
    ) {
         log.info("## createTrip is invoked");
        String tripId = tripService.createdTrip(trip);

        //Initialise response object
        ACK ack = new ACK();

        if (tripId != null) {
            ack.setId(tripId);
            ack.setResponseCode("CODE-001");
            ack.setResponseMessage(Constants.MSG_RESPONSE_SUCCESS_SAVE);
            TrackingApiUtil.setResponse(request, JsonUtil.getJsonFromObject(ack));
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            ack.setResponseCode("CODE-002");
            ack.setResponseMessage(Constants.MSG_RESPONSE_ERROR_SAVE);
            TrackingApiUtil.setResponse(request, JsonUtil.getJsonFromObject(ack));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ACK> progressTrip(
            @Parameter(name = "TripProgress", description = "Update an existent trip in the system", required = true) @Valid @RequestBody TripProgress tripProgress,
            @Parameter(name = "X-authToken", description = "", in = ParameterIn.HEADER) @RequestHeader(value = "X-authToken", required = false) String xAuthToken
    ) {
         log.info("## progressTrip is invoked");
        String tripProgressId = tripService.createTripProgress(tripProgress, xAuthToken);

        //Initialise response object
        ACK ack = new ACK();

        if (tripProgressId != null) {
            ack.setId(tripProgressId);
            ack.setResponseCode("CODE-001");
            ack.setResponseMessage(Constants.MSG_RESPONSE_SUCCESS_SAVE);
            TrackingApiUtil.setResponse(request, JsonUtil.getJsonFromObject(ack));
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            ack.setResponseCode("CODE-002");
            ack.setResponseMessage(Constants.MSG_RESPONSE_ERROR_SAVE);
            TrackingApiUtil.setResponse(request, JsonUtil.getJsonFromObject(ack));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Fetch list of progress updates (geo locations as the vehicle / asset is moving. Helps in building a route of the path taken
    @Override
    public ResponseEntity<TripProgressResponse> getTripProgressById(
            @NotNull @Parameter(name = "tripId", description = "Trip id of trip progress to search", required = true, in = ParameterIn.QUERY) @Valid @RequestParam(value = "tripId", required = true) String tripId,
            @Parameter(name = "progressId", description = "ID of trip progress to search", in = ParameterIn.QUERY) @Valid @RequestParam(value = "progressId", required = false) String progressId
    ) {
         log.info("## getTripProgressById is invoked");
        List<TripProgressResponse> tripProgressResponseList = tripService.getTripProgressById(progressId, tripId);
        TrackingApiUtil.setResponse(request, JsonUtil.getJsonFromObject(tripProgressResponseList));
        return new ResponseEntity<>(HttpStatus.OK);
    }


    public ResponseEntity<ACK> updateTripProgress(
            @Parameter(name = "TripProgress", description = "Update an existent trip progress in the system", required = true) @Valid @RequestBody TripProgress tripProgress
    ) {
         log.info("## updateTripProgress is invoked");

        String tripProgressId = tripService.updateTripProgress(tripProgress.getId(), tripProgress.getUserId(), tripProgress.getMatchedPoiId());
        ACK ack = new ACK();

        if (tripProgressId != null) {
            ack.setId(tripProgressId);
            ack.setResponseCode("CODE-001");
            ack.setResponseMessage(Constants.MSG_RESPONSE_SUCCESS_SAVE);
            TrackingApiUtil.setResponse(request, JsonUtil.getJsonFromObject(ack));
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            ack.setResponseCode("CODE-002");
            ack.setResponseMessage(Constants.MSG_RESPONSE_ERROR_SAVE);
            TrackingApiUtil.setResponse(request, JsonUtil.getJsonFromObject(ack));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ACK> updateTrip(
            @Parameter(name = "Trip", description = "Update an existent trip in the system", required = true) @Valid @RequestBody Trip trip,
            @Parameter(name = "X-authToken", description = "", in = ParameterIn.HEADER) @RequestHeader(value = "X-authToken", required = false) String xAuthToken
    ) {
         log.info("## updateTrip is invoked");

        String tripId = tripService.updateTrip(trip, xAuthToken);
        ACK ack = new ACK();

        if (tripId != null) {
            ack.setId(tripId);
            ack.setResponseCode("CODE-001");
            ack.setResponseMessage(Constants.MSG_RESPONSE_SUCCESS_SAVE);
            TrackingApiUtil.setResponse(request, JsonUtil.getJsonFromObject(ack));
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            ack.setResponseCode("CODE-002");
            ack.setResponseMessage(Constants.MSG_RESPONSE_ERROR_SAVE);
            TrackingApiUtil.setResponse(request, JsonUtil.getJsonFromObject(ack));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<Trip>> findTrip(
            @Parameter(name = "X-authToken", description = "", in = ParameterIn.HEADER) @RequestHeader(value = "X-authToken", required = false) String xAuthToken,
            @Parameter(name = "status", description = "Status values that need to be considered for filter", in = ParameterIn.QUERY) @Valid @RequestParam(value = "status", required = false) String status,
            @Parameter(name = "name", description = "Trip name that needs to be considered for filter", in = ParameterIn.QUERY) @Valid @RequestParam(value = "name", required = false) String name,
            @Parameter(name = "userId", description = "user id who created the trip", in = ParameterIn.QUERY) @Valid @RequestParam(value = "userId", required = false) String userId,
            @Parameter(name = "operatorId", description = "Operator id to whom the trip is assigned", in = ParameterIn.QUERY) @Valid @RequestParam(value = "operatorId", required = false) String operatorId,
            @Parameter(name = "tenantId", description = "Tenant id", in = ParameterIn.QUERY) @Valid @RequestParam(value = "tenantId", required = false) String tenantId,
            @Parameter(name = "referenceNos", description = "", in = ParameterIn.QUERY) @Valid @RequestParam(value = "referenceNos", required = false) String referenceNos
    ) {
         log.info("## findTrip is invoked");
        //List<Trip> trips = tripService.getTripsBySearch(operatorId, name, status, userId);
        List<Trip> trips = tripService.getFsmTripsForDriver(operatorId, xAuthToken, tenantId);
        TrackingApiUtil.setResponse(request, JsonUtil.getJsonFromObject(trips));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Trip> getTripById(
            @Parameter(name = "tripId", description = "ID of Trip to return", required = true, in = ParameterIn.PATH) @PathVariable("tripId") String tripId
    ) {
         log.info("## getTripById is invoked");
        Trip trip = tripService.getTripById(tripId);
        TrackingApiUtil.setResponse(request, JsonUtil.getJsonFromObject(trip));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<AlertInfoResponse>> getTripAlerts(
            @NotNull @Parameter(name = "tenantId", description = "", required = true, in = ParameterIn.QUERY) @Valid @RequestParam(value = "tenantId", required = true) String tenantId,
            @Parameter(name = "applicationNos", description = "", in = ParameterIn.QUERY) @Valid @RequestParam(value = "applicationNos", required = false) String applicationNos,
            @Parameter(name = "tripIds", description = "", in = ParameterIn.QUERY) @Valid @RequestParam(value = "tripIds", required = false) String tripIds,
            @Parameter(name = "startDate", description = "Alert filter start date (YYYY-MM-DD)", in = ParameterIn.QUERY) @Valid @RequestParam(value = "startDate", required = false) String startDate,
            @Parameter(name = "endDate", description = "Alert filter end date (YYYY-MM-DD)", in = ParameterIn.QUERY) @Valid @RequestParam(value = "endDate", required = false) String endDate
    ) {
        List<AlertInfoResponse> alertInfoResponses = tripAlertService.getAlerts(tenantId, applicationNos, tripIds, startDate, endDate);
        TrackingApiUtil.setResponse(request, JsonUtil.getJsonFromObject(alertInfoResponses));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //Process requests from FSM and respond with Trip data from local database
    @Override
    public ResponseEntity<List<Trip>> tripSearchFsm(
            @NotNull @Parameter(name = "tenantId", description = "Tenant id", required = true, in = ParameterIn.QUERY) @Valid @RequestParam(value = "tenantId", required = true) String tenantId,
            @Parameter(name = "status", description = "Status values that need to be considered for filter", in = ParameterIn.QUERY) @Valid @RequestParam(value = "status", required = false) String status,
            @Parameter(name = "userId", description = "user id who created the trip", in = ParameterIn.QUERY) @Valid @RequestParam(value = "userId", required = false) String userId,
            @Parameter(name = "operatorId", description = "Operator id to whom the trip is assigned", in = ParameterIn.QUERY) @Valid @RequestParam(value = "operatorId", required = false) String operatorId,
            @Parameter(name = "referenceNos", description = "", in = ParameterIn.QUERY) @Valid @RequestParam(value = "referenceNos", required = false) String referenceNos,
            @Parameter(name = "businessService", description = "", in = ParameterIn.QUERY) @Valid @RequestParam(value = "businessService", required = false) String businessService,
            @Parameter(name = "TripSearchFsmRequest", description = "") @Valid @RequestBody(required = false) TripSearchFsmRequest tripSearchFsmRequest
    ) {
         log.info("## tripSearchFsm is invoked");
        List<Trip> trips = tripService.getTripsByLocalSearch(status, userId, operatorId, tenantId, businessService, referenceNos);
        TrackingApiUtil.setResponse(request, JsonUtil.getJsonFromObject(trips));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
