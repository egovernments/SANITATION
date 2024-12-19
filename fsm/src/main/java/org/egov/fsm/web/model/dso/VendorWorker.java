package org.egov.fsm.web.model.dso;

import javax.validation.constraints.Size;

import org.egov.fsm.web.model.AuditDetails;
import org.egov.fsm.web.model.worker.WorkerStatus;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Capture the Vendor Tagged Worker information in the system.
 */
@Validated
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class VendorWorker {

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

