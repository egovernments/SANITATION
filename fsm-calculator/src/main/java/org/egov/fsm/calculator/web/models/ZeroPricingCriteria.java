package org.egov.fsm.calculator.web.models;

import org.egov.fsm.calculator.web.models.ZeroPricing.StatusEnum;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ZeroPricingCriteria {
	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("offset")
	private Integer offset;

	@JsonProperty("limit")
	private Integer limit;
	@JsonProperty("propertyType")
	private String propertyType;

	@JsonProperty("propertySubType")
	private String propertySubType;

	@JsonProperty("sortBy")
	private SortBy sortBy;

	@JsonProperty("sortOrder")
	private SortOrder sortOrder;

	public enum SortOrder {
		ASC, DESC
	}

	public enum SortBy {
		PROPERTYTYPE, SUBPROPERTYTYPE
	}

	@JsonProperty("status")
	private StatusEnum status ;
}
