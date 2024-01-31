package org.egov.tracking.data.dao;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracking.data.rowmapper.TripMapper;
import org.egov.tracking.util.DbUtil;
import org.openapitools.model.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

//VTS database access methods. Helps rune search, create and update actions
@Service
@Slf4j
public class TripDao {

    @Autowired
    DbUtil dbUtil;

    final String sqlFetchTripById = "SELECT id, operator, service_code, status, route_id, user_id, " +
            " planned_start_time, planned_end_time, actual_start_time, actual_end_time, tenant_id, reference_no, " +
            "trip_end_type " +
            " FROM Trip where id = ?";
    //Join multiple tables to fetch trip related information
    final String sqlFetchTripByFilters = "SELECT tr.id, tr.operator, tr.service_code, tr.status, tr.route_id, tr.user_id," +
            " tr.planned_start_time, tr.planned_end_time, tr.actual_start_time, tr.actual_end_time, tr.tenant_id, " +
            " tr.trip_end_type, tr.reference_no" +
            " FROM trip tr " +
            "where " +
            "tr.tenant_id = COALESCE(:tenantId, tr.tenant_id) and " +
            "tr.service_code = COALESCE(:serviceCode, tr.service_code) and " +
            "tr.reference_no = COALESCE(:referenceNos, tr.reference_no)  "
            ;
    final String sqlCreateTrip = "insert into Trip (id, operator, service_code, status, route_id, user_id, " +
            "planned_start_time, planned_end_time, actual_start_time, actual_end_time," +
            "created_date, created_by, updated_date, updated_by, tenant_id, reference_no) values (?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?,?)";
    final String sqlUpdateTrip = "update trip set route_id = COALESCE(:routeId, route_id), " +
            "status = COALESCE(:status, status), " +
            "actual_start_time = COALESCE(:actualStartTime, actual_start_time), " +
            "actual_end_time = COALESCE(:actualEndTime, actual_end_time), " +
            "trip_end_type = COALESCE(:tripEndType, trip_end_type), " +
            "updated_date = :updatedDate , updated_by = :updatedBy " +
            "where id = :tripId";

    private DataSource dataSource;

    //Datasource bean is injected
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Trip fetchTripbyId(String tripId) {
         log.info("## fetchTripbyId");
        JdbcTemplate jdbcTemplateObject = new JdbcTemplate(dataSource);
        Object[] args = new Object[]{tripId};
        List<Trip> tripList = jdbcTemplateObject.query(sqlFetchTripById, new TripMapper(), args);
        return (tripList.isEmpty())? null : tripList.get(0);

    }
    public List<Trip> fetchTripbyFilters(String status, String userId, String operatorId, String tenantId, String businessService, String referenceNos ) {
         log.info("## fetchTripbyFilters");

        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("tenantId", tenantId);
        params.put("serviceCode", businessService);
        params.put("referenceNos", referenceNos);

        List<Trip> tripList = namedParameterJdbcTemplate.query(sqlFetchTripByFilters, params, new TripMapper());
        return tripList;
    }
    //Create Trip and save it in database
    public String createTrip(Trip trip) {
         log.info("## createTrip in table");
        //TODO - Change to named parameters
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        //Prepare input data for the SQL
        //If trip id is already provided, use it. Otherwise create a new trip id
        //TODO - Handle duplicate record exception when called from some invalid situation
        String idLocal = trip.getId();
        if ( idLocal == null) {
            idLocal = dbUtil.getId();
        }
        OffsetDateTime offsetDateTime = OffsetDateTime.now();
        String currentDateString = offsetDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
        //Audit information
        String createdBy = trip.getUserId();
        String updatedBy = trip.getUserId();

        Object[] args = new Object[]{idLocal, trip.getOperator(), trip.getServiceCode(),
                trip.getStatus().toString(),
                trip.getRouteId(), createdBy, trip.getPlannedStartTime(), trip.getPlannedEndTime(), trip.getActualStartTime(),
                trip.getActualEndTime(), currentDateString,
                createdBy, currentDateString, updatedBy, trip.getTenantId(), trip.getReferenceNo()};

        int result = jdbcTemplate.update(sqlCreateTrip, args);
        if (result != 0) {
             log.info("Trip created with id " + idLocal);
            return idLocal;
        } else {
            log.error("Trip creation failed with id, locationName " + idLocal + " " + trip.getServiceCode());
            return null;
        }
    }

    //Update trip using an id and the supported list of attributes
    public String updateTrip(Trip trip) {
         log.info("## updateTrip inside DAO");

        OffsetDateTime offsetDateTime = OffsetDateTime.now();
        String currentDateString = offsetDateTime.format(DateTimeFormatter.ISO_DATE_TIME);

        String statusLocal = (trip.getStatus() != null) ? trip.getStatus().toString() : null;
        String startTime = (statusLocal.equals(Trip.StatusEnum.ONGOING.getValue())) ? currentDateString : null;
        String endTime = (statusLocal.equals(Trip.StatusEnum.COMPLETED.getValue())) ? currentDateString : null;

        //Audit information
        String updatedBy = trip.getUserId();

        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("routeId", trip.getRouteId());
        params.put("status", statusLocal);
        params.put("updatedDate", currentDateString);
        params.put("updatedBy", updatedBy);
        params.put("tripId", trip.getId());
        params.put("actualStartTime", startTime);
        params.put("actualEndTime", endTime);
        params.put("tripEndType", trip.getTripEndType());

        int result = namedParameterJdbcTemplate.update(sqlUpdateTrip, params);
        if (result != 0) {
             log.info("Trip updated with id " + trip.getId());
            return trip.getId();
        } else {
             log.error("Trip update failed with id " + trip.getId());
            return null;
        }
    }
}
