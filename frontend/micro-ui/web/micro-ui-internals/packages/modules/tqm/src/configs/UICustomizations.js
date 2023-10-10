
import { Link } from "react-router-dom";
import _ from "lodash";
import React from "react";
import { Amount, LinkLabel } from "@egovernments/digit-ui-react-components";

//create functions here based on module name set in mdms(eg->SearchProjectConfig)
//how to call these -> Digit?.Customizations?.[masterName]?.[moduleName]
// these functions will act as middlewares
var Digit = window.Digit || {};

function extractArrayByKey(arrayOfObjects, key) {
  // Use the map() function to extract the values based on the key
  return arrayOfObjects.map(function(object) {
    return object[key];
  });
}

function cleanObject(obj) {
  for (const key in obj) {
    if (obj.hasOwnProperty(key)) {
      if (Array.isArray(obj[key])) {
        if (obj[key].length === 0) {
          delete obj[key];
        }
      } else if (obj[key] === undefined || obj[key] === null || obj[key] === false || obj[key] === '' || (typeof obj[key] === 'object' && Object.keys(obj[key]).length === 0)) {
        delete obj[key];
      }
    }
  }
  return obj;
}

//this is a recursive function , test it out before you use this
function cleanObjectNested(obj) {
  for (const key in obj) {
    if (obj.hasOwnProperty(key)) {
      if (typeof obj[key] === 'object' && !Array.isArray(obj[key])) {
        // Recursively clean nested objects
        cleanObjectNested(obj[key]);
        // If the nested object becomes empty, remove it
        if (Object.keys(obj[key]).length === 0) {
          delete obj[key];
        }
      } else if (Array.isArray(obj[key]) && obj[key].length === 0) {
        // Remove empty arrays
        delete obj[key];
      } else if (
        obj[key] === undefined ||
        obj[key] === null ||
        obj[key] === false ||
        obj[key] === '' ||
        (typeof obj[key] === 'object' && Object.keys(obj[key]).length === 0)
      ) {
        // Remove falsy values (except 0)
        delete obj[key];
      }
    }
  }
  return obj;
}

const businessServiceMap = {
 tqm:"PQM"
};

const tqmRoleMapping = {
  plant:["PQM_FSTP_OPERATOR"],
  ulb:["PQM_ADMIN"]
}

const urls = {
  search:"/pqm/v1/_search"
}

export const UICustomizations = {
  urls,
  tqmRoleMapping,
  businessServiceMap,
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
      
      data.config.enabled = false
     //TODO:: here make the request info accordingly and return when you get inbox API details
      return data
    },
    populateProcessReqCriteria:() => {
      const tenantId = Digit.ULBService.getCurrentTenantId();

      return {
        url: "/egov-workflow-v2/egov-wf/businessservice/_search",
        params: { tenantId, businessServices: businessServiceMap?.tqm },
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
        params: { tenantId, businessServices: businessServiceMap?.tqm },
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
        params: { tenantId, businessServices: businessServiceMap?.tqm },
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
    populateStatusReqCriteria:() => {
      const tenantId = Digit.ULBService.getCurrentTenantId();

      return {
        url: "/egov-workflow-v2/egov-wf/businessservice/_search",
        params: { tenantId, businessServices: businessServiceMap?.tqm },
        body: {
         
        },
        changeQueryName:"setWorkflowStatus",
        config: {
          enabled: true,
          select: (data) => {
           return [
            {
              outputCode:"status1",
              optionKey:"status1"
            },
            {
              outputCode:"status2",
              optionKey:"status2"
            }
           ]
          },
        },
      };
    },
    getCustomActionLabel:(obj,row) => {
      return ""
    },
    
  },
  SearchTestResults: {
    preProcess: (data,additionalDetails) => {
      
      const { plantCodes, processCodes, materialCodes, testType, dateRange,sortOrder,limit,offset } = data.body.custom || {};

      data.body.testSearchCriteria={}
      data.body.pagination={}
      //update testSearchCriteria

      //update pagination

      //plantcodes
      data.body.testSearchCriteria.plantCodes = plantCodes?.map(plantCode => plantCode.processCode)

      //processcodes
      data.body.testSearchCriteria.processCodes = processCodes?.map(processCode => processCode.processCode)

      //materialcodes
      data.body.testSearchCriteria.materialCodes = materialCodes?.map(materialCode => materialCode.outputCode)
      //testType
      data.body.testSearchCriteria.testType = testType?.map(test=>test.outputCode)
      //dataRange //fromDate //toDate
      
      //sortOrder
      data.body.pagination.sortOrder = sortOrder?.value


      cleanObject(data.body.testSearchCriteria)
      cleanObject(data.body.pagination)
     
      if(Digit.Utils.tqm.isPlantOperatorLoggedIn()){
        data.body.pagination.limit = 100
      }

      //delete custom
      delete data.body.custom;
      return data
    },
    MobileDetailsOnClick:() => {
      return ""
    },
    onCardClick:(obj,row)=> {
      
    },
    onCardActionClick:(obj,row)=> {
      
    },
    getCustomActionLabel:(obj,row) => {
      return ""
    },
    additionalCustomization:(row, key, column, value, t, searchResult) => {

    }
  }

}