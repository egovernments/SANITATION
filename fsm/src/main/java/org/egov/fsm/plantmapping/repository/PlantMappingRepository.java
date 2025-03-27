package org.egov.fsm.plantmapping.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.fsm.fsmProducer.FSMProducer;
import org.egov.fsm.plantmapping.config.PlantMappingConfiguration;
import org.egov.fsm.plantmapping.querybuilder.PlantMappingQueryBuilder;
import org.egov.fsm.plantmapping.rowmapper.PlantMappingRowMapper;
import org.egov.fsm.plantmapping.util.PlantMappingUtils;
import org.egov.fsm.plantmapping.web.model.PlantMapping;
import org.egov.fsm.plantmapping.web.model.PlantMappingRequest;
import org.egov.fsm.plantmapping.web.model.PlantMappingResponse;
import org.egov.fsm.plantmapping.web.model.PlantMappingSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PlantMappingRepository {


	@Autowired
	private FSMProducer producer;
	
	@Autowired
	private PlantMappingConfiguration config;
	
	@Autowired
	private PlantMappingQueryBuilder queryBuilder;

	@Autowired
	private PlantMappingRowMapper rowMapper;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private PlantMappingUtils plantMappingUtils;
	
	public void save(PlantMappingRequest request) {
		producer.push(request.getPlantMapping().getTenantId(), config.getSaveTopic(), request);
	}

	public PlantMappingResponse getPlantMappingData(PlantMappingSearchCriteria criteria) {
		List<Object> preparedStmtList = new ArrayList<>();
		String query = queryBuilder.getPlantMapSearchQuery(criteria, preparedStmtList);
	    query = plantMappingUtils.replaceSchemaPlaceholder(query, criteria.getTenantId());
		List<PlantMapping> plantmaps = jdbcTemplate.query(query, preparedStmtList.toArray(), rowMapper);
		return PlantMappingResponse.builder().plantMapping(plantmaps).build();
	}

	public void update(PlantMappingRequest request) {
		producer.push(request.getPlantMapping().getTenantId(),config.getUpdateTopic(), request);
	}
}
