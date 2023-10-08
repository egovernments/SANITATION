package org.egov.pqm.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.pqm.repository.querybuilder.PqmQueryBuilder;
import org.egov.pqm.repository.rowmapper.DocumentRowMapper;
import org.egov.pqm.repository.rowmapper.PqmRowMapper;
import org.egov.pqm.web.model.Document;
import org.egov.pqm.web.model.DocumentResponse;
import org.egov.pqm.web.model.Test;
import org.egov.pqm.web.model.TestResponse;
import org.egov.pqm.web.model.TestSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class PqmRepository {

	@Autowired
	private PqmQueryBuilder pqmQueryBuilder;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private PqmRowMapper pqmRowMapper;

	@Autowired
	private DocumentRowMapper documentRowMapper;

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
