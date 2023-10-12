export const searchTestResultData = async ({ id }) => {
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
  };
};
