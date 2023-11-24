import React, { useCallback, useEffect, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { Header, Loader } from "@egovernments/digit-ui-react-components";
import { InboxSearchComposer } from "@egovernments/digit-ui-module-utilities";
import { useLocation } from "react-router-dom";
// import { configs } from "../../config/Employee/inbox";

const Inbox = () => {
  const { t } = useTranslation();
  const location = useLocation();

  //fetch this config from mdms and pass it to the preProcess fn
  // let configs = inboxConfigMukta(t);
  const [pageConfig, setPageConfig] = useState(null);
  // const moduleName = Digit.Utils.getConfigModuleName();
  const tenant = Digit.ULBService.getStateId();
  const { isLoading, data } = Digit.Hooks.useCustomMDMS(
    tenant,
    "commonUiConfig",
    [
      {
        name: "FSMInboxConfig",
      },
    ],
    {
      select: (data) => {
        return data;
      },
    }
  );
  const updatedConfig = useMemo(() => Digit.Utils.preProcessMDMSConfigInboxSearch(t, pageConfig, "sections.search.uiConfig.fields", {}), [
    data,
    pageConfig,
  ]);

  useEffect(() => {
    setPageConfig(_.cloneDeep(data?.["commonUiConfig"]?.FSMInboxConfig?.[0]));
  }, [data, location]);

  if (isLoading || !pageConfig) return <Loader />;
  return (
    <React.Fragment>
      <Header styles={{ fontSize: "32px" }}>
        {t(updatedConfig?.label)}
        {location?.state?.count ? <span className="inbox-count">{location?.state?.count}</span> : null}
      </Header>
      <div className="inbox-search-wrapper">
        <InboxSearchComposer configs={updatedConfig}></InboxSearchComposer>
      </div>
    </React.Fragment>
  );
};

export default Inbox;
