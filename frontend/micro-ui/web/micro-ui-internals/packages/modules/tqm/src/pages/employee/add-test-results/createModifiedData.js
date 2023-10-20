export const createModifiedData = (data) => {
    const modifiedData = [
        {
            "tenantId": "pg.citya",
            "plantCode": data?.plantCode?.code,
            "processCode": data?.processCode?.code,
            "stageCode": data?.stageCode?.code,
            "materialCode": data?.materialCode?.code,
            "testCriteria": data?.QualityParameter?.map ? Object.keys(data?.QualityParameter)?.map((criteriaCode) => {
                if (criteriaCode !== "documents") {
                    return {
                        criteriaCode: criteriaCode,
                        resultValue: data?.QualityParameter[criteriaCode],
                        isActive: true
                    }
                }
            }) : null,
            "testType": null,
            "scheduledDate": null,
            "isActive": null,
            "documents": [
                {
                    "id": null,
                    "documentType": null,
                    "fileStore": null,
                    "documentUid": data?.QualityParameter?.document,
                    "additionalDetails": {}
                }
            ],
            "additionalDetails": {},
            "workflow": null
        }
    ]
    return modifiedData;
};