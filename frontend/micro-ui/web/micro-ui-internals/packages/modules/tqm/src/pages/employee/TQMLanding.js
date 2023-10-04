import React, { Fragment } from "react";
import { BackButton, Card, AddNewIcon, InboxIcon, ViewReportIcon, CardText, CardHeader, ULBHomeCard } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import TQMPendingTask from "./TQMPendingTask";

const TQMLanding = () => {
  const { t } = useTranslation();
  const state = Digit.ULBService.getStateId();
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const stateId = Digit.ULBService.getStateId();
  const history = useHistory();
  const module = [
    {
      name: "ES_COMMON_INBOX",
      link: `/${window?.contextPath}/employee/fsm/fstp-inbox`,
      icon: <InboxIcon />,
    },
    {
      name: "ES_FSM_ADD_NEW_BUTTON",
      link: `/${window?.contextPath}/employee/fsm/fstp-add-vehicle`,
      icon: <AddNewIcon />,
    },
  ];

  return (
    <React.Fragment>
      <ULBHomeCard module={module}> </ULBHomeCard>
      <TQMPendingTask />
    </React.Fragment>
  );
};

export default TQMLanding;
