package org.egov.tracking.data.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.egov.tracking.util.DbUtil;
import org.openapitools.model.POI;
import org.springframework.jdbc.core.RowMapper;

public class POIMapper implements RowMapper<POI> {
    DbUtil dbUtil = new DbUtil();
    public POI mapRow(ResultSet rs, int rowNum) throws SQLException {
        POI poi = new POI();
        poi.setId(rs.getString("id"));
        poi.setType(POI.TypeEnum.valueOf(rs.getString("type").toUpperCase()));
        poi.setLocationName(rs.getString("location_name"));
        poi.setLocationDetails(dbUtil.getLocationDetailsFromSpatial(rs));
        poi.setAlert(rs.getString("alert"));
        poi.setStatus(POI.StatusEnum.valueOf(rs.getString("status").toUpperCase()));
        poi.setUserId(rs.getString("user_id"));
        poi.setTenantId(rs.getString("tenant_id"));
        poi.setDistanceMeters(rs.getInt("distance_meters"));
        return poi;
    }
}
