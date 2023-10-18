package org.egov.pqm.web.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class QualityCriteria {

  @JsonProperty("id")
  private String id;

  @JsonProperty("testId")
  private String testId;

  @JsonProperty("criteriaCode")
  private String criteriaCode = null;

  @JsonProperty("resultValue")
  private BigDecimal resultValue = null;

  @JsonProperty("allowedDeviation")
  private BigDecimal allowedDeviation = null;

  /**
   * The result status of the individual quality criteria evaluated with respect to benchmark rules
   * and standard values from MDMS data.
   */
  public enum StatusEnum {
    PASS("PASS"),

    FAIL("FAIL"),

    PENDING("PENDING");

    private final String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusEnum fromValue(String text) {
      for (StatusEnum b : StatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("resultStatus")
  private StatusEnum resultStatus = null;

  @JsonProperty("isActive")
  private Boolean isActive = Boolean.TRUE;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails;
}
