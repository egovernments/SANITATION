package org.egov.pqm.pqmProducer;

import org.egov.tracer.kafka.CustomKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class PqmProducer {
  @Autowired
  private CustomKafkaTemplate<String, Object> kafkaTemplate;

  public void push(String topic, Object value) {
    kafkaTemplate.send(topic, value);
  }

}
