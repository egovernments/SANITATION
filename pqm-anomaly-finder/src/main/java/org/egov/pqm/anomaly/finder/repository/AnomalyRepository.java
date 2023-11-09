package org.egov.pqm.anomaly.finder.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.pqm.anomaly.finder.config.PqmAnomalyConfiguration;
import org.egov.pqm.anomaly.finder.producer.PqmAnomalyFinderProducer;
import org.egov.pqm.anomaly.finder.repository.querybuilder.AnomalyFinderQueryBuilder;
import org.egov.pqm.anomaly.finder.repository.rowmapper.AnomalyFinderRowMapper;
import org.egov.pqm.anomaly.finder.web.model.PqmAnomaly;
import org.egov.pqm.anomaly.finder.web.model.PqmAnomalyRequest;
import org.egov.pqm.anomaly.finder.web.model.TestRequest;
import org.egov.pqm.anomaly.finder.web.model.TestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AnomalyRepository {

	@Autowired
	private PqmAnomalyFinderProducer pqmAnomalyFinderProducer;

	@Autowired
	private PqmAnomalyConfiguration pqmAnomalyConfiguration;

	@Autowired
	private AnomalyFinderQueryBuilder anomalyFinderQueryBuilder;

	@Autowired
	private AnomalyFinderRowMapper anomalyFinderRowMapper;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void save(PqmAnomalyRequest pqmAnomalyRequest) {
		pqmAnomalyFinderProducer.push(pqmAnomalyConfiguration.getSaveTestAnomalyTopic(), pqmAnomalyRequest);
	}

	/*
	 * Added for testing purpose will remove once we done with testing
	 */
	public void save(TestRequest testRequest) {
		pqmAnomalyFinderProducer.push("create-pqm-anomaly-finder", testRequest);
	}

	public List<PqmAnomaly> getAnomalyFinderData(List<String> testIdLists) {
		List<Object> preparedStmtList = new ArrayList<>();
		String query = anomalyFinderQueryBuilder.anomalySearchQuery(testIdLists, preparedStmtList);
		List<PqmAnomaly> pqmAnomalys = jdbcTemplate.query(query, preparedStmtList.toArray(), anomalyFinderRowMapper);
		return pqmAnomalys;
	}

}
