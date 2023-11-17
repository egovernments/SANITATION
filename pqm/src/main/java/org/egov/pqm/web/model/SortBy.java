package org.egov.pqm.web.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortBy {
    ID("id"),
    TEST_ID("testId"),
    SCHEDULED_DATE("scheduledDate"),
    WORKFLOW_STATUS("wfStatus"),
    PLANT_CODE("plantCode"),
    PROCESS_CODE("processCode"),
    INDIVIDUAL_ID("individualId"),
    STAGE_CODE("stageCode"),
    MATERIAL_CODE("materialCode"),
    DEVICE_CODE("deviceCode"),
    CREATED_TIME("createdTime");

    private final String value;
}
