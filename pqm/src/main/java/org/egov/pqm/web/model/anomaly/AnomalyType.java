package org.egov.pqm.web.model.anomaly;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AnomalyType {

	LAB_RESULTS_NOT_AS_PER_BENCHMARK,
	IOT_DEVICE_RESULTS_NOT_AS_PER_BENCHMARK,
	LAB_RESULTS_AND_DEVICE_RESULTS_DO_NOT_MATCH
	
}
