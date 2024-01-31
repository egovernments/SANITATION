package org.egov.tracking.service;

import java.util.List;
import org.egov.tracking.data.dao.RouteDao;
import org.openapitools.model.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RouteService {

    @Autowired
    RouteDao routeDao;
    public Route getRoutesById(String routeId) {
        return routeDao.fetchRoutebyId(routeId);
    }

    public String createRoute(Route route) {
        return routeDao.createRoute(route);
    }

    public List<Route> getRoutesBySearch(String name, String userId) {
        return routeDao.fetchRoutebyFilters(name, userId);
    }

}

