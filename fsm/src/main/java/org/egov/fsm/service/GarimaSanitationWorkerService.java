package org.egov.fsm.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.fsm.config.FSMConfiguration;
import org.egov.fsm.repository.ServiceRequestRepository;
import org.egov.fsm.util.FSMErrorConstants;
import org.egov.fsm.web.model.garima.*;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class GarimaSanitationWorkerService {

    @Autowired
    private FSMConfiguration config;

    private ServiceRequestRepository serviceRequestRepository;

    private ObjectMapper mapper;

    @Autowired
    public GarimaSanitationWorkerService(FSMConfiguration config, ServiceRequestRepository serviceRequestRepository,
                                         ObjectMapper mapper) {
        this.config = config;
        this.serviceRequestRepository = serviceRequestRepository;
        this.mapper = mapper;
    }

    /**
     * create Sanitation Worker using the Garima api
     * This api is an adaptor/bridge for our service and Garima api
     *
     * @param sanitationWorkerCreateRequest
     * @return
     */
    public SanitationWorkerResponse create(SanitationWorkerCreateRequest sanitationWorkerCreateRequest) {
        SanitationWorker sanitationWorker = sanitationWorkerCreateRequest.getSanitationWorker();

        StringBuilder url = new StringBuilder(config.getGarimaHost());

        url.append(config.getGarimaContextPath()).append(config.getGarimaCreatePath());

        Object result = serviceRequestRepository.fetchGarimaResult(url, sanitationWorker);
        SanitationWorkerResponse response = null;
        try {
            response = mapper.convertValue(result, SanitationWorkerResponse.class);
        } catch (IllegalArgumentException e) {
            throw new CustomException(FSMErrorConstants.PARSING_ERROR, "Failed to parse response of Workflow");
        }
        return response;

    }


    /**
     * search Sanitation Worker using the Garima api
     * This api is an adaptor/bridge for our service and Garima api
     *
     * @param sanitationWorkerSearchCriteria
     * @return
     */
    public SanitationWorkerSearchResponse search(SanitationWorkerSearchCriteria sanitationWorkerSearchCriteria) {
        StringBuilder url = new StringBuilder(config.getGarimaHost());

        url.append(config.getGarimaContextPath()).append(config.getGarimaSearchPath());
        if (!StringUtils.isEmpty(sanitationWorkerSearchCriteria.getGarimaId())) {
            url.append("?garima_id=");
            url.append(sanitationWorkerSearchCriteria.getGarimaId());
        }

        Object result = serviceRequestRepository.fetchGarimaSearchResult(url, null);
        SanitationWorkerSearchResponse response = null;
        try {
            response = mapper.convertValue(result, SanitationWorkerSearchResponse.class);
        } catch (IllegalArgumentException e) {
            throw new CustomException(FSMErrorConstants.PARSING_ERROR, "Failed to parse response of Garima");
        }
        return response;
    }

}
