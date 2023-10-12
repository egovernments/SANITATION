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

  testcriteraData.forEach((testItem) => {
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

  return {
    details: [
      {
        key: "Test ID",
        value: testResponse?.id || "N/A",
      },
      {
        key: "Plant name",
        value: testResponse?.plantCode || "N/A",
      },
      {
        key: "Treatment process",
        value: testResponse?.processCode || "N/A",
      },
      {
        key: "Stage",
        value: testResponse?.stageCode || "N/A",
      },
      {
        key: "Test type",
        value: testResponse?.testType || "N/A",
      },
      {
        key: "Test scheduled on",
        value:
          (testResponse?.scheduledDate &&
            `${new Date(testResponse?.scheduledDate).getDate()}/${new Date(testResponse?.scheduledDate).getMonth()}/${new Date(testResponse?.scheduledDate).getFullYear()}`) ||
          "N/A",
      },
      {
        key: "Status",
        value: testResponse?.status || "N/A",
      },
    ],
    documents: testResponse?.documents?.map((i) => {
      return { title: i?.documentUid, value: i?.fileStoreId };
    }),
    tableData: combinedData.length !== 0 ? combinedData : null,
    testSummary: combinedData.length !== 0 ? ["", "", "", "Result summary", !!combinedData.find((i) => i.status !== "PASS") === false ? "PASS" : "FAIL"] : null,
  };
};
