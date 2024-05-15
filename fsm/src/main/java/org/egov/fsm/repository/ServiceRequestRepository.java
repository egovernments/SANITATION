	package org.egov.fsm.repository;

import java.util.Map;
import java.util.Optional;

import org.egov.fsm.config.FSMConfiguration;
import org.egov.fsm.web.model.garima.SanitationWorkerSearchResponse;
import org.egov.tracer.model.CustomException;
import org.egov.tracer.model.ServiceCallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;

import static org.egov.fsm.util.FSMConstants.X_API_Key;
import static org.egov.fsm.util.FSMErrorConstants.INVALID_API_KEY;
import static org.egov.fsm.util.FSMErrorConstants.INVALID_API_KEY_MESSAGE;

@Repository
@Slf4j
public class ServiceRequestRepository {
	private ObjectMapper mapper;

	private RestTemplate restTemplate;

	@Autowired
	private FSMConfiguration config;

	@Autowired
	public ServiceRequestRepository(ObjectMapper mapper, RestTemplate restTemplate) {
		this.mapper = mapper;
		this.restTemplate = restTemplate;
	}
	/**
	 * fetchResult form the different services based on the url and request object
	 * @param uri
	 * @param request
	 * @return
	 */
	public Object fetchResult(StringBuilder uri, Object request) {
//		added for garima
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<Object> requestEntity = new HttpEntity<>(request, headers);
//        till here 
		Object response = null;
		try {
			response = restTemplate.postForObject(uri.toString(), requestEntity, Map.class);
		} catch (HttpClientErrorException e) {
			log.error("External Service threw an Exception: ", e);
			throw new ServiceCallException(e.getResponseBodyAsString());
		} catch (Exception e) {
			log.error("Exception while fetching from searcher: ", e);
			throw new ServiceCallException(e.getMessage());
		}

		return response;
	}
	/**
	 * Fetches results from a REST service using the uri and object
	 * 
	 * @return Object
	 * @author vishal
	 */
	public Optional<Object> fetchApiResult(StringBuilder uri, Object request) {

		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		Object response = null;
		log.info("URI: "+uri.toString());
		try {
			log.info("Request: "+mapper.writeValueAsString(request));
			response = restTemplate.postForObject(uri.toString(), request, Map.class);
		} catch (HttpClientErrorException e) {
			
			log.error("External Service threw an Exception: ", e);
			throw new ServiceCallException(e.getResponseBodyAsString());
		} catch (Exception e) {
			
			log.error("Exception while fetching from external service: ", e);
			throw new CustomException("REST_CALL_EXCEPTION : "+uri.toString(),e.getMessage());
		}
		return Optional.ofNullable(response);
	}

    /**
     * fetchResult form the Garima services based on the url and request object
     *
     * @param uri
     * @param request
     * @return
     */
    public Object fetchGarimaResult(StringBuilder uri, Object request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (!StringUtils.isEmpty(config.getGarimaApiKey()))
            headers.set(X_API_Key, config.getGarimaApiKey());
        else
            throw new CustomException(INVALID_API_KEY, INVALID_API_KEY_MESSAGE);

        HttpEntity<Object> requestEntity = new HttpEntity<>(request, headers);

        Object response = null;
        try {
            response = restTemplate.postForObject(uri.toString(), requestEntity, Map.class);

        } catch (HttpClientErrorException e) {
            log.error("External Service threw an Exception: ", e);
            throw new ServiceCallException(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Exception while fetching from searcher: ", e);
            throw new ServiceCallException(e.getMessage());
        }

        return response;
    }

    /**
     * fetchResult form the Garima services based on the url and request object
     *
     * @param uri
     * @param request
     * @return
     */
    public Object fetchGarimaSearchResult(StringBuilder uri, Object request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (!StringUtils.isEmpty(config.getGarimaApiKey()))
            headers.set(X_API_Key, config.getGarimaApiKey());
        else
            throw new CustomException(INVALID_API_KEY, INVALID_API_KEY_MESSAGE);

        HttpEntity<Object> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<SanitationWorkerSearchResponse> responseEntity;
        try {
            responseEntity = restTemplate.exchange(uri.toString(),
                    HttpMethod.GET, requestEntity,
                    SanitationWorkerSearchResponse.class);
        } catch (HttpClientErrorException e) {
            log.error("External Service threw an Exception: ", e);
            throw new ServiceCallException(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Exception while fetching from searcher: ", e);
            throw new ServiceCallException(e.getMessage());
        }

        return responseEntity.getBody();
    }

}
