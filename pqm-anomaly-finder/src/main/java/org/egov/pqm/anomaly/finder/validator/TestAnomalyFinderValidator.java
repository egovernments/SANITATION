package org.egov.pqm.anomaly.finder.validator;

import java.util.List;
import java.util.stream.Collectors;

import org.egov.pqm.anomaly.finder.config.PqmAnomalyConfiguration;
import org.egov.pqm.anomaly.finder.repository.AnomalyRepository;
import org.egov.pqm.anomaly.finder.web.model.PqmAnomaly;
import org.egov.pqm.anomaly.finder.web.model.PqmAnomalySearchCriteria;
import org.egov.pqm.anomaly.finder.web.model.Test;
import org.egov.pqm.anomaly.finder.web.model.TestRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TestAnomalyFinderValidator {

	@Autowired
	public AnomalyRepository anomalyRepository;
	
	@Autowired
	private PqmAnomalyConfiguration config;
	
	public TestRequest validateCreate(TestRequest testRequest) {
		
		List<String> testIdLists = testRequest.getTests().stream().map(Test::getTestId).collect(Collectors.toList());
		List<Test> tests = testRequest.getTests();
		List<PqmAnomaly> pqmAnomalys = anomalyRepository.getAnomalyFinderData(testIdLists, testRequest.getTests().get(0).getTenantId());
		List<String> testIds = pqmAnomalys.stream().map(PqmAnomaly::getTestId).collect(Collectors.toList());

		List<Test> filterTests = tests.stream().filter(test -> !testIds.contains(test.getTestId())).collect(Collectors.toList());
		return TestRequest.builder().tests(filterTests).requestInfo(testRequest.getRequestInfo()).build();
	}
	
	public void validateSearchCriteria(PqmAnomalySearchCriteria criteria) {

		if (config.getIsEnvironmentCentralInstance() && criteria.getTenantId() == null)
			throw new CustomException("EG_PT_INVALID_SEARCH", " TenantId is mandatory for search ");

		else if (config.getIsEnvironmentCentralInstance()
				&& criteria.getTenantId().split("\\.").length < config.getStateLevelTenantIdLength())
			throw new CustomException("EG_PT_INVALID_SEARCH",
					" TenantId should be mandatorily " + config.getStateLevelTenantIdLength() + " levels for search");

	}
}
