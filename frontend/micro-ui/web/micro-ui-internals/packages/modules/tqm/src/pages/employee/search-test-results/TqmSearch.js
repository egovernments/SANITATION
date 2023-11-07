import React, { useMemo,useState,useEffect } from "react";
import { useTranslation } from "react-i18next";
import { Header, InboxSearchComposer, Loader } from "@egovernments/digit-ui-react-components";
// import { tqmSearchConfigPlantOperator } from "./configPlantOperator";
// import { tqmSearchConfigUlbAdmin } from "./configUlbAdmin";
const TqmSearch = () => {
  const { t } = useTranslation();
  const configModuleName = Digit.Utils.getConfigModuleName()
  const tenant = Digit.ULBService.getStateId();
  const [config, setConfig] = useState();
  if (Digit.Utils.tqm.isPlantOperatorLoggedIn()) {
    const { isLoading, data } = Digit.Hooks.useCustomMDMS(
      tenant,
      "commonSanitationUiConfig",
      [
        {
          "name": "SearchPlantOperatorConfig"
        }
      ],
      {
        select: (data) => {
          return data?.commonSanitationUiConfig?.SearchPlantOperatorConfig?.[0];
        }
      }
    );
    useEffect(() => {
      setConfig(data);
    }, data)
    if (isLoading) return <Loader />
  }
  if (Digit.Utils.tqm.isUlbAdminLoggedIn()) {
    const { isLoading, data } = Digit.Hooks.useCustomMDMS(

      tenant,
      "commonSanitationUiConfig",
      [
        {
          "name": "SearchUlbAdminConfig"
        }
      ],
      {
        select: (data) => {
          return data?.commonSanitationUiConfig?.SearchUlbAdminConfig?.[0];
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
      <Header className="works-header-search">{t(config?.label)}</Header>
      <div className="inbox-search-wrapper">
        <InboxSearchComposer configs={config}></InboxSearchComposer>
      </div>
    </React.Fragment>
  )
}

export default TqmSearch;