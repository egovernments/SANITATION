package org.egov.fsm.repository;
import java.util.ArrayList;
import java.util.List;

import org.egov.fsm.util.FSMUtil;
import org.egov.fsm.web.model.vehicle.trip.VehicleTripSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class FSMInboxRepository {

        @Autowired
    	private JdbcTemplate jdbcTemplate;
        
       @Autowired
       private FSMUtil fsmUtil;


    	private static final String QUERY_VEHICLE_STATE = "select fsm.applicationno from {schema}.eg_fsm_application fsm , {schema}.eg_vehicle_trip_detail vhd, {schema}.eg_vehicle_trip vh, {schema}.eg_wf_state_v2 wfs "
    			+ " where fsm.applicationno=vhd.referenceno and vh.id=vhd.trip_id and vh.applicationstatus=wfs.applicationstatus  ";

		public List<String> fetchVehicleStateMap(VehicleTripSearchCriteria vehicleTripSearchCriteria) {
			StringBuilder builder = new StringBuilder(QUERY_VEHICLE_STATE);
			List<Object> preparedStmtList = new ArrayList<>();

			if (!CollectionUtils.isEmpty(vehicleTripSearchCriteria.getApplicationStatus() )) {
				builder.append(" and wfs.uuid IN (").append(createQuery(vehicleTripSearchCriteria.getApplicationStatus())).append(")");
				addToPreparedStatement(preparedStmtList, vehicleTripSearchCriteria.getApplicationStatus());
			}

			String query = addPaginationClause(builder,preparedStmtList,vehicleTripSearchCriteria);
			log.info("query from 	fetchVehicleStateMap :::: " + query);
		    query = fsmUtil.replaceSchemaPlaceholder(query, vehicleTripSearchCriteria.getTenantId());
			return jdbcTemplate.queryForList(query, preparedStmtList.toArray(), String.class);
		}



		private void addToPreparedStatement(List<Object> preparedStmtList, List<String> ids) {
			ids.forEach(id -> {
				preparedStmtList.add(id);
			});

		}

		private String addPaginationClause(StringBuilder builder, List<Object> preparedStmtList,
				VehicleTripSearchCriteria criteria) {

			log.info("criteria.getLimit() :::: " + criteria.getLimit());

			if (criteria.getLimit()!=null && criteria.getLimit() != 0) {
				builder.append(" order by fsm.applicationno offset ? limit ?");
				preparedStmtList.add(criteria.getOffset());
				preparedStmtList.add(criteria.getLimit());
			}


			log.info("preparedStmtList :::: " + preparedStmtList);
			log.info("preparedStmtList size :::: " + preparedStmtList.size());

			return builder.toString();
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