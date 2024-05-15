package org.egov.fsm.web.model.garima;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SanitationWorkerSearchCriteria {

    @NotNull
    @JsonProperty("garima_id")
    private String garimaId;

}
