package org.egov.vendor.driver.web.model;

import javax.validation.constraints.Size;

import org.egov.vendor.web.model.AuditDetails;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Capture the Worker information in the system.
 */
@Validated
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Worker {

  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  @Size(max = 64)
  private String tenantId = null;

  @JsonProperty("vendorId")
  @Size(max = 64)
  private String vendorId = null;

  @JsonProperty("individualId")
  @Size(max = 64)
  private String individualId = null;

  @JsonProperty("additionalDetails")
  private Object additionalDetails = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  @JsonProperty("vendorWorkerStatus")
  private WorkerStatus vendorWorkerStatus = null;

}
