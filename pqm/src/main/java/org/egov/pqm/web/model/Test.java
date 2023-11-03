package org.egov.pqm.web.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Test {

  @JsonProperty("testId")
  private String testId;

  @JsonProperty("testCode")
  private String testCode;

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
  @NotNull
  private List<QualityCriteria> qualityCriteria = new ArrayList<>();

  @JsonProperty("status")
  private TestResultStatus status;

  @JsonProperty("wfStatus")
  private String wfStatus;

  @JsonProperty("testType")
  private SourceType sourceType;

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
