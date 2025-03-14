package org.egov.vendor.driver.web.model;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.egov.vendor.web.model.AuditDetails;
import org.egov.vendor.web.model.user.User;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import jakarta.annotation.Generated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



/**
 * Capture the Driver information in the system.
 */
@Validated
@Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-01-06T05:34:12.238Z[GMT]")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Driver {

	@JsonProperty("id")
	private String id = null;

	@JsonProperty("tenantId")
	@Size(max=64)
	private String tenantId = null;

	@JsonProperty("name")
	@Size(max=128)
	private String name = null;

	@JsonProperty("owner")
	@Valid
	private User owner = null;
	
	@JsonProperty("ownerId")
	@Size(max=64)
	private String ownerId = null;

	@JsonProperty("additionalDetails")
	private Object additionalDetails = null;

	@JsonProperty("description")
	private String description = null;
	
	@JsonProperty("licenseNumber")
	private String licenseNumber = null;
	
	/*
	 * @JsonProperty("vendor")
	 * 
	 * @Valid private Vendor vendor = null;
	 */	
	/**
	 * Inactive records will be consider as soft deleted
	 */
	public enum StatusEnum {
		ACTIVE("ACTIVE"),
		INACTIVE("INACTIVE"),
		DISABLED("DISABLED");

		private String value;

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

	@JsonProperty("status")
	private StatusEnum status = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
	
	@JsonProperty("vendorDriverStatus")
	private StatusEnum vendorDriverStatus = null;

}
