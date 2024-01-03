package org.egov.vendor.driver.repository.rowmapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.egov.tracer.model.CustomException;
import org.egov.vendor.driver.web.model.Worker;
import org.egov.vendor.driver.web.model.WorkerStatus;
import org.egov.vendor.web.model.AuditDetails;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

@Component
public class WorkerRowMapper implements ResultSetExtractor<List<Worker>> {

  @Autowired
  private ObjectMapper mapper;

  private int fullCount = 0;

  public int getFullCount() {
    return fullCount;
  }

  public void setFullCount(int fullCount) {
    this.fullCount = fullCount;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public List<Worker> extractData(ResultSet rs) throws SQLException {
    Map<String, Worker> workerMap = new LinkedHashMap<>();
    this.setFullCount(0);

    while (rs.next()) {
      Worker currentWorker = new Worker();
      String id = rs.getString("id");
      String tenantId = rs.getString("tenantid");
      String vendorId = rs.getString("vendor_id");
      String individualId = rs.getString("individual_id");
      String status = rs.getString("vendor_sw_status");
      Object additionalDetail = getAdditionalDetail("additionaldetails", rs);
      currentWorker = workerMap.get(id);
      this.setFullCount(rs.getInt("full_count"));

      if (currentWorker == null) {
        currentWorker = Worker.builder()
            .id(id)
            .tenantId(tenantId)
            .individualId(individualId)
            .vendorId(vendorId)
            .additionalDetails(additionalDetail)
            .vendorWorkerStatus(WorkerStatus.valueOf(status))
            .build();

        workerMap.put(id, currentWorker);
      }
      addChildrenToProperty(rs, currentWorker);
    }

    return new ArrayList<>(workerMap.values());
  }

  @SuppressWarnings("unused")
  private void addChildrenToProperty(ResultSet rs, Worker worker) throws SQLException {
    AuditDetails auditdetails = AuditDetails.builder().createdBy(rs.getString("createdBy"))
        .createdTime(rs.getLong("createdTime")).lastModifiedBy(rs.getString("lastModifiedBy"))
        .lastModifiedTime(rs.getLong("lastModifiedTime")).build();

    worker.setAuditDetails(auditdetails);
  }

  private JsonNode getAdditionalDetail(String columnName, ResultSet rs) {

    JsonNode additionalDetail = null;
    try {
      PGobject pgObj = (PGobject) rs.getObject(columnName);
      if (pgObj != null) {
        additionalDetail = mapper.readTree(pgObj.getValue());
      }
    } catch (IOException | SQLException e) {
      e.printStackTrace();
      throw new CustomException("PARSING_ERROR", "Failed to parse additionalDetail object");
    }
    return additionalDetail;
  }

}
