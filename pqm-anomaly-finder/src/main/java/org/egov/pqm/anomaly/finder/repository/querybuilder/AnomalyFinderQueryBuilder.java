package org.egov.pqm.anomaly.finder.repository.querybuilder;

import java.util.List;

import org.egov.pqm.anomaly.finder.web.model.PqmAnomalySearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class AnomalyFinderQueryBuilder {

	private static final String ANOMALYFINDER_QUERY = "select * from eg_pqm_anomaly_details anomaly";

	public String anomalySearchQuery(List<String> testIdLists, List<Object> preparedStmtList) {
		StringBuilder builder = new StringBuilder(ANOMALYFINDER_QUERY);

		if (!CollectionUtils.isEmpty(testIdLists)) {
			addClauseIfRequired(preparedStmtList, builder);
			builder.append(" anomaly.testid IN (").append(createQuery(testIdLists)).append(")");
			addToPreparedStatement(preparedStmtList, testIdLists);
		}
		return builder.toString();
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
	
	public String getPqmAnomalyLikeQuery(PqmAnomalySearchCriteria criteria, List<Object> preparedStmtList) {

		StringBuilder builder = new StringBuilder(ANOMALYFINDER_QUERY);

		List<String> ids = criteria.getIds();
		if (!CollectionUtils.isEmpty(ids)) {

			addClauseIfRequired(preparedStmtList, builder);
			builder.append(" anomaly.id IN (").append(createQuery(ids)).append(")");
			addToPreparedStatement(preparedStmtList, ids);
		}

		return addPaginationClause(builder, preparedStmtList, criteria);

	}
	
	private String addPaginationClause(StringBuilder builder, List<Object> preparedStmtList,
			PqmAnomalySearchCriteria criteria) {

		if (criteria.getLimit() != null && criteria.getLimit() != 0) {
			builder.append(
					"and anomaly.id in (select id from eg_pqm_anomaly_details where tenantid= ? order by id offset ? limit ?)");
			preparedStmtList.add(criteria.getTenantId());
			preparedStmtList.add(criteria.getOffset());
			preparedStmtList.add(criteria.getLimit());

			addOrderByClause(builder, criteria);

		} else {
			addOrderByClause(builder, criteria);
		}
		return builder.toString();
	}
	
	/**
	 * 
	 * @param builder
	 * @param criteria
	 */
	private void addOrderByClause(StringBuilder builder, PqmAnomalySearchCriteria criteria) {

//		if (StringUtils.isEmpty(criteria.getSortBy()))
//			builder.append(" ORDER BY fsm_lastmodifiedtime ");
//
//		else if (criteria.getSortBy() == FSMSearchCriteria.SortBy.locality)
//			builder.append(" ORDER BY fsm_address.locality ");
//
//		else if (criteria.getSortBy() == FSMSearchCriteria.SortBy.applicationStatus)
//			builder.append(" ORDER BY fsm.applicationStatus ");
//
//		else if (criteria.getSortBy() == FSMSearchCriteria.SortBy.applicationNumber)
//			builder.append(" ORDER BY fsm.applicationno ");
//
//		else if (criteria.getSortBy() == FSMSearchCriteria.SortBy.propertyUsage)
//			builder.append(" ORDER BY fsm.propertyUsage ");
//
//		else if (criteria.getSortBy() == FSMSearchCriteria.SortBy.vehicle)
//			builder.append(" ORDER BY fsm.vehicle_id ");
//
//		else if (criteria.getSortBy() == FSMSearchCriteria.SortBy.createdTime)
//			builder.append(" ORDER BY fsm.createdtime ");
//
//		if (criteria.getSortOrder() == FSMSearchCriteria.SortOrder.ASC)
//			builder.append(" ASC ");
//		else
			builder.append("ORDER BY anomaly.id DESC ");

	}

}
