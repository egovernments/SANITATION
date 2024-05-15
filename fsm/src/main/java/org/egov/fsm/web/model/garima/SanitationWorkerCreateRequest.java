package org.egov.fsm.web.model.garima;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.common.contract.request.RequestInfo;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SanitationWorkerCreateRequest {


    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo = null;

    @JsonProperty("sanitationWorker")
    private SanitationWorker sanitationWorker;

}
