package org.egov.fsm.repository.querybuilder;

import java.util.List;

import org.egov.fsm.config.FSMConfiguration;
import org.egov.fsm.web.model.FSMSearchCriteria;
import org.egov.fsm.web.model.worker.WorkerSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Component
public class FsmWorkerQueryBuilder {

    private static final String WORKER_SEARCH_QUERY = "SELECT count(*) OVER() AS full_count, worker.* FROM eg_fsm_application_worker worker";
    private static final String PAGINATION_WRAPPER = "{} {orderby} {pagination}";

    @Autowired
    private FSMConfiguration config;

    public String getWorkerSearchQuery(WorkerSearchCriteria criteria, List<Object> preparedStmtList) {
        StringBuilder builder = new StringBuilder(WORKER_SEARCH_QUERY);
        if (criteria.getTenantId() != null) {

            if (criteria.getTenantId().split("\\.").length == 1) {
                addClauseIfRequired(preparedStmtList, builder);
                builder.append(" worker.tenantid like ?");
                preparedStmtList.add('%' + criteria.getTenantId() + '%');
            } else {
                addClauseIfRequired(preparedStmtList, builder);
                builder.append(" worker.tenantid=? ");
                preparedStmtList.add(criteria.getTenantId());
            }

            List<String> ids = criteria.getIds();
            if (!CollectionUtils.isEmpty(ids)) {
                addClauseIfRequired(preparedStmtList, builder);
                builder.append(" worker.id IN (").append(createQuery(ids)).append(")");
                addToPreparedStatement(preparedStmtList, ids);
            }

            List<String> individualIds = criteria.getIndividualIds();
            if (!CollectionUtils.isEmpty(individualIds)) {
                addClauseIfRequired(preparedStmtList, builder);
                builder.append(" worker.individual_id IN (").append(createQuery(individualIds)).append(")");
                addToPreparedStatement(preparedStmtList, individualIds);
            }

            List<String> applicationIds = criteria.getApplicationIds();
            if (!CollectionUtils.isEmpty(applicationIds)) {
                addClauseIfRequired(preparedStmtList, builder);
                builder.append(" worker.application_id IN (").append(createQuery(applicationIds)).append(")");
                addToPreparedStatement(preparedStmtList, applicationIds);
            }

            List<String> status = criteria.getStatus();
            if (!CollectionUtils.isEmpty(status)) {
                addClauseIfRequired(preparedStmtList, builder);
                builder.append(" worker.status IN (").append(createQuery(status)).append(")");
                addToPreparedStatement(preparedStmtList, status);
            }

            List<String> workerTypes = criteria.getWorkerTypes();
            if (!CollectionUtils.isEmpty(workerTypes)) {
                addClauseIfRequired(preparedStmtList, builder);
                builder.append(" worker.workerType IN (").append(createQuery(workerTypes)).append(")");
                addToPreparedStatement(preparedStmtList, workerTypes);
            }

        }
        return addPaginationWrapper(builder.toString(), preparedStmtList, criteria);
    }

    private String addPaginationWrapper(String query, List<Object> preparedStmtList,
                                        WorkerSearchCriteria criteria) {

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
    
    /**
	 * 
	 * @param builder
	 * @param criteria
	 */
	private void addOrderByClause(StringBuilder builder, WorkerSearchCriteria criteria) {

		if (StringUtils.isEmpty(criteria.getSortBy()))
			builder.append(" ORDER BY lastmodifiedtime ");

		else if (criteria.getSortBy() == WorkerSearchCriteria.SortBy.createdTime)
			builder.append(" ORDER BY createdTime ");
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

		if (criteria.getSortOrder() == WorkerSearchCriteria.SortOrder.ASC)
			builder.append(" ASC ");
		else
			builder.append(" DESC ");

	}

    private void addClauseIfRequired(List<Object> values, StringBuilder queryString) {
        queryString.append(values.isEmpty() ? " WHERE " : " AND");
    }

    private void addToPreparedStatement(List<Object> preparedStmtList, List<String> ids) {
        preparedStmtList.addAll(ids);
    }

    private Object createQuery(List<String> ids) {
        StringBuilder builder = new StringBuilder();
        int length = ids.size();
        for (int i = 0; i < length; i++) {
            builder.append(" ?");
            if (i != length - 1) {
                builder.append(",");
            }
        }
        return builder.toString();
    }

}