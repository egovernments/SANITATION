package org.egov.tracking.data.dao;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracking.data.rowmapper.RouteMapper;
import org.egov.tracking.util.DbUtil;
import org.egov.tracking.util.JsonUtil;
import org.openapitools.model.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RouteDao {

    @Autowired
    DbUtil dbUtil;
    final String sqlFetchRouteById = "SELECT id, start_poi, end_poi, name, status, intermediate_pois, " +
            "user_id FROM route where id = ?";
    final String sqlFetchRouteByFilters = "SELECT id, start_poi, end_poi, name, status, intermediate_pois, user_id " +
            "FROM route " +
            "where " +
            "user_id = COALESCE(:userId, user_id) " +
            "and name like COALESCE(:name, name) " +
            ";";

    final String sqlCreateRoute = "insert into route (id, start_poi, end_poi, name, status,  " +
            "user_id, created_date, created_by, updated_date, updated_by) values (?,?,?,?, ?,?,?,?,?,?)";
    private DataSource dataSource;

    //Datasource bean is injected
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Route fetchRoutebyId(String routeId) {
         log.info("## fetchRoutebyId");
        JdbcTemplate jdbcTemplateObject = new JdbcTemplate(dataSource);
        Object[] args = new Object[]{routeId};
        List<Route> routeList = jdbcTemplateObject.query(sqlFetchRouteById, new RouteMapper(), args);
        return (routeList.isEmpty())? null : routeList.get(0);
    }

    //Search based on user id or route name or a combination of both
    public List<Route> fetchRoutebyFilters(String name, String userId) {
         log.info("## fetchRoutebyFilters");
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("userId", userId);
        //Partial search is supported for route name
        if (name != null) {
            params.put("name", "%"+name+"%");
        }else{
            params.put("name", null);
        }
        List<Route> routeList = namedParameterJdbcTemplate.query(sqlFetchRouteByFilters, params, new RouteMapper());
        return routeList;
    }
    //Create Route and save it in database
    public String createRoute(Route route) {
         log.info("## createRoute");
        //TODO - Change to named jdbc template
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        //Prepare input data for the SQL
        String idLocal = dbUtil.getId();

        //TODO - Intermediate pois are not added currently due to Postres handling issues. Need to fix this. No use case currently
        String intermediatePois = JsonUtil.getJsonFromObject(route.getIntermediatePois());

        OffsetDateTime offsetDateTime = OffsetDateTime.now();
        String currentDateString = offsetDateTime.format(DateTimeFormatter.ISO_DATE_TIME);

        //Audit information
        String createdBy = route.getUserId();
        String updatedBy = route.getUserId();

        Object[] args = new Object[]{idLocal, route.getStartPoi(), route.getEndPoi(), route.getName(), route.getStatus().toString(),
                route.getUserId(), currentDateString,
                createdBy, currentDateString, updatedBy};

        int result = jdbcTemplate.update(sqlCreateRoute, args);
        if (result != 0) {
             log.info("Route created with id " + idLocal);
            return idLocal;
        } else {
             log.error("Route creation failed with id, locationName " + idLocal + " " + route.getName());
            return null;
        }
    }
}
