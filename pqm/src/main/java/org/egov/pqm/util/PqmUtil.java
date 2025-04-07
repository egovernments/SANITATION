package org.egov.pqm.util;

import org.egov.common.utils.MultiStateInstanceUtil;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PqmUtil {

	private MultiStateInstanceUtil multiStateInstanceUtil;

	@Autowired
	public PqmUtil(MultiStateInstanceUtil multiStateInstanceUtil) {
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
