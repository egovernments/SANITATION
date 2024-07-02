/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (7.0.0-beta).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package org.openapitools.api;

import org.openapitools.model.LocationAlert;
import org.openapitools.model.ServiceType;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-11-18T09:48:22.341527800+05:30[Asia/Calcutta]")
@Validated
@Tag(name = "Config", description = "Tracking service requires certain configuration information. This includes the list of services supported and types of anomalies for which notifications should be sent out. Configuration information is attached to a trip.")
public interface ConfigApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * GET /config/_alerts : Get the list of alerts. Alerts are tagged to POIs
     * Fetch the list of alerts
     *
     * @return successful operation (status code 200)
     */
    @Operation(
        operationId = "findAlerts",
        summary = "Get the list of alerts. Alerts are tagged to POIs",
        description = "Fetch the list of alerts",
        tags = { "Config" },
        responses = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = {
                @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LocationAlert.class)))
            })
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/config/_alerts",
        produces = { "application/json" }
    )
    default ResponseEntity<List<LocationAlert>> findAlerts(
        
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "[ { \"code\" : \"code\", \"title\" : \"title\" }, { \"code\" : \"code\", \"title\" : \"title\" } ]";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /config/_services : Get the list of services provided by ULBs. Service id is passed during trip creation.
     * Fetch the list of services offered
     *
     * @return successful operation (status code 200)
     */
    @Operation(
        operationId = "findServices",
        summary = "Get the list of services provided by ULBs. Service id is passed during trip creation.",
        description = "Fetch the list of services offered",
        tags = { "Config" },
        responses = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = {
                @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ServiceType.class)))
            })
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/config/_services",
        produces = { "application/json" }
    )
    default ResponseEntity<List<ServiceType>> findServices(
        
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "[ { \"code\" : \"code\", \"name\" : \"name\", \"tenantId\" : \"tenantId\" }, { \"code\" : \"code\", \"name\" : \"name\", \"tenantId\" : \"tenantId\" } ]";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}