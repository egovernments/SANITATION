package org.egov.pqm.util;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PqmUtil {

	/** * Method to fetch the state name from the tenantId * * @param query * @param tenantId * @return */
	public String replaceSchemaPlaceholder(String query, String tenantId) {
	    String finalQuery = null;
	    if (tenantId.contains(".")) {
	        String schemaName = tenantId.split("\\.")[1];
	        finalQuery = query.replace(Constants.SCHEMA_REPLACE_STRING, schemaName);
	    } else {
	        finalQuery = query.replace(Constants.SCHEMA_REPLACE_STRING.concat("."), "");
	    }
	    return finalQuery;
	} 
}
