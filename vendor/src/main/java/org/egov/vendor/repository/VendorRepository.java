package org.egov.vendor.repository;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.tracer.model.CustomException;
import org.egov.vendor.config.VendorConfiguration;
import org.egov.vendor.producer.Producer;
import org.egov.vendor.repository.querybuilder.VendorQueryBuilder;
import org.egov.vendor.repository.rowmapper.VendorRowMapper;
import org.egov.vendor.util.VendorUtil;
import org.egov.vendor.web.model.Vendor;
import org.egov.vendor.web.model.VendorRequest;
import org.egov.vendor.web.model.VendorResponse;
import org.egov.vendor.web.model.VendorSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class VendorRepository {

	@Autowired
	private Producer producer;

	@Autowired
	private VendorConfiguration configuration;

	@Autowired
	private VendorQueryBuilder vendorQueryBuilder;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private VendorRowMapper vendorrowMapper;
	
	@Autowired
	private VendorUtil vendorUtil;

	public void save(VendorRequest vendorRequest) {
		producer.push(vendorRequest.getVendor().getTenantId(), configuration.getSaveTopic(), vendorRequest);
	}

	public void update(VendorRequest vendorRequest) {
		producer.push(vendorRequest.getVendor().getTenantId(), configuration.getUpdateTopic(), vendorRequest);
	}

	public void updateVendorVehicleDriver(VendorRequest vendorRequest) {
		producer.push(vendorRequest.getVendor().getTenantId(), configuration.getSaveVendorVehicleDriverTopic(), vendorRequest);
	}

	public VendorResponse getVendorData(VendorSearchCriteria vendorSearchCriteria) {
		List<Object> preparedStmtList = new ArrayList<>();
		String query = vendorQueryBuilder.getVendorSearchQuery(vendorSearchCriteria, preparedStmtList);
	    query = vendorUtil.replaceSchemaPlaceholder(query, vendorSearchCriteria.getTenantId());
	    log.info("Get vendors query ::=>" + query.toString());
		List<Vendor> vendorData = jdbcTemplate.query(query, preparedStmtList.toArray(), vendorrowMapper);
		return VendorResponse.builder().vendor(vendorData).totalCount(Integer.valueOf(vendorrowMapper.getFullCount()))
				.build();
	}

	public List<String> getDrivers(String id, String status, String tenantId) {
		List<String> ids = null;
		List<Object> preparedStmtList = new ArrayList<>();
		preparedStmtList.add(id);
		preparedStmtList.add(status);
		String query = vendorQueryBuilder.getDriverSearchQuery();
		query = vendorUtil.replaceSchemaPlaceholder(query, tenantId);
		ids = jdbcTemplate.queryForList(query, preparedStmtList.toArray(), String.class);
		return ids;
	}

	public List<String> getWorkers(String id, String status, String tenantId) {
		List<String> ids = null;
		List<Object> preparedStmtList = new ArrayList<>();
		preparedStmtList.add(id);
		preparedStmtList.add(status);
		String query = vendorQueryBuilder.getWorkerSearchQuery();
		query = vendorUtil.replaceSchemaPlaceholder(query, tenantId);
		ids = jdbcTemplate.queryForList(query, preparedStmtList.toArray(), String.class);
		return ids;
	}

	public List<String> getVehicles(String id, String status, String tenantId) {
		List<String> ids = null;
		List<Object> preparedStmtList = new ArrayList<>();
		preparedStmtList.add(id);
		preparedStmtList.add(status);
		String query = vendorQueryBuilder.getVehicleSearchQuery();
		query = vendorUtil.replaceSchemaPlaceholder(query, tenantId);
		ids = jdbcTemplate.queryForList(query, preparedStmtList.toArray(), String.class);
		return ids;
	}

	public List<String> getVendorWithVehicles(VendorSearchCriteria vendorSearchCriteria) {
		List<String> vendorIds = null;
		List<Object> preparedStmtList = new ArrayList<>();
		String query = vendorQueryBuilder.vendorsForVehicles(vendorSearchCriteria, preparedStmtList);
		query = vendorUtil.replaceSchemaPlaceholder(query, vendorSearchCriteria.getTenantId());
		vendorIds = jdbcTemplate.queryForList(query, preparedStmtList.toArray(), String.class);
		return vendorIds;
	}

	public List<String> getVendorWithDrivers(VendorSearchCriteria vendorSearchCriteria) {
		List<String> vendorIds = null;
		List<Object> preparedStmtList = new ArrayList<>();
		String query = vendorQueryBuilder.vendorsForDrivers(vendorSearchCriteria, preparedStmtList);
		query = vendorUtil.replaceSchemaPlaceholder(query, vendorSearchCriteria.getTenantId());
		vendorIds = jdbcTemplate.queryForList(query, preparedStmtList.toArray(), String.class);
		return vendorIds;
	}

	public List<String> getVendorWithWorker(VendorSearchCriteria vendorSearchCriteria) {
		List<String> vendorIds = null;
		List<Object> preparedStmtList = new ArrayList<>();
		String query = vendorQueryBuilder.vendorsFoWorkers(vendorSearchCriteria, preparedStmtList);
		query = vendorUtil.replaceSchemaPlaceholder(query, vendorSearchCriteria.getTenantId());
		vendorIds = jdbcTemplate.queryForList(query, preparedStmtList.toArray(), String.class);
		return vendorIds;
	}

	public List<String> fetchVendorIds(@Valid VendorSearchCriteria criteria) {
		List<Object> preparedStmtList = new ArrayList<>();
		preparedStmtList.add(criteria.getOffset());
		preparedStmtList.add(criteria.getLimit());
		String query = "SELECT id from {schema}.eg_vendor ORDER BY createdtime offset " + " ? " + "limit ? ";
		query = vendorUtil.replaceSchemaPlaceholder(query, criteria.getTenantId());
		return jdbcTemplate.query(query, preparedStmtList.toArray(), new SingleColumnRowMapper<>(String.class));
	}

	public List<Vendor> getVendorPlainSearch(VendorSearchCriteria criteria) {

		if (criteria.getIds() == null || criteria.getIds().isEmpty())
			throw new CustomException("PLAIN_SEARCH_ERROR", "Search only allowed by ids!");

		List<Object> preparedStmtList = new ArrayList<>();
		String query = vendorQueryBuilder.getVendorLikeQuery(criteria, preparedStmtList);
	    query = vendorUtil.replaceSchemaPlaceholder(query, criteria.getTenantId());
		query = vendorUtil.replaceSchemaPlaceholder(query, criteria.getTenantId());
		log.info("Query: " + query);
		log.info("PS: " + preparedStmtList);
		return jdbcTemplate.query(query, preparedStmtList.toArray(), vendorrowMapper);
	}

	public int getExistingVenodrsCount(List<String> ownerIdList , String tenantId) {
		List<Object> preparedStmtList = new ArrayList<>();

		String query = vendorQueryBuilder.getvendorCount(ownerIdList, preparedStmtList);
	    query = vendorUtil.replaceSchemaPlaceholder(query, tenantId);
		return jdbcTemplate.queryForObject(query, preparedStmtList.toArray(), Integer.class);

	}

}
