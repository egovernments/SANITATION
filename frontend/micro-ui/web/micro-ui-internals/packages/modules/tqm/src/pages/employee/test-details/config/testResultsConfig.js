export const testResultsConfig = ({ t }) => {
  return [
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
};
