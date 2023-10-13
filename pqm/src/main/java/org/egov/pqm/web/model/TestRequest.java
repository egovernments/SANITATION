package org.egov.pqm.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

import lombok.*;
import org.egov.common.contract.request.RequestInfo;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TestRequest {

  @JsonProperty("requestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("tests")
  private List<Test> tests = null;
}
