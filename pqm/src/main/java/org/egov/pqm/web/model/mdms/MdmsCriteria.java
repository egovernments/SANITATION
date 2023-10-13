package org.egov.pqm.web.model.mdms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Map;
import java.util.Set;


@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MdmsCriteria {

  @JsonProperty("tenantId")
  @Size(min = 1, max = 100)
  @NotNull
  private String tenantId = null;

  @JsonProperty("ids")
  private Set<String> ids = null;

  @JsonProperty("uniqueIdentifier")
  @Size(min = 1, max = 64)
  private String uniqueIdentifier = null;

  @JsonProperty("schemaCode")
  private String schemaCode = null;

  @JsonProperty("filters")
  private Map<String, String> filterMap = null;

  @JsonProperty("isActive")
  private Boolean isActive = null;

  @JsonIgnore
  private Map<String, String> schemaCodeFilterMap = null;

  @JsonIgnore
  private Set<String> uniqueIdentifiersForRefVerification = null;

  @JsonProperty("offset")
  private Integer offset;

  @JsonProperty("limit")
  private Integer limit;


}