import React, {useMemo,useState,useEffect} from "react";
import { useTranslation } from "react-i18next";
import { Header, InboxSearchComposer,Loader } from "@egovernments/digit-ui-react-components";
import { useLocation } from "react-router-dom";
import { tqmInboxConfig } from "./inboxConfig";
import { tqmInboxConfigPlantOperator } from "./inboxConfigPlantOperator";

const TqmInbox = () => {
    const { t } = useTranslation();
    const location = useLocation()
    const isPlantOperatorLoggedIn = Digit.Utils.tqm.isPlantOperatorLoggedIn()
    const isUlbAdminLoggedIn = Digit.Utils.tqm.isUlbAdminLoggedIn()
    const moduleName = Digit?.Utils?.getConfigModuleName() || "commonSanitationUiConfig"
    const tenant = Digit.ULBService.getStateId();
    const { isLoading, data:config } = Digit.Hooks.useCustomMDMS(tenant,
        "commonSanitationUiConfig",
        [
            {
                name: "InboxPlantOperatorConfig",
            },
            {
              name: "InboxUlbAdminConfig",
            }
        ],
        {
          select:(data) => {
            if(isPlantOperatorLoggedIn){
              return tqmInboxConfigPlantOperator?.tqmInboxConfig?.[0];
              // return data?.commonSanitationUiConfig?.InboxPlantOperatorConfig?.[0]
            }
            else if(isUlbAdminLoggedIn) {
              return tqmInboxConfig?.tqmInboxConfig?.[0];
              // return data?.commonSanitationUiConfig?.InboxUlbAdminConfig?.[0]
              
            }
            return tqmInboxConfigPlantOperator?.tqmInboxConfig?.[0];
          }
        }
        
        );
    
    if(isLoading) return <Loader />
    
    return (
        <React.Fragment>
            <Header styles={{ fontSize: "32px" }}>{t(config?.label)}{<span className="inbox-count">{location?.state?.count ? location?.state?.count : 0}</span>}</Header>
            <div className="inbox-search-wrapper">
              <InboxSearchComposer configs={config}></InboxSearchComposer>
            </div>
        </React.Fragment>
    )
}

export default TqmInbox;