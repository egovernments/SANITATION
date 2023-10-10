package org.egov.pqm.util;


import java.math.BigDecimal;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field.Str;
import org.egov.pqm.config.ServiceConfiguration;
import org.egov.pqm.web.model.QualityCriteria;
import org.egov.pqm.web.model.QualityCriteria.StatusEnum;
import org.egov.pqm.web.model.TestRequest;

import static org.egov.pqm.util.Constants.*;

import org.egov.pqm.web.model.mdms.MDMSQualityCriteria;
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

  public QualityCriteria evaluateQualityCriteria(MDMSQualityCriteria mdmsQualityCriteria,
      BigDecimal value) {
    String criteriaCode = mdmsQualityCriteria.getCode();
    String benchmarkRule = mdmsQualityCriteria.getBenchmarkRule();
    String benchmarkValuesString = mdmsQualityCriteria.getBenchmarkValues();
    String allowedDeviationString = mdmsQualityCriteria.getAllowedDeviation();

    boolean areBenchmarkRulesMet = isBenchmarkMet(value, benchmarkRule, benchmarkValuesString,
        allowedDeviationString);

    QualityCriteria qualityCriteria = QualityCriteria.builder().criteriaCode(criteriaCode)
        .value(value).status(StatusEnum.PENDING).build();

    if (areBenchmarkRulesMet) {
      qualityCriteria.setStatus(StatusEnum.PASS);
    } else {
      qualityCriteria.setStatus(StatusEnum.FAIL);
    }

    return qualityCriteria;
  }

  private boolean isBenchmarkMet(BigDecimal valueToCheck, String benchmarkRule,
      String benchmarkValuesString, String allowedDeviationString) {
    // Convert benchmarkValues, allowedDeviation and valueAsString from strings to BigDecimals
    BigDecimal[] benchmarkValues = parseBenchmarkValues(benchmarkValuesString);
    BigDecimal allowedDeviation = new BigDecimal(allowedDeviationString);

    switch (benchmarkRule) {
      case "GTR":
        if (valueToCheck.compareTo(benchmarkValues[0].subtract(allowedDeviation)) > 0) {
          return true;
        }
        break;

      case "LST":
        if (valueToCheck.compareTo(benchmarkValues[0].subtract(allowedDeviation)) < 0) {
          return true;
        }
        break;

      case "BTW":
        if ((benchmarkValues.length == 2) &&
            (valueToCheck.compareTo(benchmarkValues[0].subtract(allowedDeviation)) >= 0) &&
            (valueToCheck.compareTo(benchmarkValues[1].add(allowedDeviation)) <= 0)) {
          return true;
        }
        break;

      case "OSD":
        if ((benchmarkValues.length == 2) &&
            (valueToCheck.compareTo(benchmarkValues[0].subtract(allowedDeviation)) <= 0) &&
            (valueToCheck.compareTo(benchmarkValues[1].add(allowedDeviation)) >= 0)) {
          return true;
        }
        break;

      case "EQ":
        if (valueToCheck.compareTo(benchmarkValues[0]) == 0) {
          return true;
        }
        break;

      case "NEQ":
        if (valueToCheck.compareTo(benchmarkValues[0]) != 0) {
          return true;
        }
        break;

      case "GTROREQ":
        if (valueToCheck.compareTo(benchmarkValues[0].subtract(allowedDeviation)) >= 0) {
          return true;
        }
        break;

      case "LSTOREQ":
        if (valueToCheck.compareTo(benchmarkValues[0].subtract(allowedDeviation)) <= 0) {
          return true;
        }
        break;
    }
    return false;
  }


  /**
   * Custom function to parse BigDecimal from string
   *
   * @param value
   * @return valueAsBigDecimal
   */
  public static BigDecimal parseBigDecimal(String value) {
    try {
      return new BigDecimal(value);
    } catch (NumberFormatException e) {
      log.error("Exception while parsing String into BigDecimal ", e);
      return BigDecimal.ZERO; // Return a default value in case of an error
    }
  }

  /**
   * Custom function to parse benchmark values from a string to BigDecimal or BigDecimal array
   *
   * @param values
   * @return valueAsBigDecimal
   */
  public static BigDecimal[] parseBenchmarkValues(String values) {
    String[] valueArray = values.split(",");
    BigDecimal[] decimalValues;

    try {
      if (valueArray.length == 1) {
        // If there is only one value, parse it as a single BigDecimal
        BigDecimal singleValue = parseBigDecimal(valueArray[0]);
        decimalValues = new BigDecimal[]{singleValue};
      } else if (valueArray.length == 2) {
        // If there are two values, parse them as a BigDecimal array
        decimalValues = new BigDecimal[2];
        decimalValues[0] = parseBigDecimal(valueArray[0]);
        decimalValues[1] = parseBigDecimal(valueArray[1]);
      } else {
        // Handle invalid input
        throw new IllegalArgumentException("Invalid benchmark values input");
      }
    } catch (Exception e) {
      // Handle any other exceptions that might occur during parsing
      log.error("Error parsing benchmark values: " + e.getMessage());
      decimalValues = new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO}; // Return default values
    }

    return decimalValues;
  }

}
