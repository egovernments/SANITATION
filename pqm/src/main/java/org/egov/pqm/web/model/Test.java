package org.egov.pqm.web.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
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
  private String id;

  @JsonProperty("tenantId")
  private String tenantId;

  @JsonProperty("plantCode")
  private String plantCode;

  @JsonProperty("processCode")
  private String processCode;

  @JsonProperty("stageCode")
  private String stageCode;

  @JsonProperty("materialCode")
  private String materialCode;

  @JsonProperty("deviceCode")
  private String deviceCode;

  @JsonProperty("testCriteria")
  @Valid
  private List<QualityCriteria> qualityCriteria = new ArrayList<>();

  @JsonProperty("status")
  private TestResultStatus status;

  @JsonProperty("wfStatus")
  private String wfStatus;

  @JsonProperty("testType")
  private TestType testType;

  @JsonProperty("scheduledDate")
  private Long scheduledDate;

  @JsonProperty("isActive")
  private Boolean isActive;

  @JsonProperty("type")
  private TypeEnum type = null;

  @JsonProperty("documents")
  @Valid
  private List<Document> documents;

  @JsonProperty("additionalDetails")
  private Object additionalDetails;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails;

  @JsonProperty("workflow")
  private Workflow workflow;
}
