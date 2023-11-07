import React, { useState } from "react";
import { Loader, NotificationComponent, TreatmentQualityIcon } from "@egovernments/digit-ui-react-components";

function TQMPendingTask(props) {
  const tenantId = Digit.ULBService.getCurrentTenantId();

  const requestCriteria = {
    url: "/inbox/v2/_search",
    body: {
      inbox: {
        tenantId,
        processSearchCriteria: {
          businessService: ["PQM"],
          moduleName: "pqm",
          tenantId,
        },
        moduleSearchCriteria: {
          tenantId,
          sortBy: "createdTime",
          sortOrder: "ASC",
        },
        limit: 100,
        offset: 0,
      },
    },
    config: {
      select: (data) => {
        const items = data?.items;

        const tasks = items.map((i) => {
          return {
            icon: <TreatmentQualityIcon />,
            id: i?.ProcessInstance?.businessId,
            title: i?.ProcessInstance?.businessId,
            action: i?.ProcessInstance?.state?.actions?.[0]?.action,
            date: i?.businessObject?.serviceSla,
          };
        });
        return tasks;
      },
    },
  };

  const { isLoading, data: tqm, revalidate, isFetching } = Digit.Hooks.useCustomAPIHook(requestCriteria);

  if (isLoading) return <Loader />;

  return (
    !isLoading &&
    tqm && (
      <NotificationComponent
        heading="Pending Tasks"
        data={tqm}
        viewAllRoute={`/${window?.contextPath}/employee/tqm/inbox`}
        actionRoute={`/${window?.contextPath}/employee/tqm/test-details`}
      />
    )
  );
}

export default TQMPendingTask;
