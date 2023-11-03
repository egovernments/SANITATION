export const searchTestResultData = async ({ t, id, type, tenantId }) => {
  const response = await Digit.CustomService.getResponse({
    url: "/pqm-service/v1/_search",
    body: {
      pagination: {
        sortBy: "id",
        sortOrder: "ASC",
      },
      testSearchCriteria: {
        ids: [id],
      },
    },
  });
  const testResponse = response?.tests?.[0];

  const testcriteraData = testResponse?.testCriteria;

  const mdmsCriteriaData = await Digit.CustomService.getResponse({
    url: "/mdms-v2/v2/_search",
    body: {
      tenantId,
      MdmsCriteria: {
        tenantId: tenantId,
        schemaCode: "PQM.QualityCriteria",
        isActive: true,
      },
    },
  });

  const combinedData = [];

  testcriteraData?.forEach((testItem) => {
    const matchingMdmsItem = mdmsCriteriaData?.mdms?.find((mdmsItem) => mdmsItem.uniqueIdentifier === testItem.criteriaCode);
    if (matchingMdmsItem) {
      const mergedData = {
        criteriaCode: testItem.criteriaCode,
        qparameter: matchingMdmsItem.data.parameter,
        uom: matchingMdmsItem.data.unit,
        benchmarkValues: matchingMdmsItem.data.benchmarkValues?.[0],
        results: testItem.resultValue,
        status: testItem.resultStatus,
      };
      combinedData.push(mergedData);
    }
  });

  let workflowData;
  let sla = 0;

  if (type !== "adhoc") {
    try{
      workflowData = await Digit.WorkflowService.getDetailsByIdWorks({ tenantId, id, moduleCode: "PQM" });
      sla = Math.round(workflowData?.processInstances?.[0]?.businesssServiceSla / (24 * 60 * 60 * 1000));
    }catch(err){
      console.error("error fetching workflow data")
    }
  }

  return {
    details: [
      {
        key: t("ES_TQM_LABEL_TEST_ID"),
        value: testResponse?.id || "N/A",
      },
      {
        key: t("ES_TQM_LABEL_PLANT_NAME"),
        value: t(Digit.Utils.locale.getTransformedLocale(`PQM.Plant_${testResponse?.plantCode}`)) || "N/A",
      },
      {
        key: t("ES_TQM_LABEL_TREATMENT_PROCESS"),
        value: t(Digit.Utils.locale.getTransformedLocale(`PQM.Process_${testResponse?.processCode}`)) || "N/A",
      },
      {
        key: t("ES_TQM_LABEL_STAGE"),
        value: t(Digit.Utils.locale.getTransformedLocale(`PQM.Stage_${testResponse?.stageCode}`)) || "N/A",
      },
      {
        key: t("ES_TQM_LABEL_TEST_TYPE"),
        value: t(Digit.Utils.locale.getTransformedLocale(`PQM.TestType_${testResponse?.testType}`)) || "N/A",
      },
      {
        key: t("ES_TQM_LABEL_TEST_SCHEDULED_ON"),
        value:
          (testResponse?.scheduledDate &&
            `${new Date(testResponse?.scheduledDate).getDate()}/${new Date(testResponse?.scheduledDate).getMonth() + 1}/${new Date(testResponse?.scheduledDate).getFullYear()}`) ||
          "N/A",
      },
      {
        key: t("ES_TQM_LABEL_STATUS"),
        value: t(`TQM_TEST_STATUS_${testResponse?.status}`) || "N/A",
      },
      {
        key: t("ES_TQM_LABEL_SLA"),
        isSla: true,
        isSuccess: Math.sign(sla) === -1 ? false : true,
        value: sla ? sla : "0",
      },
    ],
    documents: testResponse?.documents?.map((i) => {
      return { title: i?.documentUid, value: i?.fileStoreId };
    }),
    tableData: combinedData.length !== 0 ? combinedData : null,
    testSummary:
      combinedData.length !== 0
        ? ["", "", "", t("ES_TQM_LABEL_RESULT_SUMMARY"), !!combinedData.find((i) => i.status !== "PASS") === false ? t("ES_TQM_LABEL_RESULT_PASS") : t("ES_TQM_LABEL_RESULT_FAIL")]
        : null,
    wfStatus: testResponse?.wfStatus,
    testResponse,
  };
};
