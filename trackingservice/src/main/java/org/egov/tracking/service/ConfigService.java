package org.egov.tracking.service;

import java.util.List;
import org.egov.tracking.data.dao.ConfigDao;
import org.openapitools.model.LocationAlert;
import org.openapitools.model.ServiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigService {

    @Autowired
    ConfigDao configDao;
    public List<LocationAlert> getLocationAlerts() {
        //TODO - Mock list of alerts. Replace with database call
        return fetchAlertsFromDB();
    }

    public List<ServiceType> getServiceTypes() {
        //TODO - Mock list of alerts. Replace with database call
        return fetchServicesFromDB();
    }

    private List<LocationAlert> fetchAlertsFromDB() {
      return configDao.fetchAllLocationAlerts();
    }
    private List<ServiceType> fetchServicesFromDB() {

        //ConfigDao configDao = new ConfigDao();
      return configDao.fetchAllServiceTypes();
    }
}