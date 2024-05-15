package org.egov.fsm.web.model.garima;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class SanitationWorkerSearchResponse {

    @JsonProperty("responseInfo")
    private ResponseInfo responseInfo = null;

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("message")
    private String message = null;

    @JsonProperty("data")
    private List<SanitationWorkerSearch> data;

    // Add a default constructor
    public SanitationWorkerSearchResponse() {
    }

    public SanitationWorkerSearchResponse responseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
        return this;
    }

    public SanitationWorkerSearchResponse success(boolean success) {
        this.success = success;
        return this;
    }

    public SanitationWorkerSearchResponse message(String message) {
        this.message = message;
        return this;
    }

    public SanitationWorkerSearchResponse data(List<SanitationWorkerSearch> data) {
        this.data = data;
        return this;
    }

    public SanitationWorkerSearchResponse build() {
        SanitationWorkerSearchResponse response = new SanitationWorkerSearchResponse();
        response.setResponseInfo(responseInfo);
        response.setSuccess(success);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

}