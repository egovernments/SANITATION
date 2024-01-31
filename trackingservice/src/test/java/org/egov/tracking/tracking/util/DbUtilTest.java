package org.egov.tracking.tracking.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.egov.tracking.util.DbUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openapitools.model.Location;

class DbUtilTest {

    DbUtil dbUtil = new DbUtil();
    @Test
    @Disabled
    void convertSpatialToLocationReturnPOINTLocations() throws Exception {
        //Set test data
        String spatialData = "POINT(78.302284 17.454218)";
        List<Location> locationList = dbUtil.convertSpatialToLocation(spatialData);
        assertEquals(1, locationList.size());
        assertEquals(78.302284f, locationList.get(0).getLatitude());
    }

    @Test
    @Disabled
    void convertSpatialToLocationReturnPOLYGONLocations() throws Exception {
        //Set test data
        String spatialData = "POLYGON ((78.302284 17.454218, 78.31177 17.470797, 78.309875 17.45864, 78.302284 17.454218))";
        List<Location> locationList = dbUtil.convertSpatialToLocation(spatialData);
        assertEquals(4, locationList.size());
        assertEquals(78.31177f, locationList.get(1).getLatitude());
    }
    @Test
    @Disabled
    void convertSpatialToLocationReturnLINESTRINGLocations() throws Exception {
        //Set test data
        String spatialData = "LINESTRING (78.302284 17.454218, 78.31177 17.470797)";
        List<Location> locationList = dbUtil.convertSpatialToLocation(spatialData);
        assertEquals(2, locationList.size());
        assertEquals(17.470797f, locationList.get(1).getLongitude());
    }

    @Test
    void testEpochToOffsetDate() {
        assertEquals(dbUtil.epochToDateFormat(Long.valueOf("1698741386671")), "2023-10-31T08:36:26.671Z");
    }
}
