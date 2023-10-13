package org.egov.pqm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
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
@Configuration
@PropertySource("classpath:application.properties")
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
	@Value("${egov.test.create.kafka.topic}")
	private String testSaveTopic;

	@Value("${egov.test.update.kafka.topic}")
	private String testUpdateTopic;

	@Value("${egov.test.update.workflow.kafka.topic}")
	private String testWorkflowTopic;

	//workflow
	@Value("${create.pqm.workflow.name}")
	private String businessServiceValue;

	@Value("${workflow.context.path}")
	private String wfHost;

	@Value("${workflow.transition.path}")
	private String wfTransitionPath;

	@Value("${workflow.businessservice.search.path}")
	private String wfBusinessServiceSearchPath;

	@Value("${workflow.process.path}")
	private String wfProcessPath;

}
