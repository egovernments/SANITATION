import React, {useMemo,useState,useEffect} from "react";
import { useTranslation } from "react-i18next";
import { Header, InboxSearchComposer,Loader } from "@egovernments/digit-ui-react-components";
import { useLocation } from "react-router-dom";
import { tqmInboxConfig } from "./inboxConfig";

const TqmInbox = () => {
    const { t } = useTranslation();
    const location = useLocation()
  
    const moduleName = Digit?.Utils?.getConfigModuleName() || "commonSanitationUiConfig"
    const tenant = Digit.ULBService.getStateId();
    const { isLoading, data:config } = Digit.Hooks.useCustomMDMS(tenant,
        moduleName,
        [
            {
                name: "EstimateInboxConfig",
            },
        ],
        {
          select:(data) => {
            //for local config
            return tqmInboxConfig?.tqmInboxConfig?.[0];
          }
        }
        
        );
    
    if(isLoading) return <Loader />
    
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