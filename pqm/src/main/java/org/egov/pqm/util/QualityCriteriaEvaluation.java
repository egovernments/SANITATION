package org.egov.pqm.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field.Str;
import org.egov.pqm.config.ServiceConfiguration;
import org.egov.pqm.web.model.QualityCriteria;
import org.egov.pqm.web.model.QualityCriteria.StatusEnum;
import org.egov.pqm.web.model.Test;
import org.egov.pqm.web.model.TestRequest;

import static org.egov.pqm.util.Constants.*;
import static org.egov.pqm.util.MDMSUtils.parseJsonToMap;

import org.egov.pqm.web.model.mdms.MDMSQualityCriteria;
import org.egov.tracer.model.CustomException;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class QualityCriteriaEvaluation {

  @Autowired
  private MDMSUtils mdmsUtil;

  @Autowired
  private ServiceConfiguration config;

  /**
   * Evaluates QualityCriteria list for a Test Object
   *
   * @param testRequest The Test Request Object
   */
  public void evalutateQualityCriteria(TestRequest testRequest) {
    Test test = testRequest.getTests().get(0);

    //fetch mdms data for QualityCriteria Master
    Object jsondata = mdmsUtil.mdmsCallV2(testRequest.getRequestInfo(),
        testRequest.getTests().get(0).getTenantId(), MASTER_NAME_QUALITY_CRITERIA);
    String jsonString = "";

    try {
      ObjectMapper objectMapper = new ObjectMapper();
      jsonString = objectMapper.writeValueAsString(jsondata);
    } catch (Exception e) {
      throw new CustomException(ErrorConstants.PARSING_ERROR,
          "Unable to parse QualityCriteria mdms data ");
    }

    // Parse JSON Response and create the map for QualityCriteria
    Map<String, MDMSQualityCriteria> codeToQualityCriteriaMap = parseJsonToMap(jsonString);

    //evaluate Quality Criteria
    List<QualityCriteria> evaluatedqualityCriteriaList = new ArrayList<>();
    for (QualityCriteria qualityCriteria : test.getQualityCriteria()) {
      QualityCriteria evaluatedqualityCriteria = enrichQualityCriteriaFields(
          codeToQualityCriteriaMap.get(qualityCriteria.getCriteriaCode()),
          qualityCriteria.getValue());

      evaluatedqualityCriteriaList.add(evaluatedqualityCriteria);
    }
    test.setQualityCriteria(evaluatedqualityCriteriaList);
  }

  /**
   * returns a qualityCriteria with enriched status and allowedDeviation
   *
   * @param mdmsQualityCriteria MDMS Quality Criteria
   * @param value               Value to Test
   * @return QualityCriteria
   */
  public QualityCriteria enrichQualityCriteriaFields(MDMSQualityCriteria mdmsQualityCriteria,
      BigDecimal value) {
    String criteriaCode = mdmsQualityCriteria.getCode();
    String benchmarkRule = mdmsQualityCriteria.getBenchmarkRule();
    List<BigDecimal> benchmarkValues = mdmsQualityCriteria.getBenchmarkValues();
    BigDecimal allowedDeviation = mdmsQualityCriteria.getAllowedDeviation();

    boolean areBenchmarkRulesMet = isBenchmarkMet(value, benchmarkRule,
        benchmarkValues.toArray(new BigDecimal[0]),
        allowedDeviation);

    QualityCriteria qualityCriteria = QualityCriteria.builder().criteriaCode(criteriaCode)
        .value(value).status(StatusEnum.PENDING).build();

    if (areBenchmarkRulesMet) {
      qualityCriteria.setStatus(StatusEnum.PASS);
    } else {
      qualityCriteria.setStatus(StatusEnum.FAIL);
    }

    //enriching allowedDeviation
    qualityCriteria.setAllowedDeviation(mdmsQualityCriteria.getAllowedDeviation());
    return qualityCriteria;
  }


  /**
   * Parsing Json Data to a Code-QualityCriteria Map
   *
   * @param valueToCheck     Value to Test
   * @param benchmarkRule    Benchmark Rule to test against
   * @param benchmarkValues  Benchmark Values to test against
   * @param allowedDeviation Allowed Deviation
   * @return isBenchmarkMet - true if benchmark is met
   */
  private boolean isBenchmarkMet(BigDecimal valueToCheck, String benchmarkRule,
      BigDecimal[] benchmarkValues, BigDecimal allowedDeviation) {

    switch (benchmarkRule) {
      case GREATER_THAN:
        if (valueToCheck.compareTo(benchmarkValues[0].subtract(allowedDeviation)) > 0) {
          return true;
        }
        break;

      case LESS_THAN:
        if (valueToCheck.compareTo(benchmarkValues[0].subtract(allowedDeviation)) < 0) {
          return true;
        }
        break;

      case BETWEEN:
        if ((benchmarkValues.length == 2) &&
            (valueToCheck.compareTo(benchmarkValues[0].subtract(allowedDeviation)) >= 0) &&
            (valueToCheck.compareTo(benchmarkValues[1].add(allowedDeviation)) <= 0)) {
          return true;
        }
        break;

      case OUTSIDE_RANGE:
        if ((benchmarkValues.length == 2) &&
            (valueToCheck.compareTo(benchmarkValues[0].subtract(allowedDeviation)) <= 0) ||
            (valueToCheck.compareTo(benchmarkValues[1].add(allowedDeviation)) >= 0)) {
          return true;
        }
        break;

      case EQUALS:
        if (valueToCheck.compareTo(benchmarkValues[0]) == 0) {
          return true;
        }
        break;

      case NOT_EQUAL:
        if (valueToCheck.compareTo(benchmarkValues[0]) != 0) {
          return true;
        }
        break;

      case GREATER_THAN_EQUAL_TO:
        if (valueToCheck.compareTo(benchmarkValues[0].subtract(allowedDeviation)) >= 0) {
          return true;
        }
        break;

      case LESS_THAN_EQUAL_TO:
        if (valueToCheck.compareTo(benchmarkValues[0].subtract(allowedDeviation)) <= 0) {
          return true;
        }
        break;
    }
    return false;
  }

}