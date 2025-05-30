package org.egov.fsm.consumer;

import java.util.HashMap;

import org.egov.fsm.config.FSMConfiguration;
import org.egov.fsm.service.PaymentUpdateService;
import org.jboss.logging.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ReceiptConsumer {

	private PaymentUpdateService paymentUpdateService;

	@Autowired
	private FSMConfiguration config;

	@Autowired
	public ReceiptConsumer(PaymentUpdateService paymentUpdateService) {
		this.paymentUpdateService = paymentUpdateService;
	}

	@KafkaListener(topicPattern = "${kafka.topics.receipt.create.pattern}")
	public void listenPayments(final HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		log.info("Reached the method for updating the status from payment pending to Assign DSO::@@@");

		if (topic.matches(config.getReceiptTopicPattern())) {
			// Adding in MDC so that tracer can add it in header
//			MDC.put(PTConstants.TENANTID_MDC_STRING, stateLevelTenantID);
			paymentUpdateService.process(record);
		}

	}
}
