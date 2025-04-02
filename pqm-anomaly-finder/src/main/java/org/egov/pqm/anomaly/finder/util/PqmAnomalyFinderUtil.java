package org.egov.pqm.anomaly.finder.util;

import java.util.EnumSet;
import java.util.Set;

import org.egov.common.utils.MultiStateInstanceUtil;
import org.egov.pqm.anomaly.finder.web.model.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;

@Component
public class PqmAnomalyFinderUtil {
	
	private MultiStateInstanceUtil multiStateInstanceUtil;

	@Autowired
	public PqmAnomalyFinderUtil(MultiStateInstanceUtil multiStateInstanceUtil) {
		this.multiStateInstanceUtil = multiStateInstanceUtil;
	}

	public void defaultJsonPathConfig() {
		Configuration.setDefaults(new Configuration.Defaults() {

			private final JsonProvider jsonProvider = new JacksonJsonProvider();
			private final MappingProvider mappingProvider = new JacksonMappingProvider();

			@Override
			public JsonProvider jsonProvider() {
				return jsonProvider;
			}

			@Override
			public MappingProvider mappingProvider() {
				return mappingProvider;
			}

			@Override
			public Set<Option> options() {
				return EnumSet.noneOf(Option.class);
			}
		});
	}

	/**
	 * Method to return auditDetails for create flows
	 *
	 * @param by
	 * @param isCreate
	 * @return AuditDetails
	 */
	public AuditDetails getAuditDetails(String by, Boolean isCreate) {
		Long time = System.currentTimeMillis();
		if (isCreate)
			return AuditDetails.builder().createdBy(by).lastModifiedBy(by).createdTime(time).lastModifiedTime(time)
					.build();
		else
			return AuditDetails.builder().lastModifiedBy(by).lastModifiedTime(time).build();
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
