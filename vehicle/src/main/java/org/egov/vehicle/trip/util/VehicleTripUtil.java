package org.egov.vehicle.trip.util;

import org.springframework.stereotype.Component;
import org.egov.vehicle.trip.util.VehicleTripConstants;

@Component
public class VehicleTripUtil {

	/** * Method to fetch the state name from the tenantId * * @param query * @param tenantId * @return */
	public String replaceSchemaPlaceholder(String query, String tenantId) {
	    String finalQuery = null;
	    if (tenantId.contains(".")) {
	        String schemaName = tenantId.split("\\.")[1];
	        finalQuery = query.replace(VehicleTripConstants.SCHEMA_REPLACE_STRING, schemaName);
	    } else {
	        finalQuery = query.replace(VehicleTripConstants.SCHEMA_REPLACE_STRING.concat("."), "");
	    }
	    return finalQuery;
	} 
}
