
import { Link } from "react-router-dom";
import _ from "lodash";
import React from "react";
import { Amount, LinkLabel } from "@egovernments/digit-ui-react-components";

//create functions here based on module name set in mdms(eg->SearchProjectConfig)
//how to call these -> Digit?.Customizations?.[masterName]?.[moduleName]
// these functions will act as middlewares
var Digit = window.Digit || {};

export const UICustomizations = {
  SearchAttendanceConfig:{
    populateReqCriteria: () => {
      const tenantId = Digit.ULBService.getCurrentTenantId();

      return {
        url: "/egov-workflow-v2/egov-wf/businessservice/_search",
        params: { tenantId, businessServices: "MR" },
        body: {
         
        },
        config: {
          enabled: true,
          select: (data) => {
            const states =  data?.BusinessServices?.[0]?.states?.filter(state=> state.state)?.map(state=> {
              return {
                "i18nKey":`WF_${Digit?.Customizations?.["commonUiConfig"]?.getBusinessService("muster roll")}_STATUS_${state?.state}`,
                "wfStatus":state?.state
              }
            })
            return states  
          },
        },
      };
    }
  },
  TqmInboxConfig:{
    preProcess: (data,additionalDetails) => {
      console.log(data,additionalDetails);
      
     //TODO:: here make the request info accordingly and return when you get inbox API details
      return data
    },
    populateProcessReqCriteria:() => {
      const tenantId = Digit.ULBService.getCurrentTenantId();

      return {
        url: "/egov-workflow-v2/egov-wf/businessservice/_search",
        params: { tenantId, businessServices: "mukta-estimate" },
        body: {
         
        },
        config: {
          enabled: true,
          select: (data) => {
           return [
            {
              processCode:"process1",
              optionKey:"Process1"
            },
            {
              processCode:"process2",
              optionKey:"Process2"
            }
           ]
          },
        },
      };
    },
    populateStageReqCriteria:() => {
      const tenantId = Digit.ULBService.getCurrentTenantId();

      return {
        url: "/egov-workflow-v2/egov-wf/businessservice/_search",
        params: { tenantId, businessServices: "mukta-estimate" },
        body: {
         
        },
        config: {
          enabled: true,
          select: (data) => {
           return [
            {
              processCode:"process1",
              optionKey:"stage1"
            },
            {
              processCode:"process2",
              optionKey:"stage2"
            }
           ]
          },
        },
      };
    },
    populateOutputTypeReqCriteria:() => {
      const tenantId = Digit.ULBService.getCurrentTenantId();

      return {
        url: "/egov-workflow-v2/egov-wf/businessservice/_search",
        params: { tenantId, businessServices: "mukta-estimate" },
        body: {
         
        },
        config: {
          enabled: true,
          select: (data) => {
           return [
            {
              outputCode:"output1",
              optionKey:"Output1"
            },
            {
              outputCode:"output2",
              optionKey:"Output2"
            }
           ]
          },
        },
      };
    },

    
  }
}