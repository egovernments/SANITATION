import { Loader, FormComposerV2, Header, Toast } from "@egovernments/digit-ui-react-components";
import React, { useState, useEffect } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import { addTestConfig } from "./config";
import { createModifiedData } from "./createModifiedData";
import _ from "lodash"
const Create = () => {
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const { t } = useTranslation();
  const history = useHistory();
  const [showToast, setShowToast] = useState(false);
  const { mutate } = Digit.Hooks.tqm.useCreateTest(tenantId);
  const closeToast = () => {
    setTimeout(() => {
      setShowToast(false);
    }, 5000)
  };

  const CreateAdhocTestSession = Digit.Hooks.useSessionStorage("CREATE_ADHOC_TEST", {});
  const [sessionFormData,setSessionFormData, clearSessionFormData] = CreateAdhocTestSession;

  const onFormValueChange = (setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
    if (!_.isEqual(sessionFormData, formData)) {
        if(Object.keys(sessionFormData)?.length===0){
          setSessionFormData({ ...formData });
        }else{
        setSessionFormData({ ...sessionFormData, ...formData });
        }
    }
}

  const onSubmit = async (data) => {
    const qualityParams = data.QualityParameter;
    if (!qualityParams) {
      setShowToast({
        label: t('ES_TQM_ATLEAST_ONE_PARAMETER'),
        isError: true
      });
      closeToast();
    }
    else {
      for (const key in qualityParams) {
        if (qualityParams[key] === '') {
          delete qualityParams[key];
        }
      }
      let isonlyDocument = false;
      for (const key in qualityParams) {
        if (qualityParams[key] !== null && key != "document") {
          isonlyDocument = true;
          break;
        }
      }
      if (!isonlyDocument) {
        setShowToast({
          label: t('ES_TQM_ATLEAST_ONE_PARAMETER'),
          isError: true
        });
        closeToast();
        return ;
      }
    }
    const modifiedData = createModifiedData(data);
    await mutate(modifiedData, {
      onError: (error, variables) => {
        setShowToast({
          label: error.toString(),
          isError: true
        });
        setTimeout(() => {
          setShowToast(false);
        }, 5000);
      },
      onSuccess: async (data) => {
        setShowToast({ key: "success", label: t("TQM_ADD_TEST_SUCCESS") });
        setTimeout(() => {
          closeToast();
          history.push(`/sanitation-ui/employee/tqm/view-test-results?id=${data.tests[0].testId}&type=adhoc`);
        }, 5000);
      },
    });
  };

  return (
    <div>

      <Header>{t("TQM_ADD_TEST_RESULT")}</Header>
      <FormComposerV2
        showMultipleCardsWithoutNavs={true}
        label="ES_TQM_SUBMIT_TEST_RESULTS_BUTTON"
        config={addTestConfig.map((config) => {
          return {
            ...config,

          };
        })}
        defaultValues={sessionFormData}
        onSubmit={onSubmit}
        fieldStyle={{ marginRight: 0 }}
        noBreakLine={true}
        cardClassName={"page-padding-fix"}
        onFormValueChange={onFormValueChange}
      />

      {showToast && <Toast error={showToast?.isError} label={showToast?.label} isDleteBtn={"true"} onClose={() => setShowToast(false)} />}
    </div>
  );
};

export default Create;