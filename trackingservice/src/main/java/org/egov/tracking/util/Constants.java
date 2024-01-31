package org.egov.tracking.util;

import org.springframework.beans.factory.annotation.Value;

public class Constants {

    public static final String MSG_RESPONSE_SUCCESS_SAVE = "Data is saved successfully";
    public static final String MSG_RESPONSE_ERROR_SAVE = "Data is not saved. Error";
    public static final String MSG_RESPONSE_MANDATORY_MISSING = "Mandatory parameters are missing";

    public static final String MSG_RESPONSE_ERROR_FETCHING_DATA = "Data is not retrieved. Error";

    public static final String RULE_LOAD_METHOD = "loadModel";
    public static final String RULE_METHOD_PREFIX = "rule";
    public static final int POI_MATCH_THRESHOLD_METERS = 100;
    public static final String MONITORING_USER_ID = "MonitoringService";
    @Value("${egov.fsm.host}")
    public static final String DIGIT_FSM_URL = "";
    @Value("${egov.vehicle.host}")
    public static final String DIGIT_VEHICLE_TRIP_URL = "";
    public static final String ILLEGAL_DUMP_YARD_STOPPAGE_CODE = "Stoppage";
    public static final int ILLEGAL_DUMP_YARD_STOPPAGE_THRESHOLD = 5;
    public static final int TRIP_PROGRESS_FETCH_LIMIT = 1000;
    public static final String FSM_TRIP_COMPLETION_STATUS = "DISPOSE";
    public static final String FSM_TRIP_SEARCH_STATUS_FILTER = "WAITING_FOR_DISPOSAL";
    public static final String TRIP_CLOSE_SYSTEM = "System verified";
    public static final String TRIP_CLOSE_DRIVER = "Driver initiated";

}
