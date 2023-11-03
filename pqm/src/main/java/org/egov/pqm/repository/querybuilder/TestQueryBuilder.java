package org.egov.pqm.repository.querybuilder;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.egov.pqm.config.ServiceConfiguration;
import org.egov.pqm.web.model.Pagination;
import org.egov.pqm.web.model.Pagination.SortBy;
import org.egov.pqm.web.model.TestSearchCriteria;
import org.egov.pqm.web.model.TestSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
@Component
public class TestQueryBuilder {


	@Autowired
	private ServiceConfiguration config;

	private static final String QUERY = "select count(*) OVER() AS full_count,test.* from eg_pqm_tests test";


	private static final String PAGINATION_WRAPPER = "{} {orderby} {pagination}";
	
	private static final String DOCUMENT_QUERY = "select * from eg_pqm_test_result_documents";
	
	private static final String QUALITYCRITERIA_QUERY = "select * from eg_pqm_test_criteria_results";

	public String getPqmSearchQuery(TestSearchRequest testSearchRequest, List<Object> preparedStmtList) {

		TestSearchCriteria criteria=testSearchRequest.getTestSearchCriteria();
		StringBuilder builder = new StringBuilder(QUERY);
		if (criteria.getTenantId() != null) {
			if (criteria.getTenantId().split("\\.").length == 1) {
				addClauseIfRequired(preparedStmtList, builder);
				builder.append(" test.tenantid like ?");
				preparedStmtList.add('%' + criteria.getTenantId() + '%');
			} else {
				addClauseIfRequired(preparedStmtList, builder);
				builder.append(" test.tenantid=? ");
				preparedStmtList.add(criteria.getTenantId());
			}
		}
		/*
		 * Enable part search by application number of fsm application
		 */
		List<String> ids = criteria.getTestIds();
		if (!CollectionUtils.isEmpty(ids)) {
			addClauseIfRequired(preparedStmtList, builder);
			builder.append(" test.testId IN (").append(createQuery(ids)).append(")");
			addToPreparedStatement(preparedStmtList, ids);
		}

		List<String> testCodes = criteria.getTestCode();
		if (!CollectionUtils.isEmpty(testCodes)) {
			addClauseIfRequired(preparedStmtList, builder);
			builder.append(" test.testCode IN (").append(createQuery(testCodes)).append(")");
			addToPreparedStatement(preparedStmtList, testCodes);
		}
		
		List<String> plantCodes = criteria.getPlantCodes();
		if (!CollectionUtils.isEmpty(plantCodes)) {
			addClauseIfRequired(preparedStmtList, builder);
			builder.append(" test.plantCode IN (").append(createQuery(plantCodes)).append(")");
			addToPreparedStatement(preparedStmtList, plantCodes);
		}
		
		List<String> processCodes = criteria.getProcessCodes();
		if (!CollectionUtils.isEmpty(processCodes)) {
			addClauseIfRequired(preparedStmtList, builder);
			builder.append(" test.processCode IN (").append(createQuery(processCodes)).append(")");
			addToPreparedStatement(preparedStmtList, processCodes);
		}
		
		List<String> stageCodes = criteria.getStageCodes();
		if (!CollectionUtils.isEmpty(stageCodes)) {
			addClauseIfRequired(preparedStmtList, builder);
			builder.append(" test.stageCode IN (").append(createQuery(stageCodes)).append(")");
			addToPreparedStatement(preparedStmtList, stageCodes);
		}

		List<String> materialCodes = criteria.getMaterialCodes();
		if (!CollectionUtils.isEmpty(materialCodes)) {
			addClauseIfRequired(preparedStmtList, builder);
			builder.append(" test.materialCode IN (").append(createQuery(materialCodes)).append(")");
			addToPreparedStatement(preparedStmtList, materialCodes);

		}

		List<String> deviceCodes = criteria.getDeviceCodes();
		if (!CollectionUtils.isEmpty(deviceCodes)) {
			addClauseIfRequired(preparedStmtList, builder);
			builder.append(" test.deviceCode IN (").append(createQuery(deviceCodes)).append(")");
			addToPreparedStatement(preparedStmtList, deviceCodes);

		}

		List<String> wfStatuses = criteria.getWfStatus();
		if (!CollectionUtils.isEmpty(wfStatuses)) {
			addClauseIfRequired(preparedStmtList, builder);
			builder.append(" test.wfStatus IN (").append(createQuery(wfStatuses)).append(")");
			addToPreparedStatement(preparedStmtList, wfStatuses);
		}
		
		if (criteria.getStatus() != null) {
			addClauseIfRequired(preparedStmtList, builder);
			builder.append(" test.status=? ");
			preparedStmtList.add(criteria.getStatus());

		}
		
		if (criteria.getSourceType() != null) {
			addClauseIfRequired(preparedStmtList, builder);
			builder.append(" test.sourceType=? ");
			preparedStmtList.add(criteria.getSourceType());

		}

		if (criteria.getFromDate() != null && criteria.getToDate() != null) {

			Calendar fromDate = Calendar.getInstance(TimeZone.getDefault());
			fromDate.setTimeInMillis(criteria.getFromDate());
			fromDate.set(Calendar.HOUR_OF_DAY, 0);
			fromDate.set(Calendar.MINUTE, 0);
			fromDate.set(Calendar.SECOND, 0);

			Calendar toDate = Calendar.getInstance(TimeZone.getDefault());
			toDate.setTimeInMillis(criteria.getToDate());
			toDate.set(Calendar.HOUR_OF_DAY, 23);
			toDate.set(Calendar.MINUTE, 59);
			toDate.set(Calendar.SECOND, 59);
			toDate.set(Calendar.MILLISECOND, 0);

			addClauseIfRequired(preparedStmtList, builder);
			builder.append(" test.createdtime BETWEEN ").append(fromDate.getTimeInMillis()).append(" AND ")
					.append(toDate.getTimeInMillis());
		}
		
		return addPaginationWrapper(builder.toString(), preparedStmtList, testSearchRequest);

	}

	/**
	 * 
	 * @param query            prepared Query
	 * @param preparedStmtList values to be replaced on the query
	 * @param testSearchRequest         test search criteria
	 * @return the query by replacing the placeholders with preparedStmtList
	 */
	private String addPaginationWrapper(String query, List<Object> preparedStmtList, TestSearchRequest testSearchRequest) {
		
		Pagination criteria = testSearchRequest.getPagination();

		int limit = config.getDefaultLimit();
		int offset = config.getDefaultOffset();
		String finalQuery = PAGINATION_WRAPPER.replace("{}", query);

		if (criteria.getLimit() != null && criteria.getLimit() <= config.getMaxSearchLimit())
			limit = criteria.getLimit();

		if (criteria.getLimit() != null && criteria.getLimit() > config.getMaxSearchLimit()) {
			limit = config.getMaxSearchLimit();
		}

		if (criteria.getOffset() != null)
			offset = criteria.getOffset();

		StringBuilder orderQuery = new StringBuilder();
		addOrderByClause(orderQuery, criteria);
		finalQuery = finalQuery.replace("{orderby}", orderQuery.toString());

		if (limit == -1) {
			finalQuery = finalQuery.replace("{pagination}", "");
		} else {
			finalQuery = finalQuery.replace("{pagination}", " offset ?  limit ?  ");
			preparedStmtList.add(offset);
			preparedStmtList.add(limit);
		}

		return finalQuery;

	}

	private void addClauseIfRequired(List<Object> values, StringBuilder queryString) {
		if (values.isEmpty())
			queryString.append(" WHERE ");
		else {
			queryString.append(" AND");
		}
	}

	private void addToPreparedStatement(List<Object> preparedStmtList, List<String> ids) {
		ids.forEach(id -> {
			preparedStmtList.add(id);
		});

	}

	private Object createQuery(List<String> ids) {
		StringBuilder builder = new StringBuilder();
		int length = ids.size();
		for (int i = 0; i < length; i++) {
			builder.append(" ?");
			if (i != length - 1)
				builder.append(",");
		}
		return builder.toString();
	}

	/**
	 * 
	 * @param builder
	 * @param criteria
	 */
	private void addOrderByClause(StringBuilder builder, Pagination criteria) {

		if (StringUtils.isEmpty(criteria.getSortBy()))
			builder.append(" ORDER BY test.lastmodifiedtime ");

		else if (criteria.getSortBy() == Pagination.SortBy.wfStatus)
			builder.append(" ORDER BY test.wfStatus ");

		else if (criteria.getSortBy() == Pagination.SortBy.testId)
			builder.append(" ORDER BY test.testId ");

		else if (criteria.getSortBy() == Pagination.SortBy.scheduledDate)
			builder.append(" ORDER BY test.scheduledDate ");

		else if (criteria.getSortBy() == Pagination.SortBy.plantCode)
			builder.append(" ORDER BY test.plantCode ");

		else if (criteria.getSortBy() == Pagination.SortBy.processCode)
			builder.append(" ORDER BY test.processCode ");

		else if (criteria.getSortBy() == Pagination.SortBy.stageCode)
			builder.append(" ORDER BY test.stageCode ");

		else if (criteria.getSortBy() == Pagination.SortBy.materialCode)
			builder.append(" ORDER BY test.materialCode ");

		else if (criteria.getSortBy() == Pagination.SortBy.deviceCode)
			builder.append(" ORDER BY test.deviceCode ");

		else if (criteria.getSortBy() == Pagination.SortBy.createdTime)
			builder.append(" ORDER BY test.createdtime ");

		if (criteria.getSortOrder() == Pagination.SortOrder.ASC)
			builder.append(" ASC ");
		else
			builder.append(" DESC ");

	}


	public String getDocumentSearchQuery(List<String> idList, List<Object> preparedStmtList) {
		StringBuilder builder = new StringBuilder(DOCUMENT_QUERY);
		
		
		if (!CollectionUtils.isEmpty(idList)) {
			addClauseIfRequired(preparedStmtList, builder);
			builder.append(" testId IN (").append(createQuery(idList)).append(")");
			addToPreparedStatement(preparedStmtList, idList);

		}
		return builder.toString();
	}
	
	public String getQualityCriteriaQuery(List<String> idList, List<Object> preparedStmtList) {
		StringBuilder builder = new StringBuilder(QUALITYCRITERIA_QUERY);
		
		
		if (!CollectionUtils.isEmpty(idList)) {
			addClauseIfRequired(preparedStmtList, builder);
			builder.append(" testid IN (").append(createQuery(idList)).append(")");
			addToPreparedStatement(preparedStmtList, idList);

		}
		return builder.toString();
	}

}
