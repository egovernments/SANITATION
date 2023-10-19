package org.egov.pqm.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pqm.config.ServiceConfiguration;
import org.egov.pqm.pqmProducer.PqmProducer;
import org.egov.pqm.repository.querybuilder.TestQueryBuilder;
import org.egov.pqm.repository.rowmapper.DocumentRowMapper;
import org.egov.pqm.repository.rowmapper.QualityCriteriaRowMapper;
import org.egov.pqm.repository.rowmapper.TestRowMapper;
import org.egov.pqm.web.model.Document;
import org.egov.pqm.web.model.DocumentResponse;
import org.egov.pqm.web.model.Pagination;
import org.egov.pqm.web.model.QualityCriteria;
import org.egov.pqm.web.model.Test;
import org.egov.pqm.web.model.TestRequest;
import org.egov.pqm.web.model.TestResponse;
import org.egov.pqm.web.model.TestSearchCriteria;
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
  private QualityCriteriaRowMapper qualityCriteriaRowMapper;
  
  @Autowired
  private PqmProducer producer;
  
	@Autowired
	private ServiceConfiguration config;

	public void save(TestRequest testRequest) {
		producer.push(config.getTestSaveTopic(), testRequest);
	}

  public void saveAnomaly(String topic, TestRequest testRequest) {
    producer.push(topic, testRequest);
  }


  public void update(TestRequest testRequest) {

    Test test = testRequest.getTests().get(0);
    RequestInfo requestInfo = testRequest.getRequestInfo();
    producer.push(config.getTestUpdateTopic(),
        new TestRequest(requestInfo, Collections.singletonList(test)));

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
    List<Document> documents = jdbcTemplate.query(query, preparedStmtList.toArray(),
        documentRowMapper);
    return DocumentResponse.builder().documents(documents).build();
  }
  
  public List<QualityCriteria> getQualityCriteriaData(List<String> idList) {
	    List<Object> preparedStmtList = new ArrayList<>();
	    String query = pqmQueryBuilder.getQualityCriteriaQuery(idList, preparedStmtList);
	    List<QualityCriteria> qualityCriterias = jdbcTemplate.query(query, preparedStmtList.toArray(),
	    		qualityCriteriaRowMapper);
	    return qualityCriterias;
	  }

  public List<Test> fetchFromDB(TestRequest testRequest) {

    List<String> ids = Collections.singletonList(testRequest.getTests().get(0).getId());

    TestSearchCriteria criteria = TestSearchCriteria.builder()
        .ids(ids).tenantId(testRequest.getTests().get(0).getTenantId())
        .build();
    Pagination Pagination = new Pagination();
    TestSearchRequest request = TestSearchRequest.builder()
        .testSearchCriteria(criteria).pagination(Pagination)
        .build();

    TestResponse testResponse = getPqmData(request);
    List<Test> tests = testResponse.getTests();
    return tests;
  }
}
