package org.egov.vehicle.trip.util;

import org.egov.common.utils.MultiStateInstanceUtil;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VehicleTripUtil {

	private MultiStateInstanceUtil multiStateInstanceUtil;

	@Autowired
	public VehicleTripUtil(MultiStateInstanceUtil multiStateInstanceUtil) {
		this.multiStateInstanceUtil = multiStateInstanceUtil;
	}

	/**
	 * Method to fetch the state name from the tenantId
	 *
	 * @param query
	 * @param tenantId
	 * @return
	 */
	public String replaceSchemaPlaceholder(String query, String tenantId) {

		String finalQuery = null;

		try {
			finalQuery = multiStateInstanceUtil.replaceSchemaPlaceholder(query, tenantId);
		} catch (Exception e) {
			throw new CustomException("INVALID_TENANTID", "Invalid tenantId: " + tenantId);
		}
		return finalQuery;
	}
}
