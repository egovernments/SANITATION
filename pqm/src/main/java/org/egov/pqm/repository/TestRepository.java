package org.egov.pqm.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pqm.config.ServiceConfiguration;
import org.egov.pqm.pqmProducer.PqmProducer;
import org.egov.pqm.repository.querybuilder.TestQueryBuilder;
import org.egov.pqm.repository.rowmapper.DocumentRowMapper;
import org.egov.pqm.repository.rowmapper.TestRowMapper;
import org.egov.pqm.web.model.Document;
import org.egov.pqm.web.model.DocumentResponse;
import org.egov.pqm.web.model.Test;
import org.egov.pqm.web.model.TestRequest;
import org.egov.pqm.web.model.TestResponse;
import org.egov.pqm.web.model.TestSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class TestRepository {

	@Autowired
	private TestQueryBuilder pqmQueryBuilder;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private TestRowMapper pqmRowMapper;

	@Autowired
	private DocumentRowMapper documentRowMapper;

	@Autowired
	private PqmProducer producer;

	@Autowired
	private ServiceConfiguration config;

	public void save(TestRequest testRequest) {
		producer.push(config.getTestSaveTopic(), testRequest);
	}

	public void update(TestRequest testRequest, boolean isStateUpdatable) {

		Test test = testRequest.getTests().get(0);
		RequestInfo requestInfo = testRequest.getRequestInfo();

		if (isStateUpdatable) {
			producer.push(config.getTestUpdateTopic(), new TestRequest(requestInfo, Collections.singletonList(test)));
		} else {
			producer.push(config.getTestWorkflowTopic(), new TestRequest(requestInfo, Collections.singletonList(test)));
		}
	}

	public TestResponse getPqmData(TestSearchRequest testSearchCriteria) {
		List<Object> preparedStmtList = new ArrayList<>();
		String query = pqmQueryBuilder.getPqmSearchQuery(testSearchCriteria, preparedStmtList);

		List<Test> tests = jdbcTemplate.query(query, preparedStmtList.toArray(), pqmRowMapper);
		return TestResponse.builder().tests(tests).totalCount(pqmRowMapper.getFullCount()).build();
	}

	public DocumentResponse getDocumentData(List<String> idList) {
		List<Object> preparedStmtList = new ArrayList<>();
		String query = pqmQueryBuilder.getDocumentSearchQuery(idList, preparedStmtList);
		List<Document> documents = jdbcTemplate.query(query, preparedStmtList.toArray(), documentRowMapper);
		return DocumentResponse.builder().documents(documents).build();
	}
}
