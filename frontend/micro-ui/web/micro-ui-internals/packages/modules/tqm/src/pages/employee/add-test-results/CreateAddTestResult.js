import { Loader, FormComposerV2, Header, Toast } from "@egovernments/digit-ui-react-components";
import React, { useState, useEffect } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";


export const newConfig = [
  {
    body: [
      {
        isMandatory: true,
        key: "plant_name",
        type: "radioordropdown",
        label: "TQM_PLANT_NAME",
        disable: false,
        populators: {
          name: "plant_name",
          optionsKey: "name",
          error: "ES_TQM_REQUIRED",
          required: true,
          mdmsConfig: {
            masterName: "GenderType",
            moduleName: "common-masters",
            localePrefix: "COMMON_GENDER",
          },
        },
      },
      {
        isMandatory: true,
        key: "process",
        type: "radioordropdown",
        label: "TQM_TREATMENT_PROCESS",
        disable: false,
        populators: {
          name: "process",
          optionsKey: "name",
          error: "ES_TQM_REQUIRED",
          required: true,
          mdmsConfig: {
            masterName: "GenderType",
            moduleName: "common-masters",
            localePrefix: "COMMON_GENDER",
          },
        },
      },
      {
        isMandatory: true,
        key: "stage",
        type: "radioordropdown",
        label: "TQM_PROCESS_STAGE",
        disable: false,
        populators: {
          name: "stage",
          optionsKey: "name",
          error: "ES_TQM_REQUIRED",
          required: true,
          mdmsConfig: {
            masterName: "GenderType",
            moduleName: "common-masters",
            localePrefix: "COMMON_GENDER",
          },
        },
      },
      {
        isMandatory: true,
        key: "output",
        type: "radioordropdown",
        label: "TQM_OUTPUT_TYPE",
        disable: false,
        populators: {
          name: "output",
          optionsKey: "name",
          error: "ES_TQM_REQUIRED",
          required: true,
          mdmsConfig: {
            masterName: "GenderType",
            moduleName: "common-masters",
            localePrefix: "COMMON_GENDER",
          },
        },
      }
    ],
  },
  {
    head: "ES_TQM_QUALITY_PARAMETERS",
    body: [
      {
        label: "ES_TQM_TEST_PARAM_PH",
        isMandatory: false,
        key: "pH",
        type: "number",
        disable: false,
        populators: { name: "qualityParameter.pH", error: "ES_TQM_TEST_PARAM_ERROR_MESSAGE", validation: { pattern: /^\d+$/ } },
      },
      {
        label: "ES_TQM_TEST_PARAM_BOD",
        isMandatory: false,
        key: "BOD",
        type: "number",
        disable: false,
        populators: { name: "qualityParameter.BOD", error: "ES_TQM_TEST_PARAM_ERROR_MESSAGE", validation: { pattern: /^\d+$/ } },
      },
      {
        label: "ES_TQM_TEST_PARAM_COD",
        isMandatory: false,
        key: "COD",
        type: "number",
        disable: false,
        populators: { name: "qualityParameter.COD", error: "ES_TQM_TEST_PARAM_ERROR_MESSAGE", validation: { pattern: /^\d+$/ } },
      },
      {
        label: "ES_TQM_TEST_PARAM_TSS",
        isMandatory: false,
        key: "TSS",
        type: "number",
        disable: false,
        populators: { name: "qualityParameter.TSS", error: "ES_TQM_TEST_PARAM_ERROR_MESSAGE", validation: { pattern: /^\d+$/ } },
      },
      {
        label: "ES_TQM_TEST_PARAM_FCOLI",
        isMandatory: false,
        key: "F.Coli",
        type: "number",
        disable: false,
        populators: { name: "qualityParameter.Coli", error: "ES_TQM_TEST_PARAM_ERROR_MESSAGE", validation: { pattern: /^\d+$/ } },
      },
      {
        label: "ES_TQM_TEST_PARAM_ATTACH_DOCUMENTS",
        "isMandatory": false,
        "key": "basicDetails_photograph",
        "type": "multiupload",
        "preProcess": {
          "convertStringToRegEx": ["populators.allowedFileTypes"]
        },
        "populators": {
          "name": "basicDetails_photograph",
          "allowedMaxSizeInMB": 2,
          "maxFilesAllowed": 1,
          "allowedFileTypes": /(jpg|jpeg|png|pdf)$/i,
          "containerStyles": {
            "marginBottom": "1rem"
          }
        }
      }
    ],
  }
];

const Create = () => {
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const { t } = useTranslation();
  const history = useHistory();
  const [showToast, setShowToast] = useState(false);
  console.log(showToast, "hee")

  const onSubmit = (data) => {
    console.log(data, "data");
    const qualityParams = data.qualityParameter;
    const isAtLeastOneDefined = Object.keys(qualityParams).some(key => qualityParams[key] );
    if (!isAtLeastOneDefined) {
      setShowToast({
        label: t('ES_TQM_ATLEAST_ONE_PARAMETER'),
        isError: true
      });
    }
  };

  const closeToast = () => {
    setShowToast(false);
  };

  //remove Toast after 3s
  useEffect(() => {
    if (showToast) {
      setTimeout(() => {
        closeToast();
      }, 3000)
    }
  }, [showToast])

  const configs = newConfig;

  return (
    <div>
      <Header>{t("TQM_ADD_TEST_RESULT")}</Header>
      <FormComposerV2
        showMultipleCardsWithoutNavs={true}

        label="ES_TQM_SUBMIT_TEST_RESULTS_BUTTON"
        config={configs.map((config) => {
          return {
            ...config,
            body: config.body.filter((a) => !a.hideInEmployee),
          };
        })}
        defaultValues={{}}
        onSubmit={onSubmit}
        fieldStyle={{ marginRight: 0 }}
        noBreakLine ={true}
      />
      {showToast && <Toast error={showToast.isError} label={showToast.label} isDleteBtn={true} onClose={closeToast} />}
    </div>
  );
};

export default Create;