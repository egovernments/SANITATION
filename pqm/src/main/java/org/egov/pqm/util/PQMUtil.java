package org.egov.pqm.util;

import org.egov.pqm.web.model.AuditDetails;
import org.springframework.stereotype.Component;

@Component
public class PQMUtil {

  public AuditDetails getAuditDetails(String by, Boolean isCreate) {
    Long time = System.currentTimeMillis();
    if (isCreate) {
      return AuditDetails.builder().createdBy(by).lastModifiedBy(by).createdTime(time)
          .lastModifiedTime(time)
          .build();
    } else {
      return AuditDetails.builder().lastModifiedBy(by).lastModifiedTime(time).build();
    }
  }

}