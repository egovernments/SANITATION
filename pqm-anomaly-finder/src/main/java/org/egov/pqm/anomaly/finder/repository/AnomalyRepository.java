package org.egov.pqm.anomaly.finder.repository;


import org.egov.pqm.anomaly.finder.config.PqmAnomalyConfiguration;
import org.egov.pqm.anomaly.finder.producer.PqmAnomalyFinderProducer;
import org.egov.pqm.anomaly.finder.web.model.PqmAnomalyRequest;
import org.egov.pqm.anomaly.finder.web.model.TestRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AnomalyRepository {
	
	@Autowired
	private PqmAnomalyFinderProducer producer;
	
	@Autowired
	private PqmAnomalyConfiguration config;
	
	public void save(PqmAnomalyRequest pqmAnomalyRequest) {
		producer.push("save-pqm-test-anomaly-details", pqmAnomalyRequest);
	}
	
	public void save(TestRequest testRequest) {
		producer.push("create-pqm-anomaly-finder", testRequest);
	}

}
