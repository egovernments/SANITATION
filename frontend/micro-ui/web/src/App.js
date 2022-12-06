import React from "react";

import { initDSSComponents } from "@egovernments/digit-ui-module-dss";
import { DigitUI } from "@egovernments/digit-ui-module-core";
import { initLibraries } from "@egovernments/digit-ui-libraries";
import { initEngagementComponents } from "@egovernments/digit-ui-module-engagement";
import { initFSMLibraries } from "@egovernments/digit-ui-fsm-libraries";

import { initFSMComponents } from "@egovernments/digit-ui-module-fsm";
import { initHRMSComponents } from "@egovernments/digit-ui-module-hrms";
window.contextPath = window?.globalConfigs?.getConfig("CONTEXT_PATH");

initLibraries();
initFSMLibraries();

const enabledModules = ["FSM", "Payment", "DSS", "Engagement", "HRMS"];
window.Digit.ComponentRegistryService.setupRegistry({});

initDSSComponents();
initEngagementComponents();
initFSMComponents();
initHRMSComponents();

const moduleReducers = (initData) => ({
  initData,
});

function App() {
  window.contextPath = window?.globalConfigs?.getConfig("CONTEXT_PATH");
  const stateCode =
    window.globalConfigs?.getConfig("STATE_LEVEL_TENANT_ID") ||
    process.env.REACT_APP_STATE_LEVEL_TENANT_ID;
  if (!stateCode) {
    return <h1>stateCode is not defined</h1>;
  }
  return (
    <DigitUI
      stateCode={stateCode}
      enabledModules={enabledModules}
      moduleReducers={moduleReducers}
    />
  );
}

export default App;
