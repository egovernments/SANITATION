package org.egov.pqm.validator;


import static org.egov.pqm.util.Constants.REGEX_METACHARACTER_PATTERN;
import static org.egov.pqm.util.Constants.UPDATE_RESULT;
import static org.egov.pqm.util.ErrorConstants.*;

import java.util.List;
import java.util.Objects;
import org.egov.pqm.web.model.Document;
import org.egov.pqm.web.model.SourceType;
import org.egov.pqm.web.model.Test;

import org.egov.pqm.web.model.TestRequest;
import org.egov.pqm.web.model.TestResultStatus;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;

@Component
public class PqmValidator {

  public void validateTestCriteriaAndDocument(TestRequest testRequest) {
    validateTestCriteria(testRequest);
    if (Objects.equals(testRequest.getTests().get(0).getSourceType(), SourceType.LAB_ADHOC)
        || (testRequest.getTests().get(0).getWorkflow() != null && Objects.equals(
        testRequest.getTests().get(0).getWorkflow().getAction(), UPDATE_RESULT))
    ) {
      validateDocumentFields(testRequest);
    } else {
      validateDocumentPresence(testRequest);
    }
  }

  private void validateDocumentPresence(TestRequest testRequest) {
    if (testRequest.getTests().get(0).getDocuments() != null) {
      if (!testRequest.getTests().get(0).getDocuments().isEmpty()) {
        throw new CustomException("DOCUMENT IS ONLY ATTACHED IN UPDATE_RESULT ACTION ",
            "Document is only attached in update result action");
      }
    }
  }

  public void validateTestTypeAdhocCreate(TestRequest testRequest) {
    if (testRequest.getTests().get(0).getSourceType() == null
        || testRequest.getTests().get(0).getSourceType() == SourceType.IOT_SCHEDULED
        || testRequest.getTests().get(0).getSourceType() == SourceType.LAB_SCHEDULED) {
      throw new CustomException(TEST_TYPE_CAN_ONLY_BE_LAB_ADHOC_CODE,
          TEST_TYPE_CAN_ONLY_BE_LAB_ADHOC_MESSAGE);
    }
  }

  public void validateTestTypeScheduleCreateAndUpdate(TestRequest testRequest) {
    if (testRequest.getTests().get(0).getSourceType() == null
        || testRequest.getTests().get(0).getSourceType() == SourceType.IOT_SCHEDULED
        || testRequest.getTests().get(0).getSourceType() == SourceType.LAB_ADHOC) {
      throw new CustomException(TEST_TYPE_CAN_ONLY_BE_LAB_SCHEDULED_CODE,
          TEST_TYPE_CAN_ONLY_BE_LAB_SCHEDULED_MESSAGE);
    }
  }

  private void validateTestCriteria(TestRequest testRequest) {
    if (testRequest.getTests().get(0).getQualityCriteria() == null || testRequest.getTests()
        .get(0)
        .getQualityCriteria().isEmpty()) {
      throw new CustomException(TEST_CRITERIA_NOT_PRESENT, "test criteria not present");
    }
  }

  private void validateDocumentFields(TestRequest testRequest) {
    boolean isValid = true;
    if (testRequest.getTests().get(0).getDocuments() != null && !testRequest.getTests().get(0)
        .getDocuments().isEmpty()) {
      for (Document document : testRequest.getTests().get(0).getDocuments()) {
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

  public void validateTestRequestFieldsWhileupdate
      (List<Test> testList, List<Test> oldTestList) {
    Test test = testList.get(0);
    Test oldTest = oldTestList.get(0);
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

    if (test.getStatus() != TestResultStatus.PENDING) {
      throw new CustomException(STATUS_ERROR_CODE, STATUS_ERROR_MESSAGE);
    }

    if (!test.getId().equals(oldTest.getId())) {
      throw new CustomException(ID_CHANGED_ERROR, ID_CHANGED_MESSAGE);
    }
  }
}
