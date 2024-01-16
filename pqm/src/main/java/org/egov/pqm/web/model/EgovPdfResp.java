package org.egov.pqm.web.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
@Builder
public class EgovPdfResp {
	List<String> filestoreIds;
}
