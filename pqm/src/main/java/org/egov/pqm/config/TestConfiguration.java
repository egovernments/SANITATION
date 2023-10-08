package org.egov.pqm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
@Component
public class TestConfiguration {

	@Value("${egov.test.default.limit}")
	private Integer defaultLimit;

	@Value("${egov.test.default.offset}")
	private Integer defaultOffset;

	@Value("${egov.test.max.limit}")
	private Integer maxSearchLimit;

	// Plant Config
	@Value("${egov.plantMapping.host}")
	private String plantMappingHost;

	@Value("${egov.plantMapping.context.path}")
	private String plantMappingContextPath;

	@Value("${egov.plantMapping.search.path}")
	private String plantMappingSearchEndpoint;

}
