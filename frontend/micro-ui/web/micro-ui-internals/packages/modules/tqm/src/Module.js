import { Loader } from "@egovernments/digit-ui-react-components";
import React, { useEffect } from "react";
import { Link, useRouteMatch } from "react-router-dom";
import TqmCard from "./components/TqmCard";
import EmployeeApp from "./pages/employee";
import CitizenApp from "./pages/citizen";
import { UICustomizations } from "./configs/UICustomizations";
import TQMPendingTask from "./pages/employee/TQMPendingTask";
import TQMLanding from "./pages/employee/TQMLanding";
import { CustomisedHooks } from "./hooks";

// import TQMSummary from "./components/TQMSummary";

// TQM specific components
import TqmInbox from "./pages/employee/inbox/TqmInbox";
import TestDetails from "./pages/employee/test-details/TestDetails";
import CardReading from "./components/CardReadings";
import Response from "./pages/employee/Response";
import ViewTestResults from "./pages/employee/test-results/ViewTestResults";
import DetailsTable from "./components/DetailsTable";
import DocumentsPreview from "./components/DocumentsPreview";

const TQMModule = ({ stateCode, userType, tenants }) => {
  const moduleCode = "TQM";
  const { path, url } = useRouteMatch();
  const language = Digit.StoreData.getCurrentLanguage();
  const { isLoading, data: store } = Digit.Services.useStore({ stateCode, moduleCode, language });

  if (isLoading) {
    return <Loader />;
  }

  if (userType === "citizen") {
    return <CitizenApp path={path} />;
  } else {
    return <EmployeeApp path={path} url={url} userType={userType} />;
  }
};

const componentsToRegister = {
  TqmModule: TQMModule,
  TqmCard,
  TQMPendingTask,
  TQMLanding,
  TqmInbox,
  TestDetails,
  TqmCardReading: CardReading,
  TqmResponse: Response,
  TqmViewTestResults: ViewTestResults,
  TqmDetailsTable: DetailsTable,
  TqmDocumentsPreview: DocumentsPreview,
  //   TQMSummary
};

const overrideHooks = () => {
  Object.keys(CustomisedHooks).map((ele) => {
    if (ele === "Hooks") {
      Object.keys(CustomisedHooks[ele]).map((hook) => {
        Object.keys(CustomisedHooks[ele][hook]).map((method) => {
          setupHooks(hook, method, CustomisedHooks[ele][hook][method]);
        });
      });
    } else if (ele === "Utils") {
      Object.keys(CustomisedHooks[ele]).map((hook) => {
        Object.keys(CustomisedHooks[ele][hook]).map((method) => {
          setupHooks(hook, method, CustomisedHooks[ele][hook][method], false);
        });
      });
    } else {
      Object.keys(CustomisedHooks[ele]).map((method) => {
        setupLibraries(ele, method, CustomisedHooks[ele][method]);
      });
    }
  });
};

/* To Overide any existing hook we need to use similar method */
const setupHooks = (HookName, HookFunction, method, isHook = true) => {
  window.Digit = window.Digit || {};
  window.Digit[isHook ? "Hooks" : "Utils"] = window.Digit[isHook ? "Hooks" : "Utils"] || {};
  window.Digit[isHook ? "Hooks" : "Utils"][HookName] = window.Digit[isHook ? "Hooks" : "Utils"][HookName] || {};
  window.Digit[isHook ? "Hooks" : "Utils"][HookName][HookFunction] = method;
};

/* To Overide any existing libraries  we need to use similar method */
const setupLibraries = (Library, service, method) => {
  window.Digit = window.Digit || {};
  window.Digit[Library] = window.Digit[Library] || {};
  window.Digit[Library][service] = method;
};

/* To Overide any existing config/middlewares  we need to use similar method */
const updateCustomConfigs = () => {
  setupLibraries("Customizations", "commonUiConfig", { ...window?.Digit?.Customizations?.commonUiConfig, ...UICustomizations });
};

export const initTQMComponents = () => {
  overrideHooks();
  updateCustomConfigs();
  Object.entries(componentsToRegister).forEach(([key, value]) => {
    Digit.ComponentRegistryService.setComponent(key, value);
  });
};
