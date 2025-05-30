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

	private final PaymentUpdateService paymentUpdateService;

	@Autowired
	private FSMConfiguration config;

	@Autowired
	public ReceiptConsumer(PaymentUpdateService paymentUpdateService) {
		this.paymentUpdateService = paymentUpdateService;
	}

	@KafkaListener(topicPattern = "${kafka.topics.receipt.create.pattern}")
	public void listenPayments(final HashMap<String, Object> record,
							   @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		log.info("Received payment message on topic: {}", topic);
		paymentUpdateService.process(record);
	}

}
