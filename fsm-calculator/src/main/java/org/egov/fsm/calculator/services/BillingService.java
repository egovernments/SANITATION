package org.egov.fsm.calculator.services;

import org.egov.common.contract.request.RequestInfo;
import org.egov.fsm.calculator.config.CalculatorConfig;
import org.egov.fsm.calculator.repository.ServiceRequestRepository;
import org.egov.fsm.calculator.utils.CalculatorConstants;
import org.egov.fsm.calculator.web.models.BillResponse;
import org.egov.fsm.calculator.web.models.FSM;
import org.egov.fsm.calculator.web.models.RequestInfoWrapper;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BillingService {

	@Autowired
	private CalculatorConfig config;

	@Autowired
	private ServiceRequestRepository serviceRequestRepository;

	@Autowired
	private ObjectMapper mapper;

	public BillResponse fetchBill(FSM fsmRequest, RequestInfo requestInfo) {

		StringBuilder uri = new StringBuilder(config.getBillingHost());
		uri.append(config.getFetchBillEndpoint());
		uri.append("?").append("tenantId=").append(fsmRequest.getTenantId());
		uri.append("&businessService=").append(CalculatorConstants.CALCULATOR_PAY_BUSINESS_SERVICE);
		uri.append("&consumerCode=").append(fsmRequest.getApplicationNo());

		try {

			Optional<Object> response = serviceRequestRepository.fetchApiResult(uri,
					RequestInfoWrapper.builder().requestInfo(requestInfo).build());

			if (response.isPresent()) {
				LinkedHashMap<String, Object> responseMap = (LinkedHashMap<String, Object>) response.get();
				BillResponse billResponse = mapper.convertValue(responseMap, BillResponse.class);
				return billResponse;
			} else {
				throw new CustomException("IllegalArgumentException",
						"Did not get any response from the billing services");

			}
		}

		catch (IllegalArgumentException e) {
			throw new CustomException("IllegalArgumentException",
					"ObjectMapper not able to convert response into bill response");
		}
	}

}
