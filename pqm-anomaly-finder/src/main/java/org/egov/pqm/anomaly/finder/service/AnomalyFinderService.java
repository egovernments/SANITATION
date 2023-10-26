package org.egov.pqm.anomaly.finder.service;

import org.egov.pqm.anomaly.finder.repository.AnomalyRepository;
import org.egov.pqm.anomaly.finder.service.notification.NotificationService;
import org.egov.pqm.anomaly.finder.validator.TestAnomalyFinderValidator;
import org.egov.pqm.anomaly.finder.web.model.PqmAnomalyRequest;
import org.egov.pqm.anomaly.finder.web.model.TestRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnomalyFinderService {

	@Autowired
	private AnomalyRepository anomalyRepository;

	@Autowired
	private TestAnomalyFinderValidator testAnomalyFinderValidator;

	@Autowired
	private EnrichmentService enrichmentService;
	
	@Autowired
	private NotificationService notificationService;

	public void anomalyCreate(TestRequest testRequest) {

		TestRequest testRequests = testAnomalyFinderValidator.validateCreate(testRequest);
		PqmAnomalyRequest pqmAnomalyRequest = enrichmentService.enrichPqmAnomalyCreateRequest(testRequests);
		anomalyRepository.save(pqmAnomalyRequest);
		notificationService.process(testRequests);
	}
}
