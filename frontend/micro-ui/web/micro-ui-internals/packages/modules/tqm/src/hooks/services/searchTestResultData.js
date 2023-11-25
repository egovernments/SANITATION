export const searchTestResultData = async ({ t, id, type, tenantId }) => {
  const userInfo = Digit.UserService.getUser();
  const userRoles = userInfo.info.roles.map((roleData) => roleData.code);

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
  console.log("testResponse", testResponse);
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
    try {
      const currentDate = new Date();
      const targetTimestamp = testResponse?.scheduledDate;
      const targetDate = new Date(targetTimestamp);
      const remainingSLA = targetDate - currentDate;
      sla = targetTimestamp ? Math.round(remainingSLA / (24 * 60 * 60 * 1000)) : 0;
    } catch (err) {
      console.error("error fetching workflow data");
    }
  }

  return {
    details: userRoles.includes("PQM_ADMIN")
      ? [
          {
            key: t("ES_TQM_LABEL_TEST_ID"),
            value: testResponse?.testId || "N/A",
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
            key: t("ES_TQM_LABEL_TEST_TYPE"),
            value: t(Digit.Utils.locale.getTransformedLocale(`PQM.QualityTestLab_${testResponse?.labAssignedTo}`)) || "N/A",
          },
          {
            key: t("ES_TQM_LABEL_TEST_SCHEDULED_ON"),
            value:
              (testResponse?.scheduledDate &&
                `${new Date(testResponse?.scheduledDate).getDate()}/${new Date(testResponse?.scheduledDate).getMonth() + 1}/${new Date(
                  testResponse?.scheduledDate
                ).getFullYear()}`) ||
              "N/A",
          },
        ]
      : [
          {
            key: t("ES_TQM_LABEL_TEST_ID"),
            value: testResponse?.testId || "N/A",
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
                `${new Date(testResponse?.scheduledDate).getDate()}/${new Date(testResponse?.scheduledDate).getMonth() + 1}/${new Date(
                  testResponse?.scheduledDate
                ).getFullYear()}`) ||
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
            value: sla ? `${sla} ${t("COMMON_DAYS")}` : `0 ${t("COMMON_DAYS")}`,
          },
        ],
    documents: testResponse?.documents?.map((i) => {
      return { title: i?.documentUid, value: i?.fileStoreId };
    }),
    tableData: combinedData.length !== 0 ? combinedData : null,
    testSummary:
      combinedData.length !== 0
        ? [
            "",
            "",
            "",
            t("ES_TQM_LABEL_RESULT_SUMMARY"),
            testResponse.status === "PASS" ? t("ES_TQM_LABEL_RESULT_PASS") : testResponse.status === "FAIL" ? t("ES_TQM_LABEL_RESULT_FAIL") : t("TQM_TEST_STATUS_PENDING"),
          ]
        : null,
    wfStatus: testResponse?.wfStatus,
    testResponse,
  };
};
