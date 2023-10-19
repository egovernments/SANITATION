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
	private PqmAnomalyConfiguration pqmAnomalyConfiguration;
	
	public void save(PqmAnomalyRequest pqmAnomalyRequest) {
		producer.push(pqmAnomalyConfiguration.getSaveTestAnomalyTopic(), pqmAnomalyRequest);
	}
	
	/*
	 * Added for testing purpose will remove once we done with testing
	 */
	public void save(TestRequest testRequest) {
		producer.push("create-pqm-anomaly-finder", testRequest);
	}

}
