import { Loader } from "@egovernments/digit-ui-react-components";
import React, { useEffect } from "react";
import { Link, useRouteMatch } from "react-router-dom";
import TqmCard from "./components/TqmCard";
import EmployeeApp from "./pages/employee"
import CitizenApp from "./pages/citizen";
import { UICustomizations } from "./configs/UICustomizations";
import TQMSummary from "./components/TQMSummary";

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
  TqmModule:TQMModule,
  TqmCard,
  TQMSummary
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
  updateCustomConfigs();
  Object.entries(componentsToRegister).forEach(([key, value]) => {
    Digit.ComponentRegistryService.setComponent(key, value);
  });
};
