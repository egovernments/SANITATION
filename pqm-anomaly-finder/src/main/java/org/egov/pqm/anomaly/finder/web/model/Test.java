package org.egov.pqm.anomaly.finder.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Test {

  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("plantCode")
  private String plantCode = null;

  @JsonProperty("processCode")
  private String processCode = null;

  @JsonProperty("stageCode")
  private String stageCode = null;

  @JsonProperty("materialCode")
  private String materialCode = null;

  @JsonProperty("testCriteria")
  @Valid
  private List<QualityCriteria> testCriteria = new ArrayList<>();

  @JsonProperty("status")
  private TestResultStatus status = null;

  @JsonProperty("wfStatus")
  private String wfStatus = null;

  @JsonProperty("scheduledDate")
  private String scheduledDate = null;

  @JsonProperty("isActive")
  private Boolean isActive = null;

  @JsonProperty("documents")
  @Valid
  private List<Document> documents = null;

  @JsonProperty("additionalDetails")
  private Object additionalDetails = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;
}
