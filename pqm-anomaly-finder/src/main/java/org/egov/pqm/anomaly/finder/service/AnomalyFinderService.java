package org.egov.pqm.anomaly.finder.service;


import org.egov.pqm.anomaly.finder.repository.AnomalyRepository;
import org.egov.pqm.anomaly.finder.service.notification.NotificationService;
import org.egov.pqm.anomaly.finder.web.model.PqmAnomalyRequest;
import org.egov.pqm.anomaly.finder.web.model.TestRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnomalyFinderService {
	
	@Autowired
	private AnomalyRepository anomalyRepository;	

//	@Autowired
//	private NotificationService notificationService;
	
	@Autowired
	private EnrichmentService enrichmentService;
	
	public void anomalyCreate(TestRequest testRequest) {
	
		PqmAnomalyRequest pqmAnomalyRequest=enrichmentService.enrichPqmAnomalyCreateRequest(testRequest);
		anomalyRepository.save(pqmAnomalyRequest);
//		notificationService.process(testRequest);
	}
}
