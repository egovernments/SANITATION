import { CardText, FormComposerV2, Modal, WarningIcon } from "@egovernments/digit-ui-react-components";
import React, { Fragment, useEffect, useState } from "react";
import { updateConfig } from "./config/updateTestConfig";
import { testResultsConfig } from "./config/testResultsConfig";

function TestWFActions({ t, actionData, submitAction }) {
  // 🚧 WIP: DUMMY DATA FOR TEST RESULTS 👇

  const [showPopUp, setshowPopUp] = useState(null);
  const [config, setConfig] = useState(null);

  const onSubmit = (data) => {
    if (actionData === 2 && !showPopUp) {
      setshowPopUp(data);
      return null;
    }
    submitAction(data);
  };

  useEffect(() => {
    switch (actionData) {
      case 1:
        return setConfig(updateConfig({ t }));
      case 2:
        return setConfig(testResultsConfig({ t }));
      default:
        null;
    }
  }, [actionData]);

  const onConfirm = () => {
    submitAction(showPopUp);
  };

  return (
    <>
      <FormComposerV2 config={config} onSubmit={onSubmit} label={t(actionData === 1 ? "ES_TQM_UPDATE_STATUS_BUTTON" : "ES_TQM_SUBMIT_TEST_RESULTS_BUTTON")} submitInForm={true} />
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
