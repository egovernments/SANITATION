package org.egov.pqm.anomaly.finder.repository.querybuilder;

import java.util.List;

import org.egov.pqm.anomaly.finder.web.model.Test;
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

}
