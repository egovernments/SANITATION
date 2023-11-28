package org.egov.pqm.validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.egov.pqm.config.ServiceConfiguration;
import org.egov.pqm.error.Error;
import org.egov.pqm.repository.PlantUserRepository;
import org.egov.pqm.service.UserService;
import org.egov.pqm.util.ErrorConstants;
import org.egov.pqm.util.PlantUserConstants;
import org.egov.pqm.web.model.plant.user.PlantUser;
import org.egov.pqm.web.model.plant.user.PlantUserRequest;
import org.egov.pqm.web.model.plant.user.PlantUserResponse;
import org.egov.pqm.web.model.plant.user.PlantUserSearchCriteria;
import org.egov.pqm.web.model.plant.user.PlantUserSearchRequest;
import org.egov.pqm.web.model.user.UserDetailResponse;
import org.egov.pqm.web.model.user.UserSearchRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class PlantUserValidator {

  @Autowired
  private MDMSValidator mdmsValidator;

  @Autowired
  private PlantUserRepository plantUserRepository;
  
  @Autowired
  private UserService userService;
  
  @Autowired
  private ServiceConfiguration config;

//  public void validateCreateRequest(PlantUserRequest plantUserRequest) {
//	  
//	  if (StringUtils.isEmpty(plantUserRequest.getPlantUsers().get(0).getTenantId())) {
//			throw new CustomException("PlantMappingConstants.INVALID_TENANT", "TenantId is mandatory");
//		}
//
//		if (plantUserRequest.getPlantUsers().get(0).getPlantOperatorUuid() == null
//				|| plantUserRequest.getPlantUsers().get(0).getPlantOperatorUuid().isEmpty()) {
//			throw new CustomException(PlantUserConstants.INVALID_UUID, "At lease one employee uuid is required");
//		}
//		if (plantUserRequest.getPlantUsers().get(0).getPlantCode() == null || plantUserRequest.getPlantUsers().get(0).getPlantCode().isEmpty()) {
//			throw new CustomException(PlantUserConstants.INVALID_PLANT_CODE, "Plant code is required");
//		}
//
//
//    plantUserRequest.getPlantUsers().forEach(
//        plantUser -> mdmsValidator.validateIfMasterPresent(plantUserRequest.getRequestInfo(),
//            plantUser.getTenantId(), "", plantUser.getPlantCode()));
//
//    UserDetailResponse userDetailResponse = userExists(plantUserRequest);
//
//	ArrayList<String> code = new ArrayList<>();
//	if (!userDetailResponse.getUser().isEmpty()) {
//		userDetailResponse.getUser().get(0).getRoles().forEach(role -> {
//			code.add("" + role.getCode());
//		});
//		if (!code.contains(PlantUserConstants.PQM_TP_OPERATOR)) {
//			throw new CustomException(ErrorConstants.INVALID_APPLICANT_ERROR,
//					"Only PQM_TP_OPERATOR Empoyee Can do this creation.");
//		}
//	} else {
//		throw new CustomException(ErrorConstants.PQM_TP_OPERATOR_EMPLOYEE_INVALID_ERROR,
//				"In PQM_TP_OPERATOR plant to employee mapping, employee doesn't exists");
//
//	}
//  }
  
  public void validateCreateRequest(PlantUserRequest plantUserRequest) {
	  List<PlantUser> plantUsers=plantUserRequest.getPlantUsers();
	    if (plantUsers == null || plantUsers.isEmpty()) {
	        throw new IllegalArgumentException("PlantUsers list cannot be null or empty");
	    }

	    for (PlantUser plantUser : plantUsers) {
	        if (StringUtils.isEmpty(plantUser.getTenantId())) {
	            throw new CustomException("PlantMappingConstants.INVALID_TENANT", "TenantId is mandatory");
	        }

	        if (plantUser.getPlantOperatorUuid() == null || plantUser.getPlantOperatorUuid().isEmpty()) {
	            throw new CustomException(PlantUserConstants.INVALID_UUID, "At least one employee uuid is required");
	        }

	        if (plantUser.getPlantCode() == null || plantUser.getPlantCode().isEmpty()) {
	            throw new CustomException(PlantUserConstants.INVALID_PLANT_CODE, "Plant code is required");
	        }

	        mdmsValidator.validateIfMasterPresent(plantUserRequest.getRequestInfo(),
	              plantUser.getTenantId(), "", plantUser.getPlantCode());

	        UserDetailResponse userDetailResponse = userExists(plantUserRequest);

	        List<String> code = new ArrayList<>();
	        if (!userDetailResponse.getUser().isEmpty()) {
	            userDetailResponse.getUser().get(0).getRoles().forEach(role -> {
	                code.add("" + role.getCode());
	            });
	            if (!code.contains(PlantUserConstants.PQM_TP_OPERATOR)) {
	                throw new CustomException(ErrorConstants.INVALID_APPLICANT_ERROR,
	                        "Only PQM_TP_OPERATOR Employee Can do this creation.");
	            }
	        } else {
	            throw new CustomException(ErrorConstants.PQM_TP_OPERATOR_EMPLOYEE_INVALID_ERROR,
	                    "In PQM_TP_OPERATOR plant-to-employee mapping, employee doesn't exist");
	        }
	    }
	}


	public void validatePlantMappingExists(PlantUserRequest plantUserRequest) {
		List<PlantUser> plantUsers = plantUserRequest.getPlantUsers();
		if (plantUsers == null || plantUsers.isEmpty()) {
			throw new IllegalArgumentException("PlantUsers list cannot be null or empty");
		}

		List<String> plantOperatorUuids = new ArrayList<>();
		List<String> plantCodes = new ArrayList<>();
		String tenantId = null;

		// Assuming all PlantUser objects in the list have the same tenantId
		tenantId = plantUsers.get(0).getTenantId();

		for (PlantUser plantUser : plantUsers) {
			plantOperatorUuids.add(plantUser.getPlantOperatorUuid());
			plantCodes.add(plantUser.getPlantCode());
		}

		PlantUserSearchCriteria plantUserSearchCriteria = new PlantUserSearchCriteria();
		plantUserSearchCriteria.setPlantOperatorUuids(plantOperatorUuids);
		plantUserSearchCriteria.setPlantCodes(plantCodes);
		plantUserSearchCriteria.setTenantId(tenantId);

		PlantUserResponse plantUserResponse = plantUserRepository
				.search(PlantUserSearchRequest.builder().plantUserSearchCriteria(plantUserSearchCriteria).build());

//		if (plantUserResponse != null && plantUserResponse.getPlantUsers() != null
//				&& !plantUserResponse.getPlantUsers().isEmpty()
//				&& StringUtils.isNotBlank(plantUserResponse.getPlantUsers().get(0).getId())) {
//			throw new CustomException(ErrorConstants.PLANT_EMPLOYEE_MAP_EXISTS_ERROR,
//					"Plant and employee mapping already exist.");
//		}
		if (plantUserResponse != null && plantUserResponse.getPlantUsers() != null) {
		    for (PlantUser plantUser : plantUserResponse.getPlantUsers()) {
		        if (StringUtils.isNotBlank(plantUser.getId())) {
		            throw new CustomException(ErrorConstants.PLANT_EMPLOYEE_MAP_EXISTS_ERROR,
		                    "Plant and employee mapping already exist for PlantUser with ID: " + plantUser.getId());
		        }
		    }
		}

	}
  public void validateUpdateRequest(PlantUserRequest plantUserRequest){
    /*if (Objects.isNull(plantUserRequest.getPlantUsers()) || CollectionUtils.isEmpty(
        plantUserRequest.getPlantUsers())) {
      throw Error.mandatory_field_missing.getBuilder(PlantUserRequest.class.getName(),
          PlantUser.class.getName()).build();
    }

    plantUserRequest.getPlantUsers().forEach(
        plantUser -> mdmsValidator.validateIfMasterPresent(plantUserRequest.getRequestInfo(),
            plantUser.getTenantId(), "", plantUser.getPlantCode()));*/
  }
  
//  private UserDetailResponse userExists(PlantUserRequest plantUserRequest) {
//		UserSearchRequest userSearchRequest = new UserSearchRequest();
//		List<String> uuid = new ArrayList<>();
//		uuid.add(plantUserRequest.getPlantUsers().get(0).getPlantOperatorUuid());
//		userSearchRequest.setUuid(uuid);
//
//		StringBuilder uri = new StringBuilder(config.getUserHost()).append(config.getUserSearchEndpoint());
//		return userService.userCall(userSearchRequest, uri);
//	}
  public UserDetailResponse userExists(PlantUserRequest plantUserRequest) {
	  List<PlantUser> plantUsers=plantUserRequest.getPlantUsers();
	    if (plantUsers == null || plantUsers.isEmpty()) {
	        throw new IllegalArgumentException("PlantUsers list cannot be null or empty");
	    }

	    UserSearchRequest userSearchRequest = new UserSearchRequest();
	    List<String> uuids = new ArrayList<>();

	    for (PlantUser plantUser : plantUsers) {
	        if (plantUser.getPlantOperatorUuid() != null && !plantUser.getPlantOperatorUuid().isEmpty()) {
	            uuids.add(plantUser.getPlantOperatorUuid());
	        }
	    }

	    if (uuids.isEmpty()) {
	        // No valid UUIDs found in the list
	        throw new CustomException("INVALID_UUID", "At least one valid employee uuid is required");
	    }

	    userSearchRequest.setUuid(uuids);

	    StringBuilder uri = new StringBuilder(config.getUserHost()).append(config.getUserSearchEndpoint());
	    return userService.userCall(userSearchRequest, uri);
	}

}
