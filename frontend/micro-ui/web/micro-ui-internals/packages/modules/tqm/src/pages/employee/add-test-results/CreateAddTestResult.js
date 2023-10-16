import { Loader, FormComposerV2, Header, Toast } from "@egovernments/digit-ui-react-components";
import React, { useState, useEffect } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import { addTestConfig } from "./config";

const Create = () => {
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const { t } = useTranslation();
  const history = useHistory();
  const [showToast, setShowToast] = useState(false);

  const closeToast = () => {
      setTimeout(() => {
        setShowToast(false);
      }, 3000)
  };

  const onSubmit = (data) => {
    console.log(data, "data");
    const qualityParams = data.qualityParameter;
    const isAtLeastOneDefined = Object.keys(qualityParams).some(key => qualityParams[key] );
    if (!isAtLeastOneDefined) {
      setShowToast({
        label: t('ES_TQM_ATLEAST_ONE_PARAMETER'),
        isError: true
      });
      closeToast();
    }
  };

  return (
    <div>
      <Header>{t("TQM_ADD_TEST_RESULT")}</Header>
      <FormComposerV2
        showMultipleCardsWithoutNavs={true}

        label="ES_TQM_SUBMIT_TEST_RESULTS_BUTTON"
        config={addTestConfig.map((config) => {
          return {
            ...config,
            body: config.body.filter((a) => !a.hideInEmployee),
          };
        })}
        defaultValues={{}}
        onSubmit={onSubmit}
        fieldStyle={{ marginRight: 0 }}
        noBreakLine ={true}
      />
      {showToast && <Toast error={showToast.isError} label={showToast.label} isDleteBtn={true} onClose={()=>setShowToast(false)} />}
    </div>
  );
};

export default Create;




// let inputData = {
//   "plantCode": {
//       "code": "MALE",
//       "active": true,
//       "name": "COMMON_GENDER_MALE"
//   },
//   "processCode": {
//       "code": "MALE",
//       "active": true,
//       "name": "COMMON_GENDER_MALE"
//   },
//   "stageCode": {
//       "code": "MALE",
//       "active": true,
//       "name": "COMMON_GENDER_MALE"
//   },
//   "materialCode": {
//       "code": "MALE",
//       "active": true,
//       "name": "COMMON_GENDER_MALE"
//   },
//   "qualityParameter": {
//       "pH": "6"
//   },
//   "basicDetails_photograph": []
// };
// // Convert the input data to the desired format
// let outputData = {
//   "tests": [
//       {
//           // "id": "3",
//           // "tenantId": "pb.amritsar",
//           "plantCode": data.plantCode.code,
//           "processCode": data.processCode.code,
//           "stageCode": data.stageCode.code,
//           "materialCode": data.materialCode.code,
//           // "wfStatus": "APPLIED",
//           // "testType": "LAB",
//           "testCriteria": [
//               {
//                   "criteriaCode": "PHGRT7",
//                   "value": parseFloat(inputData.qualityParameter.pH),
//                   "isActive": true
//               }
//           ],
//           "isActive": true,
//           "documents": [
//               {
//                   "id": "124",
//                   "documentType": "string",
//                   "fileStore": "string",
//                   "documentUid": "string",
//                   "additionalDetails": {}
//               }
//           ],
//           "additionalDetails": {}
//       }
//   ]
// };

