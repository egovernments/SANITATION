import React, { useEffect } from "react";
import { Switch, useLocation } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { PrivateRoute, AppContainer, BreadCrumb, BackButton } from "@egovernments/digit-ui-react-components";
import SampleComp from "./SampleComp";
import TQMPendingTask from "./TQMPendingTask";
import TQMHome from "./TQMHome";

const TqmBreadCrumb = ({ location, defaultPath }) => {
  const { t } = useTranslation();
  const search = useLocation().search;

  const crumbs = [
    {
      path: `/${window?.contextPath}/employee`,
      content: t("WORKBENCH_HOME"),
      show: true,
    },
  ];
  return <BreadCrumb className="workbench-bredcrumb" crumbs={crumbs} spanStyle={{ maxWidth: "min-content" }} />;
};

const App = ({ path }) => {
  const { t } = useTranslation();
  const location = useLocation();
  return (
    <React.Fragment>
      <BackButton>{t("CS_COMMON_BACK")}</BackButton>
      <Switch>
        <AppContainer className="tqm">
          <PrivateRoute path={`${path}/home`} component={() => <TQMHome />} />
          <PrivateRoute path={`${path}/sample`} component={() => <SampleComp />} />
          <PrivateRoute path={`${path}/check`} component={() => <TQMPendingTask />} />
        </AppContainer>
      </Switch>
    </React.Fragment>
  );
};

export default App;
