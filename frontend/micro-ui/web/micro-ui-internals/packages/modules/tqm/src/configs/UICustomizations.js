
import { Link } from "react-router-dom";
import _ from "lodash";
import React from "react";
import { Amount, LinkLabel } from "@egovernments/digit-ui-react-components";
import { useHistory } from "react-router-dom";

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
      
      //processcodes
      data.body.inbox.moduleSearchCriteria.processCodes = processCodes?.map(processCode => processCode.code)

      //materialcodes
      data.body.inbox.moduleSearchCriteria.materialCodes = materialCodes?.map(materialCode => materialCode.code)

      //status
      data.body.inbox.moduleSearchCriteria.wfStatus = status?.map(status => status.applicationStatus)

      //fromDate and toDate
      const {fromDate,toDate} = Digit.Utils.tqm.convertDateRangeToEpochObj(dateRange) || {}
      data.body.inbox.moduleSearchCriteria.fromDate = fromDate
      data.body.inbox.moduleSearchCriteria.toDate = toDate

      //sortOrder sortBy 

      data.body.inbox.moduleSearchCriteria.sortBy = "createdTime"
      data.body.inbox.moduleSearchCriteria.sortOrder = sortOrder?.value

      //limit offset

      cleanObject(data.body.inbox.processSearchCriteria)
      cleanObject(data.body.inbox.moduleSearchCriteria)
     
      
      data.body.inbox.limit = 100
      data.body.inbox.offset = 0
      

      //set tenantId
      data.body.inbox.tenantId = Digit.ULBService.getCurrentTenantId();
      data.body.inbox.processSearchCriteria.tenantId = Digit.ULBService.getCurrentTenantId();


      // //testting
      // data.body.inbox.moduleSearchCriteria.ids = ["8"]

      //delete custom
      delete data.body.custom;

      return data
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
           const wfStates = data?.BusinessServices?.[0]?.states?.filter(state=>state.applicationStatus
            )?.map(state => {
              return {
                i18nKey:`WF_STATUS_${businessServiceMap?.tqm}_${state?.applicationStatus}`,
                ...state
              }
           })
           return wfStates
          },
        },
      };
    },
    getCustomActionLabel:(obj,row) => {
      const type = obj.apiResponse?.ProcessInstance?.state?.applicationStatus
      switch (type) {
        case "SCHEDULED":
          return "TQM_INBOX_ACTION_UPDATE_STATUS"
        
        case "PENDINGRESULTS":
          return "TQM_INBOX_ACTION_UPDATE_RESULTS"
          
        default:
          return "case_not_found"
      }
      
    },
    additionalCustomizations:(row, key, column, value, t, searchResult) => {
      switch (key) {
        case "TQM_INBOX_SLA":
          return value > 0 ? <span className="sla-cell-success">{value}</span> : <span className="sla-cell-error">{value}</span>;
          
        case "TQM_PENDING_DATE":
          return  Digit.DateUtils.ConvertEpochToDate(value)
        default:
          return "case_not_found"
      }
    },
    onCardActionClick:(obj)=> {
      return `test-details?id=${obj?.apiResponse?.ProcessInstance?.businessId}`
    },
    
  },
  TqmInboxConfigUlbAdmin:{
    populateMdmsv2SearchReqCriteria: ({schemaCode}) => {
      const tenantId = Digit.ULBService.getCurrentTenantId();

      return {
        url: "/mdms-v2/v2/_search",
        params: { },
        body: {
          tenantId,
          MdmsCriteria: {
            tenantId: tenantId,
            schemaCode: schemaCode,
            isActive: true,
          },
        },
        changeQueryName:`mdms-v2-${schemaCode}`,
        config: {
          enabled: schemaCode ? true : false,
          select: (response) => {
            const { mdms } = response;
            //first filter with isActive
            //then make a data array with actual data
            //refer the "code" key in data(for now) and set options array , also set i18nKey in each object to show in UI
            const options = mdms?.map((row) => {
              return {
                i18nKey: Digit.Utils.locale.getTransformedLocale(`${row?.schemaCode}_${row?.data?.code}`),
                ...row.data,
              };
            });
            return options;
          },
        },
      };
    },
    preProcess: (data,additionalDetails) => {
      
      const { id,plantCodes,processCodes,stage, materialCodes, status } = data.body.custom || {};
      //ids
      data.body.inbox.moduleSearchCriteria.ids = id ?  [id] : null

      //plantCodes 
      data.body.inbox.moduleSearchCriteria.plantCodes = plantCodes?.code

      //stage
      data.body.inbox.moduleSearchCriteria.stageCodes = stage?.map(st => st.code)

      //materialcodes
      data.body.inbox.moduleSearchCriteria.materialCodes = Object.keys(materialCodes?materialCodes:{})?.filter(key => materialCodes[key])

      //processcodes
      data.body.inbox.moduleSearchCriteria.processCodes = Object.keys(processCodes?processCodes:{})?.filter(key => processCodes[key])

      //status
      data.body.inbox.moduleSearchCriteria.status = status ? Object?.keys(status)?.filter(key=>status[key]) : null

      cleanObject(data.body.inbox.processSearchCriteria)
      cleanObject(data.body.inbox.moduleSearchCriteria)

      //set tenantId
      data.body.inbox.tenantId = Digit.ULBService.getCurrentTenantId();
      data.body.inbox.processSearchCriteria.tenantId = Digit.ULBService.getCurrentTenantId();

      //delete custom
      delete data.body.custom;

      return data
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
           const wfStates = data?.BusinessServices?.[0]?.states?.filter(state=>state.applicationStatus
            )?.map(state => {
              return {
                i18nKey:`WF_STATUS_${businessServiceMap?.tqm}_${state?.applicationStatus}`,
                ...state
              }
           })
           return wfStates
          },
        },
      };
    },
    getCustomActionLabel:(obj,row) => {
      return ""
    },
    additionalCustomizations:(row, key, column, value, t, searchResult) => {
      switch (key) {
        case "TQM_INBOX_SLA":
          return value > 0 ? <span className="sla-cell-success">{value}</span> : <span className="sla-cell-error">{value}</span>;
          
        case "TQM_PENDING_DATE":
          return  Digit.DateUtils.ConvertEpochToDate(value)
        
        case "TQM_TEST_ID":
          return <span className="link">
            <Link
              to={`/${window.contextPath}/employee/tqm/view-test-results?tenantId=${Digit.ULBService.getCurrentTenantId()}&id=${value}`}
            >
              {String(value ? (column.translate ? t(column.prefix ? `${column.prefix}${value}` : value) : value) : t("ES_COMMON_NA"))}
            </Link>
          </span> 
      
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
      data.body.testSearchCriteria.plantCodes = plantCodes?.map(plantCode => plantCode.code)

      //processcodes
      data.body.testSearchCriteria.processCodes = processCodes?.map(processCode => processCode.code)

      //materialcodes
      data.body.testSearchCriteria.materialCodes = materialCodes?.map(materialCode => materialCode.code)
      //testType
      data.body.testSearchCriteria.testType = testType?.code
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
    onCardClick:(obj)=> {
      return `summary?id=${obj?.apiResponse?.id}`
    },
    onCardActionClick:(obj)=> {
      return `summary?id=${obj?.apiResponse?.id}`
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
      
      const { id,plantCodes, processCodes, testType, dateRange } = data.body.custom || {};
      data.body.testSearchCriteria={}

      //update testSearchCriteria

      //test id
      data.body.testSearchCriteria.ids = id ? [id] : []
      //plantcodes
      data.body.testSearchCriteria.plantCodes = plantCodes?.map(plantCode => plantCode.code)
      //processcodes
      data.body.testSearchCriteria.processCodes = processCodes?.map(processCode => processCode.code)
      //testType
      data.body.testSearchCriteria.testType = testType?.code
      //dataRange //fromDate //toDate
      const {fromDate,toDate} = Digit.Utils.tqm.convertDateRangeToEpochObj(dateRange) || {}
      data.body.testSearchCriteria.fromDate = fromDate
      data.body.testSearchCriteria.toDate = toDate

      //tenantId
      data.body.testSearchCriteria.tenantId = Digit.ULBService.getCurrentTenantId();

      cleanObject(data.body.testSearchCriteria)
      cleanObject(data.body.pagination)

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
              to={`/${window.contextPath}/employee/tqm/view-test-results?tenantId=${Digit.ULBService.getCurrentTenantId()}&id=${value}`}
            >
              {String(value ? (column.translate ? t(column.prefix ? `${column.prefix}${value}` : value) : value) : t("ES_COMMON_NA"))}
            </Link>
          </span>

        default:
          return "case_not_found"
      }
    }
  },
  SensorConfig:{
    preProcess: (data,additionalDetails) => {
      
      const { processCodes, materialCodes, status, dateRange,sortOrder,limit,offset } = data.body.custom || {};
      
      //processcodes
      data.body.inbox.moduleSearchCriteria.processCodes = processCodes?.map(processCode => processCode.code)

      //materialcodes
      data.body.inbox.moduleSearchCriteria.materialCodes = materialCodes?.map(materialCode => materialCode.code)

      //status
      data.body.inbox.moduleSearchCriteria.status = status?.map(status => status.applicationStatus)

      //fromDate and toDate
      const {fromDate,toDate} = Digit.Utils.tqm.convertDateRangeToEpochObj(dateRange) || {}
      data.body.inbox.moduleSearchCriteria.fromDate = fromDate
      data.body.inbox.moduleSearchCriteria.toDate = toDate

      //sortOrder sortBy 

      data.body.inbox.moduleSearchCriteria.sortBy = "createdTime"
      data.body.inbox.moduleSearchCriteria.sortOrder = sortOrder?.value

      //limit offset

      cleanObject(data.body.inbox.processSearchCriteria)
      cleanObject(data.body.inbox.moduleSearchCriteria)
     
      
      data.body.inbox.limit = 100
      data.body.inbox.offset = 0
      

      //set tenantId
      data.body.inbox.tenantId = Digit.ULBService.getCurrentTenantId();
      data.body.inbox.processSearchCriteria.tenantId = Digit.ULBService.getCurrentTenantId();


      // //testting
      // data.body.inbox.moduleSearchCriteria.ids = ["8"]

      //delete custom
      delete data.body.custom;

      return data
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
           const wfStates = data?.BusinessServices?.[0]?.states?.filter(state=>state.applicationStatus
            )?.map(state => {
              return {
                i18nKey:`WF_STATUS_${businessServiceMap?.tqm}_${state?.applicationStatus}`,
                ...state
              }
           })
           return wfStates
          },
        },
      };
    },
    getCustomActionLabel:(obj,row) => {
      return ""
    },
    additionalCustomizations:(row, key, column, value, t, searchResult) => {
      switch (key) {
        case "TQM_INBOX_SLA":
          return value > 0 ? <span className="sla-cell-success">{value}</span> : <span className="sla-cell-error">{value}</span>;
          
        case "TQM_PENDING_DATE":
          return  Digit.DateUtils.ConvertEpochToDate(value)
        default:
          return "case_not_found"
      }
    }
    
  }

}