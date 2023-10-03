package org.egov.pqm.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Document {

  @JsonProperty("id")
  private String id = null;

  @JsonProperty("documentType")
  private String documentType = null;

  @JsonProperty("fileStore")
  private String fileStore = null;

  @JsonProperty("documentUid")
  private String documentUid = null;

  @JsonProperty("additionalDetails")
  private Object additionalDetails = null;

  @JsonProperty("tenantId")
  private String tenantId;

  @JsonProperty("fileStoreId")
  private String fileStoreId;

  @JsonProperty("auditDetails")
  private Map<String, Object> auditDetails;
}
