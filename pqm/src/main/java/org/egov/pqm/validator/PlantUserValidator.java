package org.egov.pqm.validator;

import java.util.Objects;
import org.egov.pqm.error.Error;
import org.egov.pqm.repository.PlantUserRepository;
import org.egov.pqm.web.model.plant.user.PlantUser;
import org.egov.pqm.web.model.plant.user.PlantUserRequest;
import org.egov.pqm.web.model.plant.user.PlantUserSearchCriteria;
import org.egov.pqm.web.model.plant.user.PlantUserSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class PlantUserValidator {

  @Autowired
  private MDMSValidator mdmsValidator;

  @Autowired
  private PlantUserRepository plantUserRepository;

  public void validateCreateRequest(PlantUserRequest plantUserRequest) {
    if (Objects.isNull(plantUserRequest.getPlantUsers()) || CollectionUtils.isEmpty(
        plantUserRequest.getPlantUsers())) {
      plantUserRepository.search(PlantUserSearchRequest.builder().plantUserSearchCriteria(
          PlantUserSearchCriteria.builder().build()).build());
      throw Error.mandatory_field_missing.getBuilder(PlantUserRequest.class.getName(),
          PlantUser.class.getName()).build();
    }

    plantUserRequest.getPlantUsers().forEach(
        plantUser -> mdmsValidator.validateIfMasterPresent(plantUserRequest.getRequestInfo(),
            plantUser.getTenantId(), "", plantUser.getPlantCode()));

  }

  public void validateUpdateRequest(PlantUserRequest plantUserRequest){
    if (Objects.isNull(plantUserRequest.getPlantUsers()) || CollectionUtils.isEmpty(
        plantUserRequest.getPlantUsers())) {
      throw Error.mandatory_field_missing.getBuilder(PlantUserRequest.class.getName(),
          PlantUser.class.getName()).build();
    }

    plantUserRequest.getPlantUsers().forEach(
        plantUser -> mdmsValidator.validateIfMasterPresent(plantUserRequest.getRequestInfo(),
            plantUser.getTenantId(), "", plantUser.getPlantCode()));
  }

}
