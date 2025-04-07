package org.egov.vendor.util;

import static org.egov.vendor.util.VendorConstants.FSM_MODULE;
import static org.egov.vendor.util.VendorConstants.VENDOR_MODULE;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.utils.MultiStateInstanceUtil;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.ModuleDetail;
import org.egov.tracer.model.CustomException;
import org.egov.vendor.config.VendorConfiguration;
import org.egov.vendor.repository.ServiceRequestRepository;
import org.egov.vendor.web.model.AuditDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;

@Component
public class VendorUtil {

	@Autowired
	private ServiceRequestRepository serviceRequestRepository;

	@Autowired
	private VendorConfiguration vendorConfiguration;
	
	private MultiStateInstanceUtil multiStateInstanceUtil;

	@Autowired
	public VendorUtil(MultiStateInstanceUtil multiStateInstanceUtil) {
		this.multiStateInstanceUtil = multiStateInstanceUtil;
	}

	public void defaultJsonPathConfig() {
		Configuration.setDefaults(new Configuration.Defaults() {

			private final JsonProvider jsonProvider = new JacksonJsonProvider();
			private final MappingProvider mappingProvider = new JacksonMappingProvider();

			@Override
			public Set<Option> options() {
				return EnumSet.noneOf(Option.class);
			}

			@Override
			public MappingProvider mappingProvider() {
				return mappingProvider;
			}

			@Override
			public JsonProvider jsonProvider() {
				return jsonProvider;
			}
		});
	}

	public Object mDMSCall(RequestInfo requestInfo, String tenantId, String module) {
		MdmsCriteriaReq mdmsCriteriaReq = getMDMSRequest(requestInfo, tenantId, module);
		return serviceRequestRepository.fetchResult(getMdmsSearchUrl(), mdmsCriteriaReq);
	}

	public StringBuilder getMdmsSearchUrl() {
		return new StringBuilder().append(vendorConfiguration.getMdmsHost())
				.append(vendorConfiguration.getMdmsEndPoint());
	}

	public MdmsCriteriaReq getMDMSRequest(RequestInfo requestInfo, String tenantId, String module) {

		List<ModuleDetail> moduleRequest = null;
		if(module.equals(VENDOR_MODULE))
			moduleRequest = getVendorModuleRequest();
		else if(module.equals(FSM_MODULE))
			moduleRequest = getSanitationWorkerMDMSRequest();

		List<ModuleDetail> moduleDetails = new LinkedList<>();
		moduleDetails.addAll(moduleRequest);
		MdmsCriteria mdmsCriteria = MdmsCriteria.builder().moduleDetails(moduleDetails).tenantId(tenantId).build();
		return MdmsCriteriaReq.builder().mdmsCriteria(mdmsCriteria).requestInfo(requestInfo).build();
	}

	public List<ModuleDetail> getVendorModuleRequest() {

		final String activeFilter = "$.[?(@.active==true)]";
		List<ModuleDetail> moduleDtls = new ArrayList<>();

		List<MasterDetail> masterDtls = new ArrayList<>();
		masterDtls.add(MasterDetail.builder().name(VendorConstants.VENDOR_AGENCY_TYPE).filter(activeFilter).build());
		masterDtls.add(
				MasterDetail.builder().name(VendorConstants.VENDOR_PAYMENT_PREFERENCE).filter(activeFilter).build());
		moduleDtls.add(
				ModuleDetail.builder().masterDetails(masterDtls).moduleName(VENDOR_MODULE).build());

		return moduleDtls;

	}

	public List<ModuleDetail> getSanitationWorkerMDMSRequest()
	{
		final String activeFilter = "$.[?(@.active==true)]";
		List<ModuleDetail> moduleDtls = new ArrayList<>();

		List<MasterDetail> masterDtls = new ArrayList<>();
		masterDtls.add(MasterDetail.builder().name(VendorConstants.SW_FUNCTIONAL_ROLES).filter(activeFilter).build());
		masterDtls.add(
				MasterDetail.builder().name(VendorConstants.SW_SKILLS).filter(activeFilter).build());

		moduleDtls.add(
				ModuleDetail.builder().masterDetails(masterDtls).moduleName(VendorConstants.FSM_MODULE).build());

		return moduleDtls;
	}

	public AuditDetails getAuditDetails(String by, Boolean isCreate) {
		Long time = System.currentTimeMillis();
		if (isCreate)
			return AuditDetails.builder().createdBy(by).lastModifiedBy(by).createdTime(time).lastModifiedTime(time)
					.build();
		else
			return AuditDetails.builder().lastModifiedBy(by).lastModifiedTime(time).build();
	}

	/**
	 * Method to fetch the state name from the tenantId
	 *
	 * @param query
	 * @param tenantId
	 * @return
	 */
	public String replaceSchemaPlaceholder(String query, String tenantId) {

		String finalQuery = null;

		try {
			finalQuery = multiStateInstanceUtil.replaceSchemaPlaceholder(query, tenantId);
		} catch (Exception e) {
			throw new CustomException("INVALID_TENANTID", "Invalid tenantId: " + tenantId);
		}
		return finalQuery;
	}
}
