package org.egov.tracking.controller;

import java.util.List;
import java.util.Optional;

import org.egov.tracking.service.RouteService;
import org.egov.tracking.util.Constants;
import org.egov.tracking.util.JsonUtil;
import org.egov.tracking.util.TrackingApiUtil;
import org.openapitools.api.RouteApi;
import org.openapitools.model.ACK;
import org.openapitools.model.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-07-29T19:38:13.286370500+05:30[Asia/Calcutta]")
@Controller
@RequestMapping("${openapi.trackingService.base-path:/api/v3}")
@CrossOrigin(maxAge = 3600)
@Slf4j
public class RouteController implements RouteApi {

    private final NativeWebRequest request;

    @Autowired
    public RouteController(NativeWebRequest request) {
        this.request = request;
    }

    @Autowired
    RouteService routeService;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<ACK> createRoute(
            @Parameter(name = "Route", description = "Create a new Route in the system", required = true) @Valid @RequestBody Route route
    ) {
         log.info("## createRoute is invoked");

        String routeId = routeService.createRoute(route);
        //Initialise response object
        ACK ack = new ACK();

        if (routeId != null) {
            ack.setId(routeId);
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
    public ResponseEntity<ACK> updateRoute(
            @Parameter(name = "Route", description = "Update an existent route in the system", required = true) @Valid @RequestBody Route route
    ) {
         log.info("## updateRoute is invoked");
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @Override
    public ResponseEntity<List<Route>> findRoute(
            @Parameter(name = "userId", description = "User id that created the route to be considered for filter", in = ParameterIn.QUERY) @Valid @RequestParam(value = "userId", required = false) String userId,
            @Parameter(name = "routeName", description = "route name that needs to be considered for filter", in = ParameterIn.QUERY) @Valid @RequestParam(value = "routeName", required = false) String routeName,
            @Parameter(name = "pageSize", description = "", in = ParameterIn.QUERY) @Valid @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @Parameter(name = "pageNumber", description = "", in = ParameterIn.QUERY) @Valid @RequestParam(value = "pageNumber", required = false) Integer pageNumber
    ) {
         log.info("## findRoute is invoked for : userId - " + userId + ", route name - " + routeName);
        List<Route> routes = routeService.getRoutesBySearch(routeName, userId);
        TrackingApiUtil.setResponse(request, JsonUtil.getJsonFromObject(routes));
        return new ResponseEntity<>(HttpStatus.OK);

    }
    @Override
    public ResponseEntity<Route> getRouteById(
            @Parameter(name = "routeId", description = "ID of Route to return", required = true, in = ParameterIn.PATH) @PathVariable("routeId") String routeId
    ) {
         log.info("## getRouteById is invoked");
        Route route = routeService.getRoutesById(routeId);
        TrackingApiUtil.setResponse(request, JsonUtil.getJsonFromObject(route));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
