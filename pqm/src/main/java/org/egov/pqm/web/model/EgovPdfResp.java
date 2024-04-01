package org.egov.pqm.web.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Builder
public class EgovPdfResp {
	
	@JsonProperty("filestoreIds")
	List<String> filestoreIds;
}
