import { EmployeeModuleCard, ArrowRightInbox, TqmHomePageCardIcon } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useTranslation } from "react-i18next";

const ROLES = {
  TQM:["EMPLOYEE"]
};

// Mukta Overrriding the Works Home screen card
const TqmCard = () => {

  const { t } = useTranslation();
  const tenantId = Digit.ULBService.getCurrentTenantId();

  let links = [
    {
      label: t("TQM_INBOX"),
      link: `/${window?.contextPath}/employee/tqm/inbox`,
      roles: ["EMPLOYEE"],
    },
    {
      label: t("TQM_VIEW_PAST_RESULTS"),
      link: `/${window?.contextPath}/employee/tqm/search-test-results`,
      roles: ["EMPLOYEE"],
    },
    {
      label: t("TQM_VIEW_IOT_READING"),
      link: `/${window?.contextPath}/employee/tqm/search-test-results`,
      roles: ["EMPLOYEE"],
    },
    {
      label: t("TQM_SENSOR_MON"),
      link: `/${window?.contextPath}/employee/tqm/search-devices`,
      roles: ["EMPLOYEE"],
    },
    {
      label: t("TQM_ADD_TEST_RESULT"),
      link: `/${window?.contextPath}/employee/tqm/add-test-result`,
      roles: ["EMPLOYEE"],
    },
    {
      label: t("TQM_DASHBOARD"),
      link: `/${window?.contextPath}/employee/tqm/dashboard`,
      roles: ["EMPLOYEE"],
    },
  ];

  // links = links.filter((link) => (link?.roles && link?.roles?.length > 0 ? Digit.Utils.didEmployeeHasAtleastOneRole(link?.roles) : true));

  const propsForModuleCard = {
    Icon: <TqmHomePageCardIcon />,
    moduleName: t("ACTION_TEST_TQM"),
    kpis: [
      {
        count:  '-' ,
        label: t('TQM_KPI_PENDING_TESTS'),
        link: `/${window?.contextPath}/employee/tqm/inbox`,
      },
      {
        count:  '-' ,
        label: t('TQM_KPI_NEARING_SLA'),
        link: `/${window?.contextPath}/employee/tqm/inbox`,
      },
    ],
    links: links,
  };
  return <EmployeeModuleCard {...propsForModuleCard} />;
};

export default TqmCard;
