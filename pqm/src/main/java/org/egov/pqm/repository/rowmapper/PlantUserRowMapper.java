package org.egov.pqm.repository.rowmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.egov.pqm.web.model.plant.user.PlantUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.egov.pqm.util.RowMapperUtil.getAdditionalDetail;
import static org.egov.pqm.util.RowMapperUtil.getAuditDetails;

@Component
@Slf4j
public class PlantUserRowMapper implements ResultSetExtractor<List<PlantUser>> {

    @Autowired
    private ObjectMapper mapper;

    @Getter
    @Setter
    public int totalCount = 0;


    @Override
    public List<PlantUser> extractData(ResultSet resultSet) throws SQLException {

        Map<String, PlantUser> plantUserMap = new LinkedHashMap<>();
        this.setTotalCount(0);
        while (resultSet.next()) {
            String id = resultSet.getString("id");

            if (plantUserMap.get(id) == null) {
                plantUserMap.put(id, PlantUser.builder()
                        .id(id).tenantId(resultSet.getString("tenantId"))
                        .plantCode(resultSet.getString("plantCode"))
                        .individualId(resultSet.getString("individualId"))
                        .isActive(resultSet.getBoolean("isActive"))
                        .additionalDetails(getAdditionalDetail("additionaldetails", resultSet, mapper))
                        .auditDetails(getAuditDetails(resultSet))
                        .build());
                this.setTotalCount(resultSet.getInt("total_count"));
            }
        }

        return new ArrayList<>(plantUserMap.values());
    }
}
