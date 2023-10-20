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
  const { mutate } = Digit.Hooks.tqm.useCreate(tenantId);
  const closeToast = () => {
    setTimeout(() => {
      setShowToast(false);
    }, 5000)
  };

  const onSubmit = async (data) => {
    const qualityParams = data.QualityParameter;
    
    if (qualityParams === undefined) {
      setShowToast({
        label: t('ES_TQM_ATLEAST_ONE_PARAMETER'),
        isError: true
      });
      closeToast();
    }
    else {
      let isonlyDocument = false;
      for (const key in qualityParams) {
        if (qualityParams[key] !== null && key!="document") {
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
        history.push({
          pathname: "/sanitation-ui/employee/tqm/view-test-results",
          // state: { responseData: getData }
        });
      }
    });
    // history.push({
    //   pathname: "/sanitation-ui/employee/tqm/view-test-results",
    //   state: { responseData: getData }
    // });
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
            // body: config.body.filter((a) => !a.hideInEmployee),
          };
        })}
        defaultValues={{}}
        onSubmit={onSubmit}
        fieldStyle={{ marginRight: 0 }}
        noBreakLine={true}
      />
      {console.log("SHOWTOAST", showToast)}
      {showToast && <Toast error={showToast.isError} label={showToast.label} isDleteBtn={"true"} onClose={() => setShowToast(false)} style={{ bottom: "8%" }} />}
    </div>
  );
};

export default Create;
