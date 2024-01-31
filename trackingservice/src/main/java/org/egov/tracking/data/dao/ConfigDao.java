package org.egov.tracking.data.dao;

import java.util.List;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracking.data.rowmapper.LocationAlertMapper;
import org.egov.tracking.data.rowmapper.ServiceTypeMapper;
import org.openapitools.model.LocationAlert;
import org.openapitools.model.ServiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
@Service
@Slf4j
public class ConfigDao {
    static final String sqlFetchServiceTypes = "SELECT * FROM service_type";
    static final String sqlFetchLocationAlerts = "SELECT * FROM location_alert";

    private DataSource dataSource;

    //Datasource bean is injected
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public List<ServiceType> fetchAllServiceTypes() {
        log.info("## fetchAllServiceTypes");
        JdbcTemplate jdbcTemplateObject = new JdbcTemplate(dataSource);

      return jdbcTemplateObject.query(sqlFetchServiceTypes, new ServiceTypeMapper());
    }

    public List<LocationAlert> fetchAllLocationAlerts() {
        log.info("## fetchAllLocationAlerts");
        JdbcTemplate jdbcTemplateObject = new JdbcTemplate(dataSource);

      return jdbcTemplateObject.query(sqlFetchLocationAlerts, new LocationAlertMapper());
    }

}
