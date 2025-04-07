package org.egov.pqm.anomaly.finder.repository;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.pqm.anomaly.finder.config.PqmAnomalyConfiguration;
import org.egov.pqm.anomaly.finder.producer.PqmAnomalyFinderProducer;
import org.egov.pqm.anomaly.finder.repository.querybuilder.AnomalyFinderQueryBuilder;
import org.egov.pqm.anomaly.finder.repository.rowmapper.AnomalyFinderRowMapper;
import org.egov.pqm.anomaly.finder.util.PqmAnomalyFinderUtil;
import org.egov.pqm.anomaly.finder.web.model.PqmAnomaly;
import org.egov.pqm.anomaly.finder.web.model.PqmAnomalyRequest;
import org.egov.pqm.anomaly.finder.web.model.PqmAnomalySearchCriteria;
import org.egov.pqm.anomaly.finder.web.model.TestRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
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
	
	@Autowired
	private PqmAnomalyFinderUtil util;

	public void save(PqmAnomalyRequest pqmAnomalyRequest) {
		pqmAnomalyFinderProducer.push(pqmAnomalyRequest.getPqmAnomalys().get(0).getTenantId(), pqmAnomalyConfiguration.getSaveTestAnomalyTopic(), pqmAnomalyRequest);
	}

	/*
	 * Added for testing purpose will remove once we done with testing
	 */
	public void save(TestRequest testRequest) {
		pqmAnomalyFinderProducer.push(testRequest.getTests().get(0).getTenantId(), pqmAnomalyConfiguration.getNotAsPerBenchMark(), testRequest);
	}

	public List<PqmAnomaly> getAnomalyFinderData(List<String> testIdLists, String tenantId) {
		List<Object> preparedStmtList = new ArrayList<>();
		String query = anomalyFinderQueryBuilder.anomalySearchQuery(testIdLists, preparedStmtList);
	    query = util.replaceSchemaPlaceholder(query, tenantId);
		List<PqmAnomaly> pqmAnomalys = jdbcTemplate.query(query, preparedStmtList.toArray(), anomalyFinderRowMapper);
		return pqmAnomalys;
	}

	public List<PqmAnomaly> getAnomalyDataForCriteria(PqmAnomalySearchCriteria searchCriteria) {
		List<Object> preparedStmtList = new ArrayList<>();
		String query = anomalyFinderQueryBuilder.anomalySearchQueryWithCriteria(searchCriteria, preparedStmtList);
	    query = util.replaceSchemaPlaceholder(query, searchCriteria.getTenantId());
		log.info("query->                           "+query);
		List<PqmAnomaly> pqmAnomalys = jdbcTemplate.query(query, preparedStmtList.toArray(), anomalyFinderRowMapper);
		log.info("preparedStmtList -> " +preparedStmtList);
		return pqmAnomalys;
	}

	public List<String> fetchPqmAnomalyIds(@Valid PqmAnomalySearchCriteria criteria) {

		List<Object> preparedStmtList = new ArrayList<>();
		preparedStmtList.add(criteria.getOffset());
		preparedStmtList.add(criteria.getLimit());
		String query = "SELECT id from {schema}.eg_pqm_anomaly_details ORDER BY createdtime offset " + " ? "
				+ "limit ? ";
		query = util.replaceSchemaPlaceholder(query, criteria.getTenantId());
		return jdbcTemplate.query(query, preparedStmtList.toArray(), new SingleColumnRowMapper<>(String.class));
	}

	public List<PqmAnomaly> getPqmAnomalyPlainSearch(PqmAnomalySearchCriteria criteria) {

		if (criteria.getIds() == null || criteria.getIds().isEmpty())
			throw new CustomException("PLAIN_SEARCH_ERROR", "Search only allowed by ids!");

		List<Object> preparedStmtList = new ArrayList<>();
		String query = anomalyFinderQueryBuilder.getPqmAnomalyLikeQuery(criteria, preparedStmtList);
	    query = util.replaceSchemaPlaceholder(query, criteria.getTenantId());
		log.info("Query: " + query);
		log.info("PS: " + preparedStmtList);
		return jdbcTemplate.query(query, preparedStmtList.toArray(), anomalyFinderRowMapper);
	}

}
