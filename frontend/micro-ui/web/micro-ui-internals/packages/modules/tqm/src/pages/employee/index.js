import React, { useEffect,useReducer } from "react";
import { Switch, useLocation } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { PrivateRoute, AppContainer, BreadCrumb, BackButton,Tutorial,initialTutorialState,TutorialReducer } from "@egovernments/digit-ui-react-components";
import SampleComp from "./SampleComp";
import TQMPendingTask from "./TQMPendingTask";
import TQMLanding from "./TQMLanding";
import TqmSearch from "./search-test-results/TqmSearch";
import TestDetails from "./test-details/TestDetails";
import TqmHome from "./home/TqmHome";
import Create from "./add-test-results/CreateAddTestResult";
import Test from "./test";
import TqmHeader from "../../components/TqmHeader";
import { TutorialContext } from "../../components/Tutorial/TutorialContext";
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
  const isPlantOperatorLoggedIn = Digit.Utils.tqm.isPlantOperatorLoggedIn();
  const isUlbAdminLoggedIn = Digit.Utils.tqm.isUlbAdminLoggedIn();
  const TqmInbox = Digit?.ComponentRegistryService?.getComponent("TqmInbox");
  const TqmResponse = Digit?.ComponentRegistryService?.getComponent("TqmResponse");
  const TqmViewTestResults = Digit?.ComponentRegistryService?.getComponent("TqmViewTestResults");
  const TQMSummary = Digit?.ComponentRegistryService?.getComponent("TQMSummary");
  const SensorScreen = Digit?.ComponentRegistryService?.getComponent("SensorScreen");
  //write all the steps in intitialTutorialState inside module and pass it from here
  const [tutorial, updateTutorial] = useReducer(TutorialReducer, initialTutorialState);

  return (
    <TutorialContext.Provider value={{tutorial,updateTutorial}}>
      {isUlbAdminLoggedIn && <TqmBreadCrumb location={location} defaultPath={path} />}
      {/* {isPlantOperatorLoggedIn && (location.pathname.includes("/response") ? null : <BackButton>{t("CS_COMMON_BACK")}</BackButton>)} */}
      {isPlantOperatorLoggedIn && <TqmHeader />}
      <Tutorial tutorial={tutorial} updateTutorial={updateTutorial} />
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
          <PrivateRoute path={`${path}/test`} component={() => <Test />} /> 
          <PrivateRoute path={`${path}/summary`} component={() => <TQMSummary />} />
          <PrivateRoute path={`${path}/search-devices`} component={() => <SensorScreen />} />
        </AppContainer>
      </Switch>
    </TutorialContext.Provider>
  );
};

export default App;
