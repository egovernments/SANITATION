package org.egov.fsm.plantmapping.util;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PlantMappingUtils {

	/* * central Instance enhancement */
	public String replaceSchemaPlaceholder(String query, String tenantId) {
	    String finalQuery = null;
	    if (tenantId.contains(".")) {
	        String schemaName = tenantId.split("\\.")[1];
	        finalQuery = query.replace(PlantMappingConstants.SCHEMA_REPLACE_STRING, schemaName);
	    } else {
	        finalQuery = query.replace(PlantMappingConstants.SCHEMA_REPLACE_STRING.concat("."), "");
	    }
	    return finalQuery;
	} 
}
