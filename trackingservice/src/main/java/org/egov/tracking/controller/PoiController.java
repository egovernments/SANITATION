package org.egov.tracking.controller;

import java.util.List;
import java.util.Optional;

import org.egov.tracking.service.POIService;
import org.egov.tracking.util.Constants;
import org.egov.tracking.util.JsonUtil;
import org.egov.tracking.util.TrackingApiUtil;
import org.openapitools.api.PoiApi;
import org.openapitools.model.ACK;
import org.openapitools.model.Location;
import org.openapitools.model.POI;
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

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-07-29T19:38:13.286370500+05:30[Asia/Calcutta]")
@Controller
@RequestMapping("${openapi.trackingService.base-path:/api/v3}")
@CrossOrigin(maxAge = 3600)
@Slf4j
public class PoiController implements PoiApi {

    @Autowired
    POIService poiService;

    private final NativeWebRequest request;

    @Autowired
    public PoiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<List<POI>> findPOI(
            @NotNull @Parameter(name = "tenantId", description = "", required = true, in = ParameterIn.QUERY) @Valid @RequestParam(value = "tenantId", required = true) String tenantId,
            @Parameter(name = "locationName", description = "Location name that needs to be considered for filter", in = ParameterIn.QUERY) @Valid @RequestParam(value = "locationName", required = false) String locationName,
            @Parameter(name = "alert", description = "Alert value set for the location. For example - Stoppage", in = ParameterIn.QUERY) @Valid @RequestParam(value = "alert", required = false) String alert
    ) {
         log.info("## findPOI is invoked");

        List<POI> pois = poiService.getPOIsBySearch(locationName, alert, tenantId);
        TrackingApiUtil.setResponse(request, JsonUtil.getJsonFromObject(pois));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<POI> getPoiById(
            @Parameter(name = "poiId", description = "ID of POI to return", required = true, in = ParameterIn.PATH) @PathVariable("poiId") String poiId
    ) {
         log.info("## getPOIbyId is invoked");
        List<POI> pois = poiService.getPOIsById(poiId);
        TrackingApiUtil.setResponse(request, JsonUtil.getJsonFromObject(pois));
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Override
    public ResponseEntity<ACK> createPOI(
            @Parameter(name = "POI", description = "Create a new POI in the system", required = true) @Valid @RequestBody POI POI
    ) {
         log.info("## createPOI is invoked");

        String poiId = poiService.createPOI(POI);
        //TrackingApiUtil.setResponse(request, JsonUtil.getJsonFromObject(alerts));
        //Initialise response object
        ACK ack = new ACK();

        if (poiId != null) {
            ack.setId(poiId);
            ack.setResponseCode("CODE-001");
            ack.setResponseMessage(Constants.MSG_RESPONSE_SUCCESS_SAVE);
            TrackingApiUtil.setResponse(request, JsonUtil.getJsonFromObject(ack));
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            ack.setResponseCode("CODE-002");
            ack.setResponseMessage(Constants.MSG_RESPONSE_ERROR_SAVE);
            TrackingApiUtil.setResponse(request, JsonUtil.getJsonFromObject(ack));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<POI>> searchNearby(
            @Parameter(name = "latitude", description = "Latitude of the user location", required = true, in = ParameterIn.PATH) @PathVariable("latitude") Float latitude,
            @Parameter(name = "longitude", description = "longitude of the user location", required = true, in = ParameterIn.PATH) @PathVariable("longitude") Float longitude,
            @Parameter(name = "distanceMeters", description = "Distance near the user to be searched", required = true, in = ParameterIn.PATH) @PathVariable("distanceMeters") Integer distanceMeters
    ) {
         log.info("## searchNearby is invoked");
        Location location = new Location();
        location.setLongitude(longitude);
        location.setLatitude(latitude);
        List<POI> pois = poiService.searchNearby(location, distanceMeters);
        TrackingApiUtil.setResponse(request, JsonUtil.getJsonFromObject(pois));
        return new ResponseEntity<>(HttpStatus.OK);
    }


    //Update POI location details
    @Override
    public ResponseEntity<Void> updatePOI(
            @Parameter(name = "POI", description = "Update an existent POI in the system", required = true) @Valid @RequestBody POI POI,
            @Parameter(name = "X-authToken", description = "", in = ParameterIn.HEADER) @RequestHeader(value = "X-authToken", required = false) String xAuthToken
    ) {
         log.info("## updatePOI is invoked in controller");
        ACK ack = new ACK();

        //Check if mandatory parameters are present in the request
        if(POI.getTenantId() == null || POI.getId() == null || POI.getType() == null || POI.getLocationDetails() == null) {
            ack.setResponseCode("CODE-003");
            ack.setResponseMessage(Constants.MSG_RESPONSE_MANDATORY_MISSING + " - Should include Poi Id, tenant id, type, location deails");
            TrackingApiUtil.setResponse(request, JsonUtil.getJsonFromObject(ack));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String poiId = poiService.updatePOI(POI);

        if (poiId != null) {
            ack.setId(poiId);
            ack.setResponseCode("CODE-001");
            ack.setResponseMessage(Constants.MSG_RESPONSE_SUCCESS_SAVE);
            TrackingApiUtil.setResponse(request, JsonUtil.getJsonFromObject(ack));
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            ack.setResponseCode("CODE-002");
            ack.setResponseMessage(Constants.MSG_RESPONSE_ERROR_SAVE);
            TrackingApiUtil.setResponse(request, JsonUtil.getJsonFromObject(ack));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Void> inactivatePOI(
            @Parameter(name = "POI", description = "", required = true) @Valid @RequestBody POI POI,
            @Parameter(name = "X-authToken", description = "", in = ParameterIn.HEADER) @RequestHeader(value = "X-authToken", required = false) String xAuthToken
    ) {
         log.info("## inactivatePOI is invoked in controller");

        ACK ack = new ACK();

        //Check if mandatory parameters are present in the request
        if(POI.getTenantId() == null || POI.getId() == null) {
            ack.setResponseCode("CODE-003");
            ack.setResponseMessage(Constants.MSG_RESPONSE_MANDATORY_MISSING + " - Should include Poi Id, tenant id");
            TrackingApiUtil.setResponse(request, JsonUtil.getJsonFromObject(ack));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String poiId = poiService.inactivatePOI(POI);

        if (poiId != null) {
            ack.setId(poiId);
            ack.setResponseCode("CODE-001");
            ack.setResponseMessage(Constants.MSG_RESPONSE_SUCCESS_SAVE);
            TrackingApiUtil.setResponse(request, JsonUtil.getJsonFromObject(ack));
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            ack.setResponseCode("CODE-002");
            ack.setResponseMessage(Constants.MSG_RESPONSE_ERROR_SAVE);
            TrackingApiUtil.setResponse(request, JsonUtil.getJsonFromObject(ack));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
