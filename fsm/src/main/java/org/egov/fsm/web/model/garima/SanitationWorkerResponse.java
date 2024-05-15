package org.egov.fsm.web.model.garima;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.egov.common.contract.response.ResponseInfo;

@Data
@Builder
public class SanitationWorkerResponse {

    @JsonProperty("responseInfo")
    private ResponseInfo responseInfo = null;

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("message")
    private String message = null;

    @JsonProperty("sanitationWorker")
    private SanitationWorkerSearch data = null;

}
