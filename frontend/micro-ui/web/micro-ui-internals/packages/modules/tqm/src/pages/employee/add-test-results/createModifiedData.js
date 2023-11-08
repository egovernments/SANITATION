export const createModifiedData = (data) => {
    const modifiedData = [
        {
            "tenantId": "pg.citya",
            "plantCode": data?.plantCode?.code,
            "processCode": data?.processCode?.code,
            "stageCode": data?.stageCode?.code,
            "materialCode": data?.materialCode?.code,
            "testCriteria": Object.keys(data?.QualityParameter)
                .filter(criteriaCode => criteriaCode.toLowerCase() !== "document")
                .map(criteriaCode => ({
                    criteriaCode: criteriaCode,
                    resultValue: data?.QualityParameter[criteriaCode],
                    isActive: true
                })),
            "testType": "LAB_ADHOC",
            "scheduledDate": null,
            "isActive": null,
            "documents": data?.QualityParameter?.document
                ? [
                    {
                        "fileStoreId": data?.QualityParameter?.document,
                    }
                ]
                : [],
            "additionalDetails": {},
            "workflow": null
        }
    ]
    return modifiedData;
};