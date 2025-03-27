package org.egov.fsm.fsmProducer;

import org.egov.fsm.config.FSMConfiguration;
import org.egov.tracer.kafka.CustomKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FSMProducer {
	@Autowired
	private CustomKafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private FSMConfiguration config;

	public void push(String tenantId, String topic, Object value) {

		String updatedTopic = topic;
		if (config.getIsEnvironmentCentralInstance()) {

			String[] tenants = tenantId.split("\\.");
			if (tenants.length > 1)
				updatedTopic = tenants[1].concat("-").concat(topic);
		}
		log.info("The Kafka topic for the tenantId : " + tenantId + " is : " + updatedTopic);
		kafkaTemplate.send(updatedTopic, value);
	}
}
