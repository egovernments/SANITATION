import React, { Fragment, useState } from "react";
import { Button, Card, SubmitBar, CardHeader, ViewComposer, Dropdown, FormComposerV2 } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";

// 🚧 WIP: DUMMY DATA FOR DETAILS 👇
const data = {
  cards: [
    {
      sections: [
        {
          type: "DATA",
          // sectionHeader: { value: "Section 1", inlineStyles: {} },
          cardHeader: { value: "Card 2", inlineStyles: {} },
          values: [
            {
              key: "key 1",
              value: "value 1",
            },
            {
              key: "key 2",
              value: "value 2",
            },
            {
              key: "key 3",
              value: "value 3",
            },
          ],
        },
        {
          type: "COMPONENT",
          component: "TqmCardReading",
          props: {
            title: "Quality Parameter 1 (in UoM)",
          },
        },
        {
          type: "COMPONENT",
          component: "TqmCardReading",
          props: {
            title: "Quality Parameter 2 (in UoM)",
          },
        },
        {
          type: "COMPONENT",
          component: "TqmCardReading",
          props: {
            title: "Quality Parameter 3 (in UoM)",
          },
        },
      ],
    },
  ],
  apiResponse: {},
  additionalDetails: {},
};

function TestDetails() {
  const [status, setStatus] = useState(null);
  const { t } = useTranslation();
  // 🚧 WIP: DUMMY DATA FOR UPDATE STATUS 👇
  const updateConfig = [
    {
      head: t("ES_TQM_SELECT_SAMPLE_TO_LAB_LABEL"),
      body: [
        {
          label: t("ES_TQM_SELECT_LAB_LABEL"),
          isMandatory: true,
          key: "status",
          type: "dropdown",
          disable: false,
          populators: {
            name: "status",
            optionsKey: "name",
            error: t("ES_TQM_SELECT_LAB_LABEL_ERROR"),
            required: true,
            // 🚧 WIP: DUMMY DATA FOR UPDATE STATUS 👇
            options: [{ name: "djhsdkj" }, { name: "34734786" }],
            // 🚧 WIP: way to call option from mdms 👇
            //   mdmsConfig: {
            //     masterName: "ProjectType",
            //     moduleName: "works",
            //     localePrefix: "COMMON_MASTERS"
            // }
          },
        },
      ],
    },
  ];

  // 🚧 WIP: DUMMY DATA FOR TEST RESULTS 👇
  const configs = [
    {
      head: t("ES_TQM_ADD_TEST_RESULTS_TITLE"),
      body: [
        {
          label: t("ES_TQM_TEST_PARAM_PH"),
          isMandatory: true,
          type: "number",
          disable: false,
          populators: { name: "pH", error: t("ES_TQM_TEST_PARAM_ERROR_MESSAGE"), validation: { min: 0, max: 10 } },
        },
        {
          label: t("ES_TQM_TEST_PARAM_BOD"),
          isMandatory: true,
          type: "number",
          disable: false,
          populators: { name: "bod", error: t("ES_TQM_TEST_PARAM_ERROR_MESSAGE"), validation: { min: 0, max: 10 } },
        },
        {
          label: t("ES_TQM_TEST_PARAM_COD"),
          isMandatory: true,
          type: "number",
          disable: false,
          populators: { name: "cod", error: t("ES_TQM_TEST_PARAM_ERROR_MESSAGE"), validation: { min: 0, max: 10 } },
        },
        {
          label: t("ES_TQM_TEST_PARAM_TSS"),
          isMandatory: true,
          type: "number",
          disable: false,
          populators: { name: "tss", error: t("ES_TQM_TEST_PARAM_ERROR_MESSAGE"), validation: { min: 0, max: 10 } },
        },
        {
          label: t("ES_TQM_TEST_PARAM_FCOLI"),
          isMandatory: true,
          type: "number",
          disable: false,
          populators: { name: "fcoli", error: t("ES_TQM_TEST_PARAM_ERROR_MESSAGE"), validation: { min: 0, max: 10 } },
        },
        // {
        //   label: "Attach Document",
        //   type: "documentUpload",
        //   withoutLabel: true,
        //   module: "UPDATE TEST",
        //   error: "TEST_REQUIRED_ERR",
        //   name: "documents",
        //   customClass: "",
        //   localePrefix: "TQM",
        // },
        {
          type: "multiupload",
          label: t("ES_TQM_TEST_PARAM_ATTACH_DOCUMENTS"),
          populators: {
            name: "documents",
            allowedMaxSizeInMB: 5,
            maxFilesAllowed: 1,
            customClass: "upload-margin-bottom",
            errorMessage: t("ES_TQM_TEST_PARAM_ATTACH_DOCUMENTS_ERROR_MSG"),
            allowedFileTypes: /(.*?)(pdf|jpg|png)$/i,
            // hintText: t("WORKS_DOC_UPLOAD_HINT"),
            // showHintBelow: true,
            // hideInForm: !fetchIsShow("upload"),
          },
        },
      ],
    },
  ];

  const onUpdateStatus = ({ status }) => {
    setStatus(status);
  };

  // 🚧 WIP: NEED TO ADD LOGIN FOR SUBMIT HERE 👇
  const onSubmit = (data) => {};

  return (
    <>
      <ViewComposer data={data} isLoading={false} />
      {!status && <FormComposerV2 config={updateConfig} onSubmit={onUpdateStatus} label={t("ES_TQM_UPDATE_STATUS_BUTTON")} submitInForm={true} />}
      {status && <FormComposerV2 config={configs} onSubmit={onSubmit} label={t("ES_TQM_SUBMIT_TEST_RESULTS_BUTTON")} submitInForm={true} />}
    </>
  );
}

export default TestDetails;
