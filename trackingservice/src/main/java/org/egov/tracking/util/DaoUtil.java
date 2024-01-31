package org.egov.tracking.util;

import org.openapitools.model.Location;
import org.openapitools.model.POI;

public class DaoUtil {
    //Create a geometry position compatible with PostgresSQL PostGIS extension
    public static String getGeometryPositionPostgresSQL(POI poi) {
        String geomPosition = "";
        if (poi.getType() == POI.TypeEnum.POINT) {
            //In case of a POINT, only one LatLong pair will be sent by client app in locationDetails
            geomPosition = "POINT(" + poi.getLocationDetails().get(0).getLongitude() + " " + poi.getLocationDetails().get(0).getLatitude() + ")";
        } else if (poi.getType() == POI.TypeEnum.POLYGON) {
            StringBuilder polyBuffStr = new StringBuilder();
            int indx = 1;
            for (Location location : poi.getLocationDetails()) {
                polyBuffStr.append(location.getLongitude()).append(" ").append(location.getLatitude());
                //Avoid a comma after the last element in concatenated list
                if (indx < poi.getLocationDetails().size()) {
                    indx++;
                    polyBuffStr.append(" , ");
                }
            }
            geomPosition = "POLYGON((" + polyBuffStr + "))";
        } else {
            StringBuilder polyBuffStr = new StringBuilder();
            int indx = 1;
            for (Location location : poi.getLocationDetails()) {
                polyBuffStr.append(location.getLongitude()).append(" ").append(location.getLatitude());
                //Avoid a comma after the last element in concatenated list
                if (indx < poi.getLocationDetails().size()) {
                    indx++;
                    polyBuffStr.append(" , ");
                }
            }
            geomPosition = "LINESTRING(" + polyBuffStr + ")";
        }
        return geomPosition;
    }
}
