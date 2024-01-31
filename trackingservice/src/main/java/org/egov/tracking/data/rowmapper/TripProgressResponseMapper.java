package org.egov.tracking.data.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.egov.tracking.util.DbUtil;
import org.openapitools.model.TripProgressResponse;
import org.springframework.jdbc.core.RowMapper;

public class TripProgressResponseMapper implements RowMapper<TripProgressResponse> {
    DbUtil dbUtil = new DbUtil();
    public TripProgressResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        TripProgressResponse tripProgress = new TripProgressResponse();
        tripProgress.setId(rs.getString("id"));
        tripProgress.setTripId(rs.getString("trip_id"));
        tripProgress.setProgressReportedTime(rs.getString("progress_reported_time"));
        tripProgress.setProgressTime(rs.getString("progress_time"));
        tripProgress.setLocation(dbUtil.convertSpatialSinglePointToLocation(rs.getString("position_point")));
        return tripProgress;
    }
}
