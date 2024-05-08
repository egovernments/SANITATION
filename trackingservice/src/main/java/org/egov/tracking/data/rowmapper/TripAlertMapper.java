package org.egov.tracking.data.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.egov.tracking.data.model.TripAlert;
import org.egov.tracking.util.DbUtil;
import org.springframework.jdbc.core.RowMapper;

public class TripAlertMapper implements RowMapper<TripAlert> {
    DbUtil dbUtil = new DbUtil();
    public TripAlert mapRow(ResultSet rs, int rowNum) throws SQLException {
        TripAlert tripAlert = new TripAlert();
        tripAlert.setId(rs.getString("id"));
        tripAlert.setTripId(rs.getString("trip_id"));
        tripAlert.setTripProgressId(rs.getString("trip_progress_id"));
        tripAlert.setAlert(rs.getString("alert"));
        tripAlert.setApplicationNo(rs.getString("application_no"));
        tripAlert.setAlertDateTime(rs.getString("alert_date_time"));
        return tripAlert;
    }
}
