import React, { Fragment, useEffect, useState } from "react";
import { ViewComposer, Toast, Loader } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";
import { useLocation } from "react-router-dom";
import TestWFActions from "./TestWFActions";
import { useQueryClient } from "react-query";

function TestDetails() {
  const { t } = useTranslation();
  const location = useLocation();
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const searchParams = new URLSearchParams(location.search);
  const id = searchParams.get("id");
  const businessService = "PQM";
  const [status, setStatus] = useState(null);
  const [showToast, setShowToast] = useState(null);
  const [workflowDetails, setWorkflowDetails] = useState(null);
  const [nextAction, setNextAction] = useState(null);
  const queryClient = useQueryClient();

  const { isLoading, data: testData, revalidate, isFetching, refetch } = Digit.Hooks.tqm.useViewTestResults({
    t: t,
    id: id,
    tenantId: tenantId,
    config: {
      select: (data) => ({
        cards: [
          {
            sections: [
              {
                type: "DATA",
                cardHeader: { value: t("ES_TQM_TEST_DETAILS_HEADING"), inlineStyles: { marginTop: 0 } },
                values: data.details,
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
      }),
    },
  });

  let { isLoading: isWFLoading, isError: isWFError, data: WFData } = Digit.Hooks.useWorkflowDetailsWorks({
    tenantId: tenantId,
    id: id,
    moduleCode: businessService,
    config: {
      enabled: true,
      cacheTime: 0,
    },
  });

  const { mutate } = Digit.Hooks.tqm.useTestUpdate(tenantId);

  const closeToast = () => {
    setShowToast(null);
  };

  useEffect(() => {
    if (WFData && !isWFLoading) {
      setWorkflowDetails(WFData);
      setNextAction(WFData?.actionState?.applicationStatus);
    }
  }, [WFData, isWFLoading]);

  const submitAction = (data) => {
    mutate(data, {
      onError: (error, variables) => {
        setShowToast({ key: "error", action: error });
        setTimeout(closeToast, 5000);
      },
      onSuccess: (data, variables) => {
        setShowToast({ key: "success", action: "ES_TQM_STATUS_UPDATED_SUCCESSFULLY" });
        setTimeout(closeToast, 5000);
        queryClient.invalidateQueries("TQM_ADMIN_TEST_RESULTS");
        queryClient.invalidateQueries("workFlowDetailsWorks");
        refetch();
      },
    });
  };

  if (isLoading || isWFLoading) {
    return <Loader />;
  }
  return (
    <>
      {!isLoading && <ViewComposer data={testData} isLoading={isLoading} />}
      {workflowDetails && nextAction && (
        <TestWFActions
          id={id}
          t={t}
          WFData={workflowDetails}
          actionData={workflowDetails?.nextActions?.[0]}
          actionState={workflowDetails?.actionState?.applicationStatus}
          submitAction={submitAction}
        />
      )}
      {showToast && (
        <Toast error={showToast.key === "error" ? true : false} label={t(showToast.key === "success" ? `ES_TQM_STATUS_UPDATE_SUCCESS` : showToast.action)} onClose={closeToast} />
      )}
    </>
  );
}

export default TestDetails;
