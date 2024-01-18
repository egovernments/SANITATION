package org.egov.pqm.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EgovPdfResp {

	@JsonProperty("filestoreIds")
	List<String> filestoreIds;
}
