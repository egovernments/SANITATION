package org.egov.pqm.repository.rowmapper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.egov.pqm.web.model.AuditDetails;
import org.egov.pqm.web.model.QualityCriteria;
import org.egov.pqm.web.model.Test;
import org.egov.pqm.web.model.TestResultStatus;
import org.egov.pqm.web.model.TestType;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TestRowMapper implements ResultSetExtractor<List<Test>> {

	@Autowired
	private ObjectMapper mapper;

	private int fullCount = 0;

	public int getFullCount() {
		return fullCount;
	}

	public void setFullCount(int fullCount) {
		this.fullCount = fullCount;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Test> extractData(ResultSet rs) throws SQLException {

		Map<String, Test> testMap = new LinkedHashMap<>();
		this.setFullCount(0);
		while (rs.next()) {
			String id = rs.getString("id");
			Test currentTest = testMap.get(id);

			if (currentTest == null) {
				String tenantId = rs.getString("tenantId");
				String plantCode = rs.getString("plantCode");
				String processCode = rs.getString("processCode");
				String stageCode = rs.getString("stageCode");
				String materialCode = rs.getString("materialCode");
				String deviceCode = rs.getString("deviceCode");
				String qualityCriteriaJson = rs.getString("qualityCriteria");
				List<QualityCriteria> qualityCriteriaList = parseQualityCriteriaJson(qualityCriteriaJson);
				String statusString = rs.getString("status");
				TestResultStatus status = TestResultStatus.valueOf(statusString.toUpperCase());
				String wfStatus = rs.getString("wfStatus");
				String testTypeString = rs.getString("testType");
				TestType testType = TestType.valueOf(testTypeString.toUpperCase());
				Long scheduledDate = rs.getLong("scheduledDate");
				Boolean isActive = rs.getBoolean("isActive");
				this.setFullCount(rs.getInt("full_count"));
				Object additionaldetails = getAdditionalDetail("additionaldetails", rs);
				AuditDetails auditdetails = AuditDetails.builder().createdBy(rs.getString("createdby"))
						.createdTime(rs.getLong("createdtime")).lastModifiedBy(rs.getString("lastmodifiedby"))
						.lastModifiedTime(rs.getLong("lastmodifiedtime")).build();

				currentTest = Test.builder().id(id).tenantId(tenantId).plantCode(plantCode).processCode(processCode)
						.stageCode(stageCode).materialCode(materialCode).deviceCode(deviceCode)
						.qualityCriteria(qualityCriteriaList).status(status).wfStatus(wfStatus).testType(testType)
						.scheduledDate(scheduledDate).isActive(isActive).additionalDetails(additionaldetails)
						.auditDetails(auditdetails).build();

				testMap.put(id, currentTest);
			}
		}

		return new ArrayList<>(testMap.values());
	}

	private List<QualityCriteria> parseQualityCriteriaJson(String json) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.readValue(json, new TypeReference<List<QualityCriteria>>() {
			});
		} catch (IOException e) {
			throw new RuntimeException("Error parsing QualityCriteria JSON", e);
		}
	}

	private JsonNode getAdditionalDetail(String columnName, ResultSet rs) {

		JsonNode additionalDetail = null;
		try {
			PGobject pgObj = (PGobject) rs.getObject(columnName);
			if (pgObj != null) {
				additionalDetail = mapper.readTree(pgObj.getValue());
			}
		} catch (IOException | SQLException e) {
			throw new CustomException("PARSING_ERROR", "Failed to parse additionalDetail object");
		}
		return additionalDetail;
	}
}
