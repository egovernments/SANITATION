package org.egov.pqm.validator;


import static org.egov.pqm.util.Constants.REGEX_METACHARACTER_PATTERN;
import static org.egov.pqm.util.ErrorConstants.FILE_STORE_ID_INVALID_CODE;
import static org.egov.pqm.util.ErrorConstants.FILE_STORE_ID_INVALID_MESSAGE;
import static org.egov.pqm.util.ErrorConstants.TEST_CRITERIA_NOT_PRESENT;
import static org.egov.pqm.util.ErrorConstants.TEST_TYPE_CAN_ONLY_BE_LAB;

import java.util.List;
import java.util.UUID;
import org.egov.pqm.web.model.Document;
import org.egov.pqm.web.model.SourceType;
import org.egov.pqm.web.model.TestRequest;
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
          if (REGEX_METACHARACTER_PATTERN.matcher(document.getFileStoreId()).find()) {
            throw new CustomException(FILE_STORE_ID_INVALID_CODE, FILE_STORE_ID_INVALID_MESSAGE);
          } else {
            try {
              UUID.fromString(document.getFileStoreId());
            } catch (IllegalArgumentException e) {
              throw new CustomException(FILE_STORE_ID_INVALID_CODE, FILE_STORE_ID_INVALID_MESSAGE);
            }
          }
        } else {
          continue;
        }
      }
    }
  }
}
