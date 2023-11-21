package org.egov.pqm.web.model.plant.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.pqm.web.model.AuditDetails;

import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PlantUser {
    @JsonProperty("id")
    @Size(
            min = 2,
            max = 64
    )
    private String id;

    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("plantCode")
    private String plantCode;

    @JsonProperty("individualId")
    @Size(
            min = 2,
            max = 64
    )
    private String individualId;

    @JsonProperty("isActive")
    private Boolean isActive;

    @JsonProperty("additionalDetails")
    private Object additionalDetails;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails;
}
