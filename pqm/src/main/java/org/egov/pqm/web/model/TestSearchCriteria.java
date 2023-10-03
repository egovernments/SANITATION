package org.egov.pqm.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestSearchCriteria {

  @JsonProperty("ids")
  @Valid
  private List<String> ids = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("plats")
  @Valid
  private List<String> plats = null;

  @JsonProperty("processes")
  @Valid
  private List<String> processes = null;

  @JsonProperty("stages")
  @Valid
  private List<String> stages = null;

  @JsonProperty("materials")
  @Valid
  private List<String> materials = null;

  @JsonProperty("deviceCode")
  @Valid
  private List<String> deviceCode = null;

  @JsonProperty("testStatus")
  private String testStatus = null;

  @JsonProperty("resultStatus")
  private TestResultStatus resultStatus = null;

  @JsonProperty("testType")
  private String testType = null;

  @JsonProperty("fromDate")
  private Long fromDate = null;

  @JsonProperty("toDate")
  private Long toDate = null;
}
