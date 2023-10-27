import React, { Fragment } from "react";
import { CardCaption, CardSubHeader, SubmitBar } from "@egovernments/digit-ui-react-components";
import CardReading from "./CardReadings";
import CardMessage from "./CardMessage";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";

function ParameterReadings({ reading }) {
  const { t } = useTranslation();
  const history = useHistory();
  const isTestPassed = reading?.readings?.map((i) => i.resultStatus).includes("FAIL") ? false : true;
  return reading ? (
    <>
      {reading?.title ? <CardSubHeader>{t(reading?.title)}</CardSubHeader> : null}
      {reading?.date ? (
        <CardCaption style={{ display: "flex" }}>
          <p>{t("ES_TQM_TEST_RESULTS_DATE_LABEL")}: </p>
          <p> {reading?.date}</p>
        </CardCaption>
      ) : null}
      {reading?.readings && reading?.readings?.length > 0
        ? reading?.readings?.map(({ criteriaCode, resultValue, resultStatus }) => <CardReading showInfo={true} success={resultStatus === "PASS"} title={criteriaCode} value={resultValue} />)
        : null}
      {reading?.readings?.length > 0 ? (
        <CardMessage
          success={isTestPassed}
          title={isTestPassed ? t("ES_TQM_TEST_PARAMS_SUCCESS_TITLE") : t("ES_TQM_TEST_PARAMS_FAIL_TITLE")}
          message={
            isTestPassed ? (
              <>
                <p>{t("ES_TQM_TEST_PARAMS_SUCCESS_MESSAGE_PRI")}</p>
                <p>{t("ES_TQM_TEST_PARAMS_SUCCESS_MESSAGE_SECND")}</p>
              </>
            ) : (
              <>
                <p>{t("ES_TQM_TEST_PARAMS_FAIL_MESSAGE_PRI")}</p>
                <p>{t("ES_TQM_TEST_PARAMS_FAIL_MESSAGE_SECND")}</p>
              </>
            )
          }
        />
      ) : null}
      <SubmitBar label={t("ES_TQM_TEST_BACK_BUTTON")} onSubmit={() => history.goBack()} style={{ marginBottom: "12px" }} />
    </>
  ) : null;
}

export default ParameterReadings;
