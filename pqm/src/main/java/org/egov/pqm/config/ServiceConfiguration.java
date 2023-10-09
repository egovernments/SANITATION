package org.egov.pqm.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;


@Component
@Data
@Import({TracerConfiguration.class})
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ServiceConfiguration {

  //MDMS
  @Value("${egov.mdms.host}")
  private String mdmsHost;
  @Value("${egov.mdms.search.endpoint}")
  private String mdmsEndPoint;
  @Value("${egov.mdms.search.v2.endpoint}")
  private String mdmsv2EndPoint;

}
