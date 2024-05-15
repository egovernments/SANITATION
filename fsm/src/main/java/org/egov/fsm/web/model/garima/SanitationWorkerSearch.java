package org.egov.fsm.web.model.garima;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.egov.fsm.util.FSMConstants;
import org.egov.fsm.web.model.worker.WorkerType;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Validated
@AllArgsConstructor
@Builder
@Getter
@Setter
public class SanitationWorkerSearch {

    @SafeHtml
    @JsonProperty("isProvisional")
    private boolean isProvisional;

    @SafeHtml
    @JsonProperty("garima_id")
    private String garima_id;

    @NonNull
    @SafeHtml
    @Size(max = 200, min = 2)
    @Pattern(regexp = FSMConstants.PATTERN_GENDER)
    @JsonProperty("worker_name")
    private String name;

    @SafeHtml
    @JsonProperty("dob")
    private String dob;

    @NonNull
    @SafeHtml
    @Pattern(regexp = FSMConstants.PATTERN_GENDER)
    @JsonProperty("gender")
    private String gender;

    @NonNull
    @JsonProperty("city_id")
    private Long city_id;

    @SafeHtml
    @JsonProperty("district_id")
    private Long district_id;

    @NonNull
    @SafeHtml
    @Size(max = 10)
    @JsonProperty("mobile_number")
    private String mobile_number;

    @SafeHtml
    @Size(max = 4)
    @JsonProperty("aadhar_digits")
    private String aadhar_digits;
    
    @JsonProperty("workerType")
    private WorkerType workerType = null;

    public SanitationWorkerSearch() {
    }


}