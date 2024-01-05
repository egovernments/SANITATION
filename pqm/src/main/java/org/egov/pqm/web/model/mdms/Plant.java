package org.egov.pqm.web.model.mdms;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Plant {
    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;

    @JsonProperty("active")
    private boolean active;

    @JsonProperty("address")
    private Address address;

    @JsonProperty("plantType")
    private String plantType;

    @JsonProperty("processes")
    private List<String> processes;

    @JsonProperty("wasteType")
    private List<String> wasteType;

    @JsonProperty("description")
    private String description;

    @JsonProperty("effectiveTo")
    private long effectiveTo;

    @JsonProperty("plantConfig")
    private String plantConfig;

    @JsonProperty("effectiveFrom")
    private long effectiveFrom;

}
