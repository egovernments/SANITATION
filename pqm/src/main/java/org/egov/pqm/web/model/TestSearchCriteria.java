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

  @JsonProperty("plantCodes")
  @Valid
  private List<String> plantCodes = null;

  @JsonProperty("processCodes")
  @Valid
  private List<String> processCodes = null;

  @JsonProperty("stageCodes")
  @Valid
  private List<String> stageCodes = null;

  @JsonProperty("materialCodes")
  @Valid
  private List<String> materialCodes = null;

  @JsonProperty("deviceCodes")
  @Valid
  private List<String> deviceCodes = null;

  @JsonProperty("wfStatus")
  private String wfStatus = null;

  @JsonProperty("status")
  private TestResultStatus status = null;

  @JsonProperty("testType")
  private String testType = null;

  @JsonProperty("fromDate")
  private Long fromDate = null;

  @JsonProperty("toDate")
  private Long toDate = null;
  
}
