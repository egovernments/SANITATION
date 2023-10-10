package org.egov.pqm.web.model.mdms;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MDMSQualityCriteria {

  @JsonProperty("criteriaCode")
  private String criteriaCode = null;

  @JsonProperty("code")
  private String code = null;

  @JsonProperty("parameter")
  private String parameter = null;

  @JsonProperty("units")
  private String units = null;

  @JsonProperty("benchmarkRule")
  private String benchmarkRule = null;

  @JsonProperty("benchmarkValues")
  private String benchmarkValues = null;

  @JsonProperty("allowedDeviation")
  private String allowedDeviation = null;

  @JsonProperty("effectiveFrom")
  private Long effectiveFrom = null;

  @JsonProperty("effectiveTo")
  private String effectiveTo = null;

  @JsonProperty("active")
  private Boolean active = null;

}
