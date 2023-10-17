package org.egov.pqm.anomaly.finder.controller;

import javax.validation.Valid;

import org.egov.pqm.anomaly.finder.repository.AnomalyRepository;
import org.egov.pqm.anomaly.finder.web.model.TestRequest;
import org.egov.pqm.anomaly.finder.web.model.TestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class PqmAnomalyFinderController {

	@Autowired
	private AnomalyRepository anomalyRepository;

//	@Autowired
//	private ResponseInfoFactory responseInfoFactory;

	@PostMapping(value = "/_create")
	public ResponseEntity<TestResponse> create(@Valid @RequestBody TestRequest testRequest) {
//    Test test = pqmService.create(testRequest);
		anomalyRepository.save(testRequest);

//    List<Test> testList = new ArrayList<>();
//    testList.add(test);
    TestResponse response = null;
//    		TestResponse.builder().tests(testList)
//        .responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(testRequest.getRequestInfo(), true))
//        .build();
		return new ResponseEntity<>(response,HttpStatus.OK);
	}

}
