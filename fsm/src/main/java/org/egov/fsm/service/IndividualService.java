package org.egov.fsm.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.models.core.Role;
import org.egov.common.models.individual.Identifier;
import org.egov.common.models.individual.Individual;
import org.egov.common.models.individual.IndividualBulkResponse;
import org.egov.common.models.individual.IndividualRequest;
import org.egov.common.models.individual.IndividualResponse;
import org.egov.common.models.individual.IndividualSearch;
import org.egov.common.models.individual.IndividualSearchRequest;
import org.egov.common.models.individual.Name;
import org.egov.common.models.individual.UserDetails;
import org.egov.fsm.config.FSMConfiguration;
import org.egov.fsm.repository.ServiceRequestRepository;
import org.egov.fsm.util.FSMErrorConstants;
import org.egov.fsm.web.model.garima.SanitationWorker;
import org.egov.fsm.web.model.worker.WorkerType;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IndividualService {

//	private static final UserType CITIZEN = null;

	@Autowired
	private FSMConfiguration config;

	private ServiceRequestRepository serviceRequestRepository;

	private ObjectMapper mapper;

	@Autowired
	public IndividualService(FSMConfiguration config, ServiceRequestRepository serviceRequestRepository,
			ObjectMapper mapper) {
		this.config = config;
		this.serviceRequestRepository = serviceRequestRepository;
		this.mapper = mapper;
	}

	/**
	 * create Individual for Worker using the Individual api
	 * 
	 *
	 * @param  worker
	 * @return
	 */
	public IndividualResponse create(SanitationWorker sanitationWorker, RequestInfo requestInfo) {

		List<Identifier> identifiers = new ArrayList<Identifier>();

		Identifier identifier = Identifier.builder().identifierId(sanitationWorker.getGarima_id())
				.identifierType("GarimaID").build();

		identifiers.add(identifier);
//				User user
		List<Role> roles = new ArrayList<Role>();
		Role role = new Role();

		WorkerType workerType = sanitationWorker.getWorkerType();
		if (workerType != null) {
			if (workerType.equals(workerType.HELPER)) {
				role.setCode("SANITATION_HELPER");
				role.setTenantId(requestInfo.getUserInfo().getTenantId());
			} else if (workerType.equals(workerType.DRIVER)) {
				role.setCode("FSM_DRIVER");
				role.setTenantId(requestInfo.getUserInfo().getTenantId());
			} else {
				throw new IllegalArgumentException("Invalid workerType: " + workerType);
			}
		} else {
			throw new NullPointerException("WorkerType should not be null");
		}

		roles.add(role);
		role.setCode("CITIZEN");
		role.setTenantId(requestInfo.getUserInfo().getTenantId());
		roles.add(role);
		role.setCode("SANITATION_WORKER");
		role.setTenantId(requestInfo.getUserInfo().getTenantId());
		roles.add(role);
		UserDetails userDetails = UserDetails.builder().tenantId(requestInfo.getUserInfo().getTenantId()).roles(roles)
				.build();
		Name name = Name.builder().givenName(sanitationWorker.getName()).build();
		Individual individual = Individual.builder().isSystemUser(true).isSystemUserActive(true)
				.tenantId(requestInfo.getUserInfo().getTenantId()).identifiers(identifiers).userDetails(userDetails)
				.name(name).build();

		StringBuilder url = new StringBuilder(config.getIndividualHost());

		url.append(config.getIndividualContextPath()).append(config.getIndividualCreatePath());

		IndividualRequest request = IndividualRequest.builder().requestInfo(requestInfo).individual(individual).build();

		Object result = serviceRequestRepository.fetchResult(url, request);
		IndividualResponse response = null;
		try {
			response = mapper.convertValue(result, IndividualResponse.class);
		} catch (IllegalArgumentException e) {
			throw new CustomException(FSMErrorConstants.PARSING_ERROR, "Failed to parse response of Workflow");
		}
		return response;

	}

	/**
	 * search Individual using the individual api
	 *
	 * @param fsmRequest
	 * @return
	 */
	public IndividualBulkResponse search(SanitationWorker sanitationWorker,RequestInfo reguestInfo) {// Construct the base URL
		StringBuilder url = new StringBuilder(config.getIndividualHost());
		url.append(config.getIndividualContextPath()).append(config.getIndividualSearchPath()).append("?tenantId=")
				.append(reguestInfo.getUserInfo().getTenantId()).append("&limit=").append("10").append("&offset=")
				.append("0");

		// Create a list of identifiers
//		List<Identifier> identifiers = new ArrayList<>();
		Identifier identifier = new Identifier();
		identifier.setIdentifierType("GarimaId");
		identifier.setIdentifierId(sanitationWorker.getGarima_id());
//		identifiers.add(identifier);

		// Create an Individual object and set its identifiers
		 IndividualSearchRequest request = IndividualSearchRequest.builder()
	                .requestInfo(reguestInfo)
	                .individual(IndividualSearch.builder().identifier(identifier)
	                .build())
	                .build();
		// Make the service request
		// Assuming serviceRequestRepository.fetchResult(url, individual) is your method to make the service call
		Object result = serviceRequestRepository.fetchResult(url, request);

		IndividualBulkResponse response = null;
		try {
			response = mapper.convertValue(result, IndividualBulkResponse.class);
		} catch (IllegalArgumentException e) {
			throw new CustomException(FSMErrorConstants.PARSING_ERROR, "Failed to parse response of Workflow");
		}
		return response;
	}

}
