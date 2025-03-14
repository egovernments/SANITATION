package org.egov.vendor.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.egov.tracer.model.CustomException;
import org.egov.vendor.config.VendorConfiguration;
import org.egov.vendor.repository.ServiceRequestRepository;
import org.egov.vendor.web.model.RequestInfoWrapper;
import org.egov.vendor.web.model.Vendor;
import org.egov.vendor.web.model.VendorRequest;
import org.egov.vendor.web.model.location.Boundary;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BoundaryService {

	private ServiceRequestRepository serviceRequestRepository;

	private ObjectMapper mapper;

	private VendorConfiguration config;

	@Autowired
	public BoundaryService(ServiceRequestRepository serviceRequestRepository, ObjectMapper mapper,
			VendorConfiguration config) {
		this.serviceRequestRepository = serviceRequestRepository;
		this.mapper = mapper;
		this.config = config;
	}

	public void getAreaType(VendorRequest request, String hierarchyTypeCode) {
		if (request.getVendor() == null) {
			return;
		}
		Vendor vendor = request.getVendor();

		String tenantId = request.getVendor().getTenantId();

		if (vendor.getAddress() == null || vendor.getAddress().getLocality() == null) {
			throw new CustomException("Invalid Address", "The address or locality cannot be null");
		}

		StringBuilder uri = new StringBuilder(config.getLocationHost());
		uri.append(config.getLocationContextPath()).append(config.getLocationEndpoint());
		uri.append("?").append("tenantId=").append(tenantId);

		if (hierarchyTypeCode != null) {
			uri.append("&").append("hierarchyType=").append(hierarchyTypeCode);
		}
		uri.append("&").append("boundaryType=").append("Locality");
		uri.append("&").append("codes=").append(vendor.getAddress().getLocality().getCode());

		RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(request.getRequestInfo()).build();
		LinkedHashMap responseMap = (LinkedHashMap) serviceRequestRepository.fetchResult(uri, wrapper);

		if (CollectionUtils.isEmpty(responseMap)) {
			throw new CustomException("FSMErrorConstants.BOUNDARY_ERROR",
					"The response from location service is empty or null");
		}

		String jsonString = new JSONObject(responseMap).toString();

		DocumentContext context = JsonPath.parse(jsonString);

		List<Boundary> boundaryResponse = context
				.read("$..boundary[?(@.code==\"{}\")]".replace("{}", vendor.getAddress().getLocality().getCode()));

		if (boundaryResponse != null && CollectionUtils.isEmpty((boundaryResponse))) {
			log.debug("The boundary data was not found");
			throw new CustomException("FSMErrorConstants.BOUNDARY_MDMS_DATA_ERROR", "The boundary data was not found");
		}

		Boundary boundary = mapper.convertValue(boundaryResponse != null ? boundaryResponse.get(0) : new Boundary(),
				Boundary.class);

		if (boundary.getCode() == null) {

			throw new CustomException("FSMErrorConstants.INVALID_BOUNDARY_DATA", "The boundary data for the code "
					+ vendor.getAddress().getLocality().getCode() + " is not available");
		}

		vendor.getAddress().setLocality(boundary);

	}
}
