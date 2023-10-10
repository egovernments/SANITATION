import React, { Fragment, useState } from "react";
import { ViewComposer, Toast } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";
import TestWFActions from "./TestWFActions";

// 🚧 WIP: DUMMY DATA FOR DETAILS 👇
const data = {
  cards: [
    {
      sections: [
        {
          type: "DATA",
          // sectionHeader: { value: "Section 1", inlineStyles: {} },
          cardHeader: { value: "Card 2", inlineStyles: {} },
          values: [
            {
              key: "key 1",
              value: "value 1",
            },
            {
              key: "key 2",
              value: "value 2",
            },
            {
              key: "key 3",
              value: "value 3",
            },
          ],
        },
        {
          type: "COMPONENT",
          component: "TqmCardReading",
          props: {
            title: "Quality Parameter 1 (in UoM)",
          },
        },
        {
          type: "COMPONENT",
          component: "TqmCardReading",
          props: {
            title: "Quality Parameter 2 (in UoM)",
          },
        },
        {
          type: "COMPONENT",
          component: "TqmCardReading",
          props: {
            title: "Quality Parameter 3 (in UoM)",
          },
        },
      ],
    },
  ],
  apiResponse: {},
  additionalDetails: {},
};

function TestDetails() {
  const [status, setStatus] = useState(null);
  const [showToast, setShowToast] = useState(null);
  const { t } = useTranslation();
  // 🚧 WIP: DUMMY DATA WF 👇
  const [workflowDetails, setWorkflowDetails] = useState(2);
  const closeToast = () => {
    setShowToast(null);
  };

  const submitAction = (data) => {
    console.log("DATA", data);
    // mutate(data, {
    //   onError: (error, variables) => {
    //     setShowToast({ key: "error", action: error });
    //     setTimeout(closeToast, 5000);
    //   },
    //   onSuccess: (data, variables) => {
    //     setShowToast({ key: "success", action: selectedAction });
    //     setTimeout(closeToast, 5000);
    //     queryClient.invalidateQueries("FSM_CITIZEN_SEARCH");
    //     const inbox = queryClient.getQueryData("FUNCTION_RESET_INBOX");
    //     inbox?.revalidate();
    //   },
    // });
  };

  return (
    <>
      <ViewComposer data={data} isLoading={false} />
      {workflowDetails && <TestWFActions t={t} actionData={workflowDetails} submitAction={submitAction} />}
      {showToast && (
        <Toast
          error={showToast.key === "error" ? true : false}
          label={t(showToast.key === "success" ? `ES_FSM_${showToast.action}_UPDATE_SUCCESS` : showToast.action)}
          onClose={closeToast}
        />
      )}
    </>
  );
}

export default TestDetails;
