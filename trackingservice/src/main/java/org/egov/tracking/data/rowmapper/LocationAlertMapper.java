package org.egov.tracking.data.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.openapitools.model.LocationAlert;
import org.springframework.jdbc.core.RowMapper;

public class LocationAlertMapper implements RowMapper<LocationAlert> {
    public LocationAlert mapRow(ResultSet rs, int rowNum) throws SQLException {
        LocationAlert locationAlert = new LocationAlert();
        locationAlert.setCode(rs.getString("code"));
        locationAlert.setTitle(rs.getString("title"));
        return locationAlert;
    }

}
