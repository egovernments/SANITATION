package org.egov.vehicle.trip.web.model;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.common.contract.request.User;
import org.egov.vehicle.web.model.AuditDetails;
import org.egov.vehicle.web.model.Vehicle;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import jakarta.annotation.Generated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request schema of VehicleTrip.  
 */
@Validated
@Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-12-23T12:08:13.326Z[GMT]")

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VehicleTrip   {

  @JsonProperty("id")
  private String id = null;

  @NotNull
  @NotBlank
  @Size(max=64)
  @JsonProperty("tenantId")
  private String tenantId = null;
  
  @JsonProperty("tripOwner")
  @Valid
  private User tripOwner = null;
  
  @JsonProperty("tripOwnerId")
  private String tripOwnerId = null;
  
  @JsonProperty("driver")
  @Valid
  private User driver = null;
  
  @JsonProperty("driverId")
  @Size(max=64)
  private String driverId = null;
  

  @JsonProperty("vehicle")
  @Valid
  private Vehicle vehicle;
  
  @JsonProperty("vehicleId")
  private String vehicleId = null;

  @JsonProperty("applicationNo")
  private String applicationNo = null;
  
  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ACTIVE("ACTIVE"),
    
    INACTIVE("INACTIVE");

    private String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusEnum fromValue(String text) {
      for (StatusEnum b : StatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("status")
  private StatusEnum status = null;

  @NotNull
  @NotBlank
  @JsonProperty("businessService")
  private String businessService = null;

  @JsonProperty("applicationStatus")
  private String applicationStatus = null;

  @JsonProperty("endType")
  private String endType = null;


  @JsonProperty("additionalDetails")
  private Object additionalDetails = null;
  
  @NotNull
  @NotEmpty
  @Valid
  @JsonProperty("tripDetails")
  private List<VehicleTripDetail> tripDetails = null;


  @JsonProperty("tripStartTime")
  private Long tripStartTime = null;
  
  @JsonProperty("tripEndTime")
  private Long tripEndTime = null;
  
  @JsonProperty("volumeCarried")
  private Double volumeCarried = null;
  
  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;
  
 
}

