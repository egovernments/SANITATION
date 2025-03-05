package org.egov.fsm.web.model.worker;

import javax.validation.constraints.Size;

import org.egov.fsm.web.model.AuditDetails;
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

  @JsonProperty("applicationId")
  @Size(max = 64)
  private String applicationId = null;

  @JsonProperty("individualId")
  @Size(max = 64)
  private String individualId = null;

  @JsonProperty("workerType")
  private WorkerType workerType = null;

  @JsonProperty("status")
  private WorkerStatus status = null;

  @JsonProperty("additionalDetails")
  private Object additionalDetails = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;
}
