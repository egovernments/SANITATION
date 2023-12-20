package org.egov.vendor.driver.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.egov.vendor.web.model.AuditDetails;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.validation.annotation.Validated;


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

  @SafeHtml
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  @SafeHtml
  @Size(max = 64)
  private String tenantId = null;

  @JsonProperty("vendorId")
  @SafeHtml
  @Size(max = 64)
  private String vendorId = null;

  @JsonProperty("individualId")
  @SafeHtml
  @Size(max = 64)
  private String individualId = null;

  @JsonProperty("additionalDetails")
  private Object additionalDetails = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  @JsonProperty("vendorWorkerStatus")
  private WorkerStatus vendorWorkerStatus = null;

}
