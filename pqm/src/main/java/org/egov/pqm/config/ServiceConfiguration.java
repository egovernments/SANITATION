package org.egov.pqm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class ServiceConfiguration {

	@Value("${egov.test.default.limit}")
	private Integer defaultLimit;

	@Value("${egov.test.default.offset}")
	private Integer defaultOffset;

	@Value("${egov.test.max.limit}")
	private Integer maxSearchLimit;

	// MDMS
	@Value("${egov.mdms.host}")
	private String mdmsHost;
	
	@Value("${egov.mdms.search.endpoint}")
	private String mdmsEndPoint;
	
	@Value("${egov.mdms.search.v2.endpoint}")
	private String mdmsv2EndPoint;


	// Kafka Topic
	@Value("${egov.mdms.search.v2.endpoint}")
	private String testSaveTopic;

	@Value("${egov.mdms.search.v2.endpoint}")
	private String testUpdateTopic;

	@Value("${egov.mdms.search.v2.endpoint}")
	private String testWorkflowTopic;

}
