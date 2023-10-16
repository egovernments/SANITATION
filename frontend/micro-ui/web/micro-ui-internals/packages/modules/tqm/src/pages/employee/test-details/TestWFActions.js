import { CardText, FormComposerV2, Modal, WarningIcon } from "@egovernments/digit-ui-react-components";
import React, { Fragment, useEffect, useState } from "react";
import { updateConfig } from "./config/updateTestConfig";
import { testResultsConfig } from "./config/testResultsConfig";

function TestWFActions({ id, t, WFData, actionData, actionState, submitAction }) {
  const [showPopUp, setshowPopUp] = useState(null);
  const [config, setConfig] = useState(null);
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const { isLoading: istestLabLoading, data: testLabs } = Digit.Hooks.tqm.useCustomMDMSV2({
    tenantId: tenantId,
    schemaCode: "PQM.QualityTestLab",
  });

  const { isLoading: isDataLoading, data: testDetailsData } = Digit.Hooks.tqm.useSearchTest({ id: id, tenantId: tenantId });

  const onSubmit = (data) => {
    if (actionState === "PENDINGRESULTS" && !showPopUp) {
      setshowPopUp(data);
      return null;
    }
    const { action: workflow } = actionData;
    testDetailsData.workflow = workflow;
    submitAction(testDetailsData);
  };

  useEffect(() => {
    switch (actionState) {
      case "SCHEDULED":
        return setConfig(updateConfig({ t, testLabs }));
      case "PENDINGRESULTS":
        return setConfig(testResultsConfig({ t }));
      default:
        null;
    }
  }, [actionState, testLabs, istestLabLoading]);

  const onConfirm = () => {
    const { action: workflow } = actionData;
    testDetailsData.workflow = workflow;
    testDetailsData.additionalDetails.testData = showPopUp;
    submitAction(testDetailsData);
  };

  return (
    <>
      <FormComposerV2
        config={config}
        onSubmit={onSubmit}
        label={t(actionState === "SCHEDULED" ? "ES_TQM_UPDATE_STATUS_BUTTON" : "ES_TQM_SUBMIT_TEST_RESULTS_BUTTON")}
        submitInForm={true}
      />
      {showPopUp && (
        <Modal
          popmoduleClassName="tqm-pop-module"
          popupModuleActionBarClass="tqm-pop-action"
          style={{ flex: 1 }}
          popupMainModuleClass="tqm-pop-main"
          headerBarMain={
            <h1 className="tqm-modal-heading">
              <WarningIcon /> {t("ES_TQM_UPDATE_MODAL_HEADER")}
            </h1>
          }
          actionCancelLabel={t("ES_TQM_UPDATE_MODAL_BACK")}
          actionCancelOnSubmit={() => {
            setshowPopUp(false);
          }}
          actionSaveLabel={t("ES_TQM_UPDATE_MODAL_SUBMIT")}
          actionSaveOnSubmit={onConfirm}
          customTheme="v-tqm"
          formId="modal-action"
        >
          <div>
            <CardText style={{ margin: 0 }}>{t("ES_TQM_UPDATE_MODAL_TEXT") + " "}</CardText>
          </div>
        </Modal>
      )}
    </>
  );
}

export default TestWFActions;
