import { EmployeeModuleCard, ArrowRightInbox, ShippingTruck } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useTranslation } from "react-i18next";

const ROLES = {
  TQM:["EMPLOYEE"]
};

// Mukta Overrriding the Works Home screen card
const TqmCard = () => {
  // if (!Digit.Utils.didEmployeeHasAtleastOneRole(Object.values(ROLES).flatMap((e) => e))) {
  //   return null;
  // }

  const { t } = useTranslation();
  const tenantId = Digit.ULBService.getCurrentTenantId();

  let links = [
    {
      label: t("test"),
      link: `/${window?.contextPath}/employee/workbench/manage-master-data`,
      roles: ["EMPLOYEE"],
    },
  ];

  // links = links.filter((link) => (link?.roles && link?.roles?.length > 0 ? Digit.Utils.didEmployeeHasAtleastOneRole(link?.roles) : true));

  const propsForModuleCard = {
    Icon: <ShippingTruck />,
    moduleName: t("ACTION_TEST_TQM"),
    kpis: [
      {
        count:  '-' ,
        label: t('TOTAL_FSM'),
        link: `/${window?.contextPath}/employee/fsm/inbox`,
      },
    ],
    links: links,
  };
  return <EmployeeModuleCard {...propsForModuleCard} />;
};

export default TqmCard;
