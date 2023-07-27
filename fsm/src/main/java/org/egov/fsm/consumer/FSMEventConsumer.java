package org.egov.fsm.consumer;

import java.util.HashMap;

import org.egov.fsm.util.FSMEventUtil;
import org.egov.fsm.web.model.FSMEvent;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FSMEventConsumer {
	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private FSMEventUtil eventProcessingUtil;

	@KafkaListener(topics = { "${fsm.event.kafka.topic}" })
	public void listen(final HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		try {
			log.info("Received event on topic - " + record);
			FSMEvent fsmEvent = mapper.convertValue(record, FSMEvent.class);
			log.info("Received event on topic - " + topic + "Received Request body for processFSMEvent " + fsmEvent);
			eventProcessingUtil.processFSMEvent(fsmEvent);
		} catch (Exception e) {
			throw new CustomException("PARSING_ERROR", "Failed to parse record to fsmEvent" + e.getMessage());
		}
	}

	public void pushEvent(FSMEvent event) {
		eventProcessingUtil.processFSMEvent(event);
	}

}
