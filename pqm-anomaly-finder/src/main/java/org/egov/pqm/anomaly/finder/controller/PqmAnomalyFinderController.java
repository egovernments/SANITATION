package org.egov.pqm.anomaly.finder.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.pqm.anomaly.finder.repository.AnomalyRepository;
import org.egov.pqm.anomaly.finder.service.AnomalyFinderService;
import org.egov.pqm.anomaly.finder.util.ResponseInfoFactory;
import org.egov.pqm.anomaly.finder.web.model.PqmAnomaly;
import org.egov.pqm.anomaly.finder.web.model.PqmAnomalyResponse;
import org.egov.pqm.anomaly.finder.web.model.PqmAnomalySearchCriteria;
import org.egov.pqm.anomaly.finder.web.model.PqmAnomalySearchRequest;
import org.egov.pqm.anomaly.finder.web.model.RequestInfoWrapper;
import org.egov.pqm.anomaly.finder.web.model.TestRequest;
import org.egov.pqm.anomaly.finder.web.model.TestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class PqmAnomalyFinderController {

	@Autowired
	private AnomalyRepository anomalyRepository;
	
	@Autowired
	private AnomalyFinderService anomalyFinderService;
	
	@Autowired
	private ResponseInfoFactory responseInfoFactory;
	/**
	 * Created this api for testing purpose 
	 * Will delete once we will complete testing.
	 * @param testRequest
	 *  
	 */

	@PostMapping(value = "/_create")
	public ResponseEntity<TestResponse> create(@Valid @RequestBody TestRequest testRequest) {

		anomalyRepository.save(testRequest);

		TestResponse response = null;
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping(value = "/_plainsearch")
	public ResponseEntity<PqmAnomalyResponse> plainsearch(@Valid @RequestBody RequestInfoWrapper requestInfoWrapper,
			@Valid @ModelAttribute PqmAnomalySearchCriteria criteria) {
		List<PqmAnomaly> pqmAnomalyList = anomalyFinderService.pqmAnomalyPlainSearch(criteria, requestInfoWrapper.getRequestInfo());
		PqmAnomalyResponse response = PqmAnomalyResponse.builder().pqmAnomalys(pqmAnomalyList).responseInfo(
				responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true))
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping(value = "/_search")
	public ResponseEntity<PqmAnomalyResponse> search(@Valid @RequestBody PqmAnomalySearchRequest pqmAnomalySearchRequest) {
		List<PqmAnomaly> pqmAnomalyList = anomalyFinderService.anomalySearch(pqmAnomalySearchRequest.getPqmAnomalySearchCriteria());
		PqmAnomalyResponse response = PqmAnomalyResponse.builder().pqmAnomalys(pqmAnomalyList).responseInfo(
						responseInfoFactory.createResponseInfoFromRequestInfo(pqmAnomalySearchRequest.getRequestInfo(), true))
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
