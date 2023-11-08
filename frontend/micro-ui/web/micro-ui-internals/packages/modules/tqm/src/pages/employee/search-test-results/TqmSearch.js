import React, { useMemo,useState,useEffect } from "react";
import { useTranslation } from "react-i18next";
import { Header, InboxSearchComposer, Loader } from "@egovernments/digit-ui-react-components";
// import { tqmSearchConfigPlantOperator } from "./configPlantOperator";
// import { tqmSearchConfigUlbAdmin } from "./configUlbAdmin";
const TqmSearch = () => {
  const { t } = useTranslation();
  const configModuleName = Digit.Utils.getConfigModuleName()
  const tenant = Digit.ULBService.getStateId();

  const { isLoading, data: config } = Digit.Hooks.useCustomMDMS(
    tenant,
    "commonSanitationUiConfig",
    [
      {
        "name": "SearchPlantOperatorConfig"
      },
      {
        "name": "SearchUlbAdminConfig"
      },
    ],
    {
      select: (data) => {
        if (Digit.Utils.tqm.isPlantOperatorLoggedIn())
          return data?.commonSanitationUiConfig?.SearchPlantOperatorConfig?.[0];
        if (Digit.Utils.tqm.isUlbAdminLoggedIn())
          return data?.commonSanitationUiConfig?.SearchUlbAdminConfig?.[0];
      }
    }
  );
  if (isLoading) return <Loader />

  return (
    <React.Fragment>
      <Header className="works-header-search">{t(config?.label)}</Header>
      <div className="inbox-search-wrapper">
        <InboxSearchComposer configs={config}></InboxSearchComposer>
      </div>
    </React.Fragment>
  )
}

export default TqmSearch;