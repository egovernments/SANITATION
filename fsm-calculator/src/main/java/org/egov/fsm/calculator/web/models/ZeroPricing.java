package org.egov.fsm.calculator.web.models;

import javax.validation.Valid;

import org.egov.fsm.calculator.web.models.BillingSlab.StatusEnum;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ZeroPricing {
	public ZeroPricing(String propertyType, String propertySubType) {
		this.propertyType = propertyType;
		this.propertySubType = propertySubType;
		
	}

	@JsonProperty("tenantId")
	private String tenantId;
	@Valid
	@JsonProperty("propertyType")
	private String propertyType = null;
	@Valid
	@JsonProperty("propertySubType")
	private String propertySubType = null;

	public enum StatusEnum {
		ACTIVE("ACTIVE"),

		INACTIVE("INACTIVE");

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
	private StatusEnum status;

}
