import React, { useMemo, useState, useEffect } from "react";
import { useTranslation } from "react-i18next";
import { Header, InboxSearchComposer, Loader } from "@egovernments/digit-ui-react-components";
import { useLocation } from "react-router-dom";
// import { tqmInboxConfig } from "./inboxConfig";
// import { tqmInboxConfigPlantOperator } from "./inboxConfigPlantOperator";

const TqmInbox = () => {
  const { t } = useTranslation();
  const location = useLocation()
  const isPlantOperatorLoggedIn = Digit.Utils.tqm.isPlantOperatorLoggedIn()
  const isUlbAdminLoggedIn = Digit.Utils.tqm.isUlbAdminLoggedIn()
  const moduleName = "commonSanitationUiConfig"
  const tenant = Digit.ULBService.getStateId();
  
  const [config, setConfig] = useState();
  if (isPlantOperatorLoggedIn) {
    const { isLoading, data } = Digit.Hooks.useCustomMDMS(
      tenant,
      moduleName,
      [
        {
          "name": "InboxPlantOperatorConfig"
        }
      ],
      {
        select: (data) => {
          return data?.commonSanitationUiConfig?.InboxPlantOperatorConfig?.[0];
        }
      }
    );
    useEffect(() => {
      setConfig(data);
    }, data)
    if (isLoading) return <Loader />

  }
  if (isUlbAdminLoggedIn) {
    const { isLoading, data } = Digit.Hooks.useCustomMDMS(
      tenant,
      "commonSanitationUiConfig",
      [
        {
          "name": "InboxUlbAdminConfig"
        }
      ],
      {
        select: (data) => {
          return data?.commonSanitationUiConfig?.InboxUlbAdminConfig?.[0];
        }
      }
    );
    useEffect(() => {
      setConfig(data);
    }, data)
    if (isLoading) return <Loader />
  }

  return (
    <React.Fragment>
      <Header styles={{ fontSize: "32px" }}>{t(config?.label)}{location?.state?.count ? <span className="inbox-count">{location?.state?.count}</span> : null}</Header>
      <div className="inbox-search-wrapper">
        <InboxSearchComposer configs={config}></InboxSearchComposer>
      </div>
    </React.Fragment>
  )
}

export default TqmInbox;