package org.egov.pqm.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.pqm.repository.PlantUserRepository;
import org.egov.pqm.validator.PlantUserValidator;
import org.egov.pqm.web.model.plant.user.PlantUser;
import org.egov.pqm.web.model.plant.user.PlantUserRequest;
import org.egov.pqm.web.model.plant.user.PlantUserResponse;
import org.egov.pqm.web.model.plant.user.PlantUserSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PlantUserService {

    @Autowired
    private PlantUserRepository plantUserRepository;

    @Autowired
    private PlantUserValidator plantUserValidator;

    @Autowired
    private EnrichmentService enrichmentService;

    public List<PlantUser> create(PlantUserRequest plantUserRequest) {
        plantUserValidator.validateCreateRequest(plantUserRequest);
        enrichmentService.enrichCreatePlanUserRequest(plantUserRequest);
        return plantUserRepository.save(plantUserRequest);
    }

    public List<PlantUser> update(PlantUserRequest plantUserRequest) {
        enrichmentService.enrichUpdatePlanUserRequest(plantUserRequest);
        return plantUserRepository.update(plantUserRequest);
    }

    public PlantUserResponse search(PlantUserSearchRequest plantUserSearchRequest) {
        return plantUserRepository.search(plantUserSearchRequest);
    }
}
