package org.egov.pqm.web.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QualityCriteriaResponse {

	  @JsonProperty("qualityCriteria")
	  private List<QualityCriteria> qualityCriteria = null;
}
