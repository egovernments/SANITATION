package org.egov.vendor.driver.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkerSearchCriteria {

	@JsonProperty("offset")
	private Integer offset;

	@JsonProperty("limit")
	private Integer limit;

	@JsonProperty("tenantId")
	private String tenantId;

	@JsonProperty("ids")
	private List<String> ids;

	@JsonProperty("status")
	private List<String> vendorWorkerStatus;

	@JsonProperty("individualIds")
	private List<String> individualIds;

	@JsonProperty("vendorIds")
	private List<String> vendorIds;

	@JsonProperty("sortBy")
	private SortBy sortBy;

	@JsonProperty("sortOrder")
	private SortOrder sortOrder;

	public enum SortOrder {
		ASC, DESC
	}

	public enum SortBy {
		tenantId, mobileNumber, ownerIds, name, dsoName, ids, status, driverWithNoVendor, createdTime

	}
}
