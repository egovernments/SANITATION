package org.egov.pqm.service;

import java.util.LinkedList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pqm.web.model.Test;
import org.egov.pqm.web.model.TestResponse;
import org.egov.pqm.web.model.TestSearchRequest;
import org.springframework.stereotype.Service;

@Service	
public class PqmService {

	public TestResponse pqmSearch(TestSearchRequest criteria, RequestInfo requestInfo) {

		List<Test> pqmList = new LinkedList<>();
		TestResponse pqmResponse = null;

		return pqmResponse;
	}

}
