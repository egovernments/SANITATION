import { Loader, FormComposerV2, Header, Toast } from "@egovernments/digit-ui-react-components";
import React, { useState, useEffect } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import { addTestConfig } from "./config";
import { createModifiedData } from "./createModifiedData";
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
        setShowToast({ key: "success", label: "TQM_ADD_TEST_SUCCESS" });
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
        defaultValues={{}}
        onSubmit={onSubmit}
        fieldStyle={{ marginRight: 0 }}
        noBreakLine={true}
        cardClassName={"page-padding-fix"}
      />

      {showToast && <Toast error={showToast?.isError} label={showToast?.label} isDleteBtn={"true"} onClose={() => setShowToast(false)} />}
    </div>
  );
};

export default Create;