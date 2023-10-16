export const searchTestResultData = async ({ id, tenantId }) => {
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
        uom: matchingMdmsItem.data.units,
        benchmarkValues: matchingMdmsItem.data.benchmarkValues?.[0],
        results: testItem.value,
        status: testItem.status,
      };
      combinedData.push(mergedData);
    }
  });

  const workflowData = await Digit.WorkflowService.getDetailsByIdWorks({ tenantId, id, moduleCode: "PQM" });
  const sla = Math.round(workflowData?.processInstances?.[0]?.stateSla / (24 * 60 * 60 * 1000));

  return {
    details: [
      {
        key: "ES_TQM_LABEL_TEST_ID",
        value: testResponse?.id || "N/A",
      },
      {
        key: "ES_TQM_LABEL_PLANT_NAME",
        value: Digit.Utils.locale.getTransformedLocale(`PQM.Plant_${testResponse?.plantCode}`) || "N/A",
      },
      {
        key: "ES_TQM_LABEL_TREATMENT_PROCESS",
        value: Digit.Utils.locale.getTransformedLocale(`PQM.Process_${testResponse?.processCode}`) || "N/A",
      },
      {
        key: "ES_TQM_LABEL_STAGE",
        value: Digit.Utils.locale.getTransformedLocale(`PQM.Stage_${testResponse?.stageCode}`) || "N/A",
      },
      {
        key: "ES_TQM_LABEL_TEST_TYPE",
        value: Digit.Utils.locale.getTransformedLocale(`PQM.TestType_${testResponse?.testType}`) || "N/A",
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
        value: testResponse?.status || "N/A",
      },
      {
        key: "ES_TQM_LABEL_SLA",
        isSla: true,
        isSuccess: Math.sign(sla) === -1 ? false : true,
        value: sla ? sla : "N/A",
      },
    ],
    documents: testResponse?.documents?.map((i) => {
      return { title: i?.documentUid, value: i?.fileStoreId };
    }),
    tableData: combinedData.length !== 0 ? combinedData : null,
    testSummary: combinedData.length !== 0 ? ["", "", "", "ES_TQM_LABEL_RESULT_SUMMARY", !!combinedData.find((i) => i.status !== "PASS") === false ? "ES_TQM_LABEL_RESULT_PASS" : "ES_TQM_LABEL_RESULT_FAIL"] : null,
    wfStatus: testResponse?.wfStatus,
  };
};
