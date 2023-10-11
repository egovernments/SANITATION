
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
  plant:["PQM_TP_OPERATOR"],
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

      const { processCodes, materialCodes, status, dateRange,sortOrder,limit,offset } = data.body.custom || {};
      
      //plantCode

      //materialCodes

      //status

      //fromDate and toDate
      const {fromDate,toDate} = Digit.Utils.tqm.convertDateRangeToEpochObj(dateRange) || {}
      data.body.inbox.moduleSearchCriteria.fromDate = fromDate
      data.body.inbox.moduleSearchCriteria.toDate = toDate

      //sortOrder sortBy 

      // data.body.inbox.moduleSearchCriteria.sortBy = "createdTime"
      // data.body.inbox.moduleSearchCriteria.sortOrder = "DESC"

      //limit offset

      cleanObject(data.body.inbox.processSearchCriteria)
      cleanObject(data.body.inbox.moduleSearchCriteria)
     
      if(Digit.Utils.tqm.isPlantOperatorLoggedIn()){
        data.body.inbox.limit = 100
        data.body.inbox.offset = 0
      }

      //set tenantId
      data.body.inbox.tenantId = Digit.ULBService.getCurrentTenantId();
      data.body.inbox.processSearchCriteria.tenantId = Digit.ULBService.getCurrentTenantId();

      //delete custom
      delete data.body.custom;

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
    additionalCustomizations:(row, key, column, value, t, searchResult) => {
      switch (key) {
        case "sla":
          return value > 0 ? <span className="sla-cell-success">{value}</span> : <span className="sla-cell-error">{value}</span>;
          
      
        default:
          return "case_not_found"
      }
    }
    
  },
  SearchTestResults: {
    preProcess: (data,additionalDetails) => {
      
      const { plantCodes, processCodes, materialCodes, testType, dateRange,sortOrder,limit,offset } = data.body.custom || {};

      data.body.testSearchCriteria={}
      data.body.pagination={}

      //update testSearchCriteria

      //plantcodes
      data.body.testSearchCriteria.plantCodes = plantCodes?.map(plantCode => plantCode.processCode)

      //processcodes
      data.body.testSearchCriteria.processCodes = processCodes?.map(processCode => processCode.processCode)

      //materialcodes
      data.body.testSearchCriteria.materialCodes = materialCodes?.map(materialCode => materialCode.outputCode)
      //testType
      data.body.testSearchCriteria.testType = testType?.map(test=>test.outputCode)
      //dataRange //fromDate //toDate
      const {fromDate,toDate} = Digit.Utils.tqm.convertDateRangeToEpochObj(dateRange) || {}
      data.body.testSearchCriteria.fromDate = fromDate
      data.body.testSearchCriteria.toDate = toDate

      //sortOrder
      data.body.pagination.sortOrder = sortOrder?.value

      cleanObject(data.body.testSearchCriteria)
      cleanObject(data.body.pagination)

      //update pagination
      
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
    additionalCustomizations:(row, key, column, value, t, searchResult) => {
      switch (key) {
        case "TQM_TEST_RESULTS":
          return value > 0 ? <span className="sla-cell-success">{value}</span> : <span className="sla-cell-error">{value}</span>;
          
        case "TQM_PENDING_DATE":
          return  Digit.DateUtils.ConvertEpochToDate(value)

        default:
          return "case_not_found"
      }
    }
  },
  SearchTestResultsUlbAdmin: {
    preProcess: (data,additionalDetails) => {
      
      const { plantCodes, processCodes, materialCodes, testType, dateRange,sortOrder,limit,offset } = data.body.custom || {};

      data.body.testSearchCriteria={}
      data.body.pagination={}

      //update testSearchCriteria

      //plantcodes
      data.body.testSearchCriteria.plantCodes = plantCodes?.map(plantCode => plantCode.processCode)

      //processcodes
      data.body.testSearchCriteria.processCodes = processCodes?.map(processCode => processCode.processCode)

      //materialcodes
      data.body.testSearchCriteria.materialCodes = materialCodes?.map(materialCode => materialCode.outputCode)
      //testType
      data.body.testSearchCriteria.testType = testType?.map(test=>test.outputCode)
      //dataRange //fromDate //toDate
      const {fromDate,toDate} = Digit.Utils.tqm.convertDateRangeToEpochObj(dateRange) || {}
      data.body.testSearchCriteria.fromDate = fromDate
      data.body.testSearchCriteria.toDate = toDate

      //sortOrder
      data.body.pagination.sortOrder = sortOrder?.value

      cleanObject(data.body.testSearchCriteria)
      cleanObject(data.body.pagination)

      //update pagination
      
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
    additionalCustomizations:(row, key, column, value, t, searchResult) => {
      switch (key) {
        case "TQM_TEST_RESULTS":
          return value > 0 ? <span className="sla-cell-success">{value}</span> : <span className="sla-cell-error">{value}</span>;
          
        case "TQM_TEST_DATE":
          return  Digit.DateUtils.ConvertEpochToDate(value)
        
        case "TQM_TEST_ID":
          return <span className="link">
            <Link
              to={`/${window.contextPath}/employee/tqm/view-test-results?tenantId=${Digit.ULBService.getCurrentTenantId()}&testId=${value}`}
            >
              {String(value ? (column.translate ? t(column.prefix ? `${column.prefix}${value}` : value) : value) : t("ES_COMMON_NA"))}
            </Link>
          </span>

        default:
          return "case_not_found"
      }
    }
  }

}