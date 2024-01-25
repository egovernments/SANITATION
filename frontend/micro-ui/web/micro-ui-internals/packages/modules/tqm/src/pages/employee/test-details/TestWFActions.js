import { CardText, FormComposerV2, Loader, Modal, WarningIcon } from "@egovernments/digit-ui-react-components";
import React, { Fragment, useEffect, useState } from "react";
import { updateConfig } from "./config/updateTestConfig";
import { testResultsConfig } from "./config/testResultsConfig";
import _ from "lodash";

function TestWFActions({ id, t, WFData, actionData, actionState, submitAction, testDetailsData, isDataLoading }) {
  const [showPopUp, setshowPopUp] = useState(null);
  const [config, setConfig] = useState(null);
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const { isLoading: istestLabLoading, data: testLabs } = Digit.Hooks.tqm.useCustomMDMSV2({
    tenantId: tenantId,
    schemaCode: "PQM.QualityTestLab",
  });

  const UpdateTestSessionScheduled = Digit.Hooks.useSessionStorage("UPDATE_TEST_SESSION_SCHEDULED", {});
  const [sessionFormDataScheduled,setSessionFormDataScheduled, clearSessionFormDataScheduled] = UpdateTestSessionScheduled

  const UpdateTestSessionPendingResults = Digit.Hooks.useSessionStorage("UPDATE_TEST_SESSION_PENDINGRESULTS", {});
  const [sessionFormDataPendingResults,setSessionFormDataPendingResults, clearSessionFormDataPendingResults] = UpdateTestSessionPendingResults
  

  const onFormValueChange = (setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
    if(actionState === "PENDINGRESULTS") {
        if (!_.isEqual(sessionFormDataPendingResults, formData)) {
          const duplicateFormData = _.clone(formData)
          delete duplicateFormData.status
          setSessionFormDataPendingResults({ ...sessionFormDataPendingResults, ...duplicateFormData });
      }
    }else{
        if (!_.isEqual(sessionFormDataScheduled, formData)) {
          setSessionFormDataScheduled({ ...sessionFormDataScheduled, ...formData });
      }
    }
    
  }

  const isMobile = window.Digit.Utils.browser.isMobile();

  // const { isLoading: isDataLoading, data: testDetailsData } = Digit.Hooks.tqm.useSearchTest({ id: id, tenantId: tenantId });

  const { isLoading: istestCriteriaLoading, data: testCriteriaData } = Digit.Hooks.tqm.useCustomMDMSV2({
    tenantId: tenantId,
    schemaCode: "PQM.QualityCriteria",
    changeQueryName: "QualityCriteria",
  });

  const onSubmit = (data) => {
    if (actionState === "PENDINGRESULTS"){
      clearSessionFormDataPendingResults()
    }else{
      clearSessionFormDataScheduled()
    }

    if (actionState === "PENDINGRESULTS" && !showPopUp) {
      setshowPopUp(data);
      return null;
    }
    const { action: workflow } = actionData;
    testDetailsData.workflow = { action: workflow };
    //here add lab as well
    testDetailsData.labAssignedTo = data?.status?.code
    submitAction(testDetailsData);
  };

  useEffect(() => {
    if (testLabs || (testDetailsData && testCriteriaData)) {
      switch (actionState) {
        case "SCHEDULED":
          return setConfig(updateConfig({ t, testLabs }));
        case "PENDINGRESULTS":
          if(testDetailsData && testCriteriaData)
            return setConfig(testResultsConfig({ t, testDetailsData, testCriteriaData }));
          return setConfig(null);
        default:
          return setConfig(null);
      }
    }
  }, [actionState, testLabs, istestLabLoading, istestCriteriaLoading, testCriteriaData, testDetailsData, isDataLoading, WFData]);

  const onConfirm = () => {
    const keyf = Object.keys(showPopUp);
    const tempCriteria = testDetailsData?.testCriteria;
    keyf?.forEach((i) => {
      const _ = tempCriteria?.find((h) => h?.criteriaCode === i);
      if (_) {
        _.resultValue = showPopUp[i];
      }
    });
    if (showPopUp?.documents?.length > 0) {
      const fileStoreIds = showPopUp.documents.map(([, obj]) => obj.fileStoreId.fileStoreId);
      testDetailsData.documents.push({ fileStoreId: fileStoreIds[0] });
    }
    const { action: workflow } = actionData;
    testDetailsData.workflow = { action: workflow };
    submitAction(testDetailsData);
    setshowPopUp(null);
  };

  if (istestCriteriaLoading || isDataLoading || istestLabLoading ) {
    return <Loader />;
  }
  if(!config) {
    return <Loader />
  }

  return (
    <>
      <FormComposerV2
        config={config}
        onSubmit={onSubmit}
        label={t(actionState === "SCHEDULED" ? "ES_TQM_UPDATE_STATUS_BUTTON" : "ES_TQM_SUBMIT_TEST_RESULTS_BUTTON")}
        submitInForm={isMobile ? true : false}
        cardClassName={isMobile ? "testwf" : "employeeCard-override"}
        onFormValueChange={onFormValueChange}
        defaultValues={actionState === "PENDINGRESULTS" ? sessionFormDataPendingResults : sessionFormDataScheduled}
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
