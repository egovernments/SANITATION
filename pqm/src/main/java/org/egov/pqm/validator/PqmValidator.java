package org.egov.pqm.validator;


import static org.egov.pqm.util.Constants.REGEX_METACHARACTER_PATTERN;
import static org.egov.pqm.util.Constants.UPDATE_RESULT;
import static org.egov.pqm.util.ErrorConstants.CRITERIA_CODE_INVALID_CODE;
import static org.egov.pqm.util.ErrorConstants.CRITERIA_CODE_INVALID_MESSAGE;
import static org.egov.pqm.util.ErrorConstants.FILE_STORE_ID_INVALID_CODE;
import static org.egov.pqm.util.ErrorConstants.FILE_STORE_ID_INVALID_MESSAGE;
import static org.egov.pqm.util.ErrorConstants.MATERIAL_CODE_INVALID_CODE;
import static org.egov.pqm.util.ErrorConstants.MATERIAL_CODE_INVALID_MESSAGE;
import static org.egov.pqm.util.ErrorConstants.PLANT_CODE_INVALID_CODE;
import static org.egov.pqm.util.ErrorConstants.PLANT_CODE_INVALID_MESSAGE;
import static org.egov.pqm.util.ErrorConstants.PROCESS_CODE_INVALID_CODE;
import static org.egov.pqm.util.ErrorConstants.PROCESS_CODE_INVALID_MESSAGE;
import static org.egov.pqm.util.ErrorConstants.STAGE_CODE_INVALID_CODE;
import static org.egov.pqm.util.ErrorConstants.STAGE_CODE_INVALID_MESSAGE;
import static org.egov.pqm.util.ErrorConstants.STATUS_ERROR_CODE;
import static org.egov.pqm.util.ErrorConstants.STATUS_ERROR_MESSAGE;
import static org.egov.pqm.util.ErrorConstants.TEST_CRITERIA_NOT_PRESENT;
import static org.egov.pqm.util.ErrorConstants.TEST_TYPE_CAN_ONLY_BE_LAB;
import static org.egov.pqm.util.ErrorConstants.TEST_TYPE_INVALID_CODE;
import static org.egov.pqm.util.ErrorConstants.TEST_TYPE_INVALID_MESSAGE;

import java.util.List;
import java.util.Objects;

import org.egov.pqm.util.ErrorConstants;
import org.egov.pqm.web.model.Document;
import org.egov.pqm.web.model.SourceType;
import org.egov.pqm.web.model.Test;

import org.egov.pqm.web.model.TestRequest;
import org.egov.pqm.web.model.TestResultStatus;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;

@Component
public class PqmValidator {

  public void validateTestRequestBody(TestRequest testRequest) {
    validateTestCriteria(testRequest);
    validateTestType(testRequest);
    validateDocumentFields(testRequest);
  }

  private void validateTestType(TestRequest testRequest) {
    if (testRequest.getTests().get(0).getSourceType() == null
        || testRequest.getTests().get(0).getSourceType() == SourceType.IOT) {
      throw new CustomException(TEST_TYPE_CAN_ONLY_BE_LAB, "test type can only be lab");
    }
  }

  private void validateTestCriteria(TestRequest testRequest) {
    if (testRequest.getTests().get(0).getQualityCriteria() == null || testRequest.getTests().get(0)
        .getQualityCriteria().isEmpty()) {
      throw new CustomException(TEST_CRITERIA_NOT_PRESENT, "test criteria not present");
    }
  }

  private void validateDocumentFields(TestRequest testRequest) {
    List<Document> documentList = testRequest.getTests().get(0).getDocuments();
    boolean isValid = true;
    if (!documentList.isEmpty()) {
      for (Document document : documentList) {
        if (document.getFileStoreId() != null) {
          if (!REGEX_METACHARACTER_PATTERN.matcher(document.getFileStoreId()).find()) {
            throw new CustomException(FILE_STORE_ID_INVALID_CODE, FILE_STORE_ID_INVALID_MESSAGE);
          }
        } else {
          continue;
        }
      }
    }
  }

  public void validateTestRequestFieldsWhileupdate(List<Test> testList, List<Test> oldTestList) {
    Test test = testList.get(0);
    Test oldTest = oldTestList.get(0);
    if(test.getId() != oldTest.getId()){
      throw new CustomException(ErrorConstants.ID_CHANGED_ERROR, "id cannot be changed");
    }
    if (test.getSourceType() != oldTest.getSourceType()) {
      throw new CustomException(TEST_TYPE_INVALID_CODE, TEST_TYPE_INVALID_MESSAGE);
    }
    if (!Objects.equals(test.getPlantCode(), oldTest.getPlantCode())) {
      throw new CustomException(PLANT_CODE_INVALID_CODE, PLANT_CODE_INVALID_MESSAGE);
    }
    if (!Objects.equals(test.getProcessCode(), oldTest.getProcessCode())) {
      throw new CustomException(PROCESS_CODE_INVALID_CODE, PROCESS_CODE_INVALID_MESSAGE);
    }
    if (!Objects.equals(test.getMaterialCode(), oldTest.getMaterialCode())) {
      throw new CustomException(MATERIAL_CODE_INVALID_CODE, MATERIAL_CODE_INVALID_MESSAGE);
    }
    if (!Objects.equals(test.getStageCode(), oldTest.getStageCode())) {
      throw new CustomException(STAGE_CODE_INVALID_CODE, STAGE_CODE_INVALID_MESSAGE);
    }
    if (!Objects.equals(test.getQualityCriteria().get(0).getCriteriaCode(),
        oldTest.getQualityCriteria().get(0).getCriteriaCode())) {
      throw new CustomException(CRITERIA_CODE_INVALID_CODE, CRITERIA_CODE_INVALID_MESSAGE);
    }
    if (!Objects.equals(test.getWorkflow().getAction(), UPDATE_RESULT)) {
      if (test.getStatus() != TestResultStatus.PENDING) {
        throw new CustomException(STATUS_ERROR_CODE, STATUS_ERROR_MESSAGE);
      }
    }
  }
}
