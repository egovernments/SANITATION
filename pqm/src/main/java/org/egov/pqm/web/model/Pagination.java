package org.egov.pqm.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pagination {


	@JsonProperty("offset")
	  private Integer offset;

	  @JsonProperty("limit")
	  private Integer limit;
	  
	  @JsonProperty("sortBy")
	  private SortBy sortBy;
	  
	  @JsonProperty("sortOrder")
	  private SortOrder sortOrder;
	  
	  public enum SortOrder {
	      ASC,
	      DESC
	  }

	  public enum SortBy {
		  wfStatus,
		  testId,
		  plantCode,
		  processCode,
		  stageCode,
		  materialCode,
		  deviceCode,
	      createdTime,
			scheduledDate
	  }
}
