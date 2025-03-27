package org.egov.vendor.driver.repository;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.vendor.config.VendorConfiguration;
import org.egov.vendor.driver.repository.querybuilder.DriverQueryBuilder;
import org.egov.vendor.driver.repository.rowmapper.DriverRowMapper;
import org.egov.vendor.driver.repository.rowmapper.WorkerRowMapper;
import org.egov.vendor.driver.web.model.Driver;
import org.egov.vendor.driver.web.model.DriverRequest;
import org.egov.vendor.driver.web.model.DriverResponse;
import org.egov.vendor.driver.web.model.DriverSearchCriteria;
import org.egov.vendor.driver.web.model.Worker;
import org.egov.vendor.driver.web.model.WorkerSearchCriteria;
import org.egov.vendor.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class DriverRepository {

	@Autowired
	private Producer producer;

	@Autowired
	private VendorConfiguration configuration;

	@Autowired
	private DriverQueryBuilder driverQueryBuilder;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private DriverRowMapper driverRowMapper;

	@Autowired
	private WorkerRowMapper workerRowMapper;

	public void save(DriverRequest driverRequest) {
		producer.push(driverRequest.getDriver().getTenantId(), configuration.getSaveDriverTopic(), driverRequest);
	}

	public void update(DriverRequest driverRequest) {
		producer.push(driverRequest.getDriver().getTenantId(), configuration.getUpdateDriverTopic(), driverRequest);
	}

	public DriverResponse getDriverData(DriverSearchCriteria driverSearchCriteria) {
		List<Object> preparedStmtList = new ArrayList<>();
		String query = driverQueryBuilder.getDriverSearchQuery(driverSearchCriteria, preparedStmtList);
		log.info("DriverSearch Query" + query);
		List<Driver> driverData = jdbcTemplate.query(query, preparedStmtList.toArray(), driverRowMapper);
		return DriverResponse.builder().driver(driverData).totalCount(Integer.valueOf(driverRowMapper.getFullCount()))
				.build();
	}

	public List<Worker> getWorkersData(WorkerSearchCriteria workerSearchCriteria) {
		List<Object> preparedStmtList = new ArrayList<>();
		String query = driverQueryBuilder.getWorkerSearchQuery(workerSearchCriteria, preparedStmtList);
		log.info("Workers Search Query" + query);
		return jdbcTemplate.query(query, preparedStmtList.toArray(), workerRowMapper);
	}

	public List<String> fetchDriverIdsWithNoVendor(@Valid DriverSearchCriteria criteria) {
		List<Object> preparedStmtList = new ArrayList<>();
		String query = driverQueryBuilder.getDriverIdsWithNoVendorQuery(criteria, preparedStmtList);
		log.info("DriverQuery:: " + query);
		return jdbcTemplate.query(query, preparedStmtList.toArray(), new SingleColumnRowMapper<>(String.class));
	}

	public String getdriverSeqMobileNum(String seqDriverMobileNumber) {
		List<Object> preparedStmtList = new ArrayList<>();

		String query = driverQueryBuilder.getSeqDriverMobileNumber(seqDriverMobileNumber, preparedStmtList);

		return jdbcTemplate.queryForObject(query, preparedStmtList.toArray(), String.class);

	}

}
