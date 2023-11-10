import { EmployeeModuleCard, ArrowRightInbox, TqmHomePageCardIcon } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";

const ROLES = {
  plant:["PQM_TP_OPERATOR"],
  ulb:["PQM_ADMIN"]
};

const TqmCard = ({reRoute=true}) => {
  const history = useHistory()
  const isMobile = Digit.Utils.browser.isMobile();
  const isPlantOperatorLoggedIn = Digit.Utils.tqm.isPlantOperatorLoggedIn()
  const { t } = useTranslation();
  const tenantId = Digit.ULBService.getCurrentTenantId();

  const requestCriteria = {
    url: "/inbox/v2/_search",
    body: {
      inbox: {
        tenantId,
        processSearchCriteria: {
          "businessService": [
              "PQM"
          ],
          "moduleName": "pqm",
          tenantId
      },
        moduleSearchCriteria: {
          tenantId,
        },
        limit: 100,
        offset: 0,
      },
    },
    config: {
      enabled: Digit.Utils.didEmployeeHasAtleastOneRole(ROLES.plant) || Digit.Utils.didEmployeeHasAtleastOneRole(ROLES.ulb),
    },
  };

  const { isLoading, data:tqmInboxData } = Digit.Hooks.useCustomAPIHook(requestCriteria);

  let links = [
    {
      label: t("TQM_INBOX"),
      link: `/${window?.contextPath}/employee/tqm/inbox`,
      roles: [...ROLES.plant,ROLES.ulb],
      count:  isLoading ? '-' : tqmInboxData?.totalCount
    },
    {
      label: t("TQM_VIEW_PAST_RESULTS"),
      link: `/${window?.contextPath}/employee/tqm/search-test-results`,
      roles: [...ROLES.plant,ROLES.ulb],
    },
    // {
    //   label: t("TQM_VIEW_IOT_READING"),
    //   link: `/${window?.contextPath}/employee/tqm/search-test-results`,
    //   roles: [...ROLES.plant,ROLES.ulb],
    // },
    // {
    //   label: t("TQM_SENSOR_MON"),
    //   link: `/${window?.contextPath}/employee/tqm/search-devices`,
    //   roles: [...ROLES.plant,ROLES.ulb],
    // },
    {
      label: t("TQM_ADD_TEST_RESULT"),
      link: `/${window?.contextPath}/employee/tqm/add-test-result`,
      roles: [...ROLES.ulb],
    },
    {
      label: t("TQM_DASHBOARD"),
      link: `/${window?.contextPath}/employee/dss/dashboard/pqm`,
      roles: [...ROLES.plant,ROLES.ulb],
    }
  ];

  // links = links.filter((link) => (link?.roles && link?.roles?.length > 0 ? Digit.Utils.didEmployeeHasAtleastOneRole(link?.roles) : true));
  

  const propsForModuleCard = {
    Icon: <TqmHomePageCardIcon />,
    moduleName: t("ACTION_TEST_TQM"),
    kpis: [
      {
        count:  isLoading ? '-' : tqmInboxData?.totalCount ,
        label: t('TQM_KPI_PENDING_TESTS'),
        link: `/${window?.contextPath}/employee/tqm/inbox`,
      },
    ],
    links: links,
  };

  if(isPlantOperatorLoggedIn) {
    delete propsForModuleCard.kpis
  }
  if(reRoute && isPlantOperatorLoggedIn){
    history.push( `/${window?.contextPath}/employee/tqm/landing`)
  }
  return <EmployeeModuleCard {...propsForModuleCard} />;
};

export default TqmCard;
