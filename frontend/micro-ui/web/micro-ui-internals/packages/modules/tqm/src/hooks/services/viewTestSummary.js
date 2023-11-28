export const viewTestSummary = async ({ t, id }) => {
  const response = await Digit.CustomService.getResponse({
    url: "/pqm-service/v1/_search",
    body: {
      pagination: {
        // sortBy: "testId",
        // sortOrder: "ASC",
      },
      testSearchCriteria: {
        testIds: [id],
      },
    },
  });

  const testResponse = response?.tests?.[0];
  const updatedTime = testResponse?.auditDetails?.lastModifiedTime;

  return {
    details: [
      {
        key: "ES_TQM_LABEL_TEST_ID",
        value: testResponse?.testId || "N/A",
      },
      {
        key: "ES_TQM_LABEL_PLANT_NAME",
        value: testResponse?.plantCode ? t(Digit.Utils.locale.getTransformedLocale(`PQM.Plant_${testResponse?.plantCode}`)) : "N/A",
      },
      {
        key: "ES_TQM_LABEL_TREATMENT_PROCESS",
        value: testResponse?.processCode ? t(Digit.Utils.locale.getTransformedLocale(`PQM.Process_${testResponse?.processCode}`)) : "N/A",
      },
      {
        key: "ES_TQM_LABEL_STAGE",
        value: testResponse?.stageCode ? t(Digit.Utils.locale.getTransformedLocale(`PQM.Stage_${testResponse?.stageCode}`)) : "N/A",
      },
      {
        key: "ES_TQM_LABEL_TEST_TYPE",
        value: testResponse?.testType ? t(Digit.Utils.locale.getTransformedLocale(`PQM.TestType_${testResponse?.testType}`)) : "N/A",
      },
      {
        key: t("ES_TQM_LABEL_OUTPUT_TYPE"),
        value: testResponse?.materialCode ? t(Digit.Utils.locale.getTransformedLocale(`PQM.Material_${testResponse?.materialCode}`)) : "N/A",
      },
      {
        key: t("ES_TQM_LABEL_LAB_NAME"),
        value: testResponse?.labAssignedTo ? t(Digit.Utils.locale.getTransformedLocale(`PQM.QualityTestLab_${testResponse?.labAssignedTo}`)) : "N/A",
      },
      {
        key: "ES_TQM_LABEL_TEST_SCHEDULED_ON",
        value:
          (testResponse?.scheduledDate &&
            `${new Date(testResponse?.scheduledDate).getDate()}/${new Date(testResponse?.scheduledDate).getMonth() + 1}/${new Date(testResponse?.scheduledDate).getFullYear()}`) ||
          "N/A",
      },
      {
        key: "ES_TQM_LABEL_STATUS",
        isSla: true,
        isSuccess: testResponse?.status === "PASS" ? true : false,
        value: t(`TQM_TEST_STATUS_${testResponse?.status}`) || "N/A",
      },
    ],
    documents: testResponse?.documents?.map((i) => {
      return { title: i?.title, value: i?.fileStoreId };
    }),
    reading: testResponse?.testCriteria
      ? {
          title: "ES_TQM_TEST_PARAMS_HEADING",
          date: updatedTime ? `${new Date(updatedTime).getDate()}/${new Date(updatedTime).getMonth() + 1}/${new Date(updatedTime).getFullYear()}` : `N/A`,
          readings: testResponse?.testCriteria,
        }
      : null,
    testResponse: testResponse,
  };
};
