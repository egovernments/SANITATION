package org.egov.tracking.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class ApplicationConfig {

  @Value("${egov.fsm.host}")
  private String fsmUrl;

  @Value("${egov.vehicle.host}")
  private String vehicleTripUrl;

}
