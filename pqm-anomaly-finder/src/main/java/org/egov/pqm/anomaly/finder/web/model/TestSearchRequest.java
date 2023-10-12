package org.egov.pqm.anomaly.finder.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.request.RequestInfo;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestSearchRequest {

  @JsonProperty("requestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("testSearchCriteria")
  private TestSearchCriteria testSearchCriteria = null;

  @JsonProperty("pagination")
  private Pagination pagination = null;
}
