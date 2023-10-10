import React, { useEffect } from "react";
import { Switch, useLocation } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { PrivateRoute, AppContainer, BreadCrumb, BackButton } from "@egovernments/digit-ui-react-components";
import SampleComp from "./SampleComp";
import TQMPendingTask from "./TQMPendingTask";
import TQMLanding from "./TQMLanding";
import TqmSearch from "./search-test-results/TqmSearch";
import TqmHome from "./home/TqmHome";
import Create from "./add-test-results/CreateAddTestResult";

// import TQMSummary from "../../components/TQMSummary";

const TqmBreadCrumb = ({ location, defaultPath }) => {
  const { t } = useTranslation();
  const search = useLocation().search;

  const crumbs = [
    {
      path: `/${window?.contextPath}/employee`,
      content: t("TQM_HOME"),
      show: true,
    },
  ];
  return <BreadCrumb className="workbench-bredcrumb" crumbs={crumbs} spanStyle={{ maxWidth: "min-content" }} />;
};

const App = ({ path }) => {
  const { t } = useTranslation();
  const location = useLocation();
  const isPlantOperatorLoggedIn = Digit.Utils.tqm.isPlantOperatorLoggedIn()
  const TqmInbox = Digit?.ComponentRegistryService?.getComponent("TqmInbox");

  return (
    <React.Fragment>
      {/* <TqmBreadCrumb location={location} defaultPath={path} /> */}
      {isPlantOperatorLoggedIn && (location.pathname.includes("/response") ? null : <BackButton>{t("CS_COMMON_BACK")}</BackButton>)}
      <Switch>
        <AppContainer className="tqm">
          <PrivateRoute path={`${path}/landing`} component={() => <TQMLanding />} />
          <PrivateRoute path={`${path}/home`} component={() => <TqmHome {...{ path }} />} />
          <PrivateRoute path={`${path}/sample`} component={() => <SampleComp />} />
          <PrivateRoute path={`${path}/check`} component={() => <TQMPendingTask />} />
          <PrivateRoute path={`${path}/inbox`} component={() => <TqmInbox {...{ path }} />} />
          <PrivateRoute path={`${path}/search-test-results`} component={() => <TqmSearch {...{ path }} />} />
          <PrivateRoute path={`${path}/add-test-result`} component={() => <Create />} />
          
          {/* <PrivateRoute path={`${path}/summary/:id`} component={() => <TQMSummary />} /> */}
          
        </AppContainer>
      </Switch>
    </React.Fragment>
  );
};

export default App;
