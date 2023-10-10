package org.egov.pqm.web.model.idgen;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.response.ResponseInfo;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IdGenerationResponse {
	private ResponseInfo responseInfo;

    private List<IdResponse> idResponses;

	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
	}

	public List<IdResponse> getIdResponses() {
		return idResponses;
	}

	public void setIdResponses(List<IdResponse> idResponses) {
		this.idResponses = idResponses;
	}
    

}
