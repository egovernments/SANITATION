import React, { useEffect,useReducer,Fragment } from "react";
import { Switch, useLocation } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { PrivateRoute, AppContainer, BreadCrumb, BackButton } from "@egovernments/digit-ui-react-components";
import SampleComp from "./SampleComp";
import TQMPendingTask from "./TQMPendingTask";
import TQMLanding from "./TQMLanding";
import TqmSearch from "./search-test-results/TqmSearch";
import TestDetails from "./test-details/TestDetails";
import TqmHome from "./home/TqmHome";
import Create from "./add-test-results/CreateAddTestResult";
import Test from "./test";
import TqmHeader from "../../components/TqmHeader";
import TqmAdminNotification from "./TqmAdminNotification";

// import TQMSummary from "../../components/TQMSummary";


const TqmBreadCrumb = ({ location, defaultPath }) => {
  const pathVar=location.pathname.replace(defaultPath+'/',"").split("?")?.[0];
  const { t } = useTranslation();
  const search = useLocation().search;
  const fromScreen = new URLSearchParams(search).get("from") || null;
  
  const crumbs = [
    {
      path: `/${window?.contextPath}/employee`,
      content: t("TQM_BREAD_HOME"),
      show: true,
    },
    {
      path: `/${window.contextPath}/employee/tqm/inbox`,
      content:  t(`TQM_BREAD_INBOX`) ,
      show: pathVar.includes("inbox")?true: false,
      
    },
    {
      path: `/${window.contextPath}/employee/tqm/search-test-results`,
      // content:  t(`TQM_BREAD_PAST_TESTS`) ,
      show: pathVar.includes("search-test-results")?true: false,
      content: fromScreen ? `${t(fromScreen)} / ${t("TQM_BREAD_PAST_TESTS")}` : t("TQM_BREAD_PAST_TESTS"),
      isBack:fromScreen ? true : false
    },
    {
      path: `/${window.contextPath}/employee/tqm/add-test-result`,
      content:  t(`TQM_BREAD_CREATE_TEST`) ,
      show: pathVar.includes("add-test-result")?true: false,
    },
    {
      path: `/${window.contextPath}/employee/tqm/add-test-result`,
      // content:  t(`TQM_BREAD_SENSOR`) ,
      show: pathVar.includes("search-devices")?true: false,
      content: fromScreen ? `${t(fromScreen)} / ${t("TQM_BREAD_SENSOR")}` : t("TQM_BREAD_SENSOR"),
      isBack:fromScreen ? true: false
    },
    {
      path: `/${window.contextPath}/employee/tqm/search-test-results`,
      // content:  t(`TQM_BREAD_VIEW_TEST_RESULTS`) ,
      show: pathVar.includes("view-test-results")?true: false,
      content:fromScreen ? `${t(fromScreen)} / ${t("TQM_BREAD_VIEW_TEST_RESULTS")}` : t("TQM_BREAD_VIEW_TEST_RESULTS"),
      isBack:fromScreen ? true: false
    },

    
  ];
  return <BreadCrumb className="workbench-bredcrumb" crumbs={crumbs} spanStyle={{ maxWidth: "min-content" }} />;
};

const App = ({ path }) => {
  const { t } = useTranslation();
  const location = useLocation();
  const isPlantOperatorLoggedIn = Digit.Utils.tqm.isPlantOperatorLoggedIn();
  const isUlbAdminLoggedIn = Digit.Utils.tqm.isUlbAdminLoggedIn();
  const TqmInbox = Digit?.ComponentRegistryService?.getComponent("TqmInbox");
  const TqmResponse = Digit?.ComponentRegistryService?.getComponent("TqmResponse");
  const TqmViewTestResults = Digit?.ComponentRegistryService?.getComponent("TqmViewTestResults");
  const TQMSummary = Digit?.ComponentRegistryService?.getComponent("TQMSummary");
  const SensorScreen = Digit?.ComponentRegistryService?.getComponent("SensorScreen");
  

  return (
    <>
      {isUlbAdminLoggedIn && <TqmBreadCrumb location={location} defaultPath={path} />}
      {/* {isPlantOperatorLoggedIn && (location.pathname.includes("/response") ? null : <BackButton>{t("CS_COMMON_BACK")}</BackButton>)} */}
      {isPlantOperatorLoggedIn && <TqmHeader location={location} defaultPath={path}/>}
      
      <Switch>
        <AppContainer className="tqm">
          <PrivateRoute path={`${path}/landing`} component={() => <TQMLanding />} />
          <PrivateRoute path={`${path}/home`} component={() => <TqmHome {...{ path }} />} />
          <PrivateRoute path={`${path}/sample`} component={() => <SampleComp />} />
          <PrivateRoute path={`${path}/check`} component={() => <TQMPendingTask />} />
          <PrivateRoute path={`${path}/inbox`} component={() => <TqmInbox {...{ path }} />} />
          <PrivateRoute path={`${path}/search-test-results`} component={() => <TqmSearch {...{ path }} />} />
          <PrivateRoute path={`${path}/add-test-result`} component={() => <Create />} />
          <PrivateRoute path={`${path}/test-details`} component={() => <TestDetails />} />
          <PrivateRoute path={`${path}/response`} component={() => <TqmResponse />} />
          <PrivateRoute path={`${path}/view-test-results`} component={() => <TqmViewTestResults />} />
          {/* for testing purpose */}
          <PrivateRoute path={`${path}/notification`} component={() => <TqmAdminNotification />} /> 
          <PrivateRoute path={`${path}/summary`} component={() => <TQMSummary />} />
          <PrivateRoute path={`${path}/search-devices`} component={() => <SensorScreen />} />
        </AppContainer>
      </Switch>
      </>
  );
};

export default App;
