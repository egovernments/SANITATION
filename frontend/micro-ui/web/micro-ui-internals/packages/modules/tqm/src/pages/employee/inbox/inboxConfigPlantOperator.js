export const tqmInboxConfigPlantOperator = {
  "tenantId": "pb",
  "moduleName": "commonSanitationUiConfig",
  "tqmInboxConfig": [
    {
      "label": "ACTION_TEST_TQM_INBOX",
      "type": "inbox",
      "apiDetails": {
        "serviceName": "/inbox/v2/_search",
        "requestParam": {},
        "requestBody": {
          "inbox": {
            "processSearchCriteria": {
              "businessService": ["PQM"],
              "moduleName": "pqm"
            },
            "moduleSearchCriteria": {}
          }
        },
        "minParametersForSearchForm": 0,
        "minParametersForFilterForm": 0,
        "masterName": "commonUiConfig",
        "moduleName": "TqmInboxConfig",
        "tableFormJsonPath": "requestBody.inbox",
        "filterFormJsonPath": "requestBody.custom",
        "searchFormJsonPath": "requestBody.custom"
      },
      "sections": {
        "search": {
          "uiConfig": {
            "type":"filter",
            "headerLabel":"TQM_INBOX_FILTERS",
            "searchWrapperStyles":{
              flexDirection:"column-reverse",
              marginTop:"1.4rem",
              alignItems:"center",
              justifyContent:"end",
              gridColumn:"3"
            },
            "headerStyle": null,
            "primaryLabel": "ES_COMMON_SEARCH",
            "secondaryLabel": "ES_COMMON_CLEAR_SEARCH",
            "minReqFields": 0,
            "defaultValues": {
              "processCodes":[],
              // "status":"",
              "materialCodes":[],
              "status":[],
              "dateRange":""
            },
            "fields": [
              {
                "label": "TQM_TREATMENT_PROCESS",
                "type": "apidropdown",
                "isMandatory": false,
                "disable": false,
                "populators": {
                  "optionsCustomStyle": {
                    "top": "2.3rem"
                  },
                  "name": "processCodes",
                  "optionsKey": "optionKey",
                  "allowMultiSelect": true,
                  "masterName": "commonUiConfig",
                  "moduleName": "TqmInboxConfig",
                  "customfn": "populateProcessReqCriteria"
                }
              },
              {
                "label": "TQM_OUTPUT_TYPE",
                "type": "apidropdown",
                "isMandatory": false,
                "disable": false,
                "populators": {
                  "optionsCustomStyle": {
                    "top": "2.3rem"
                  },
                  "name": "materialCodes",
                  "optionsKey": "outputCode",
                  "allowMultiSelect": true,
                  "masterName": "commonUiConfig",
                  "moduleName": "TqmInboxConfig",
                  "customfn": "populateOutputTypeReqCriteria"
                }
              },
              {
                "label": "TQM_INBOX_STATUS",
                "type": "apidropdown",
                "isMandatory": false,
                "disable": false,
                "populators": {
                  "optionsCustomStyle": {
                    "top": "2.3rem"
                  },
                  "name": "status",
                  "optionsKey": "optionKey",
                  "allowMultiSelect": true,
                  "masterName": "commonUiConfig",
                  "moduleName": "TqmInboxConfig",
                  "customfn": "populateStatusReqCriteria"
                }
              },
              {
                "label":"TQM_INBOX_DATE_RANGE",
                "type": "dateRange",
                "isMandatory": false,
                "disable": false,
                "populators": { 
                    "name": "dateRange"
                }
            },
              
            ]
          },
          "label": "",
          "labelMobile":"TQM_INBOX_FILTER",
          "children": {},
          "show": true
        },
        "searchResult": {
          // "label": "",
          // "estimateNumber": "",
          // "projectId": "",
          // "department": "",
          // "estimateStatus": "",
          // "fromProposalDate": "",
          // "toProposalDate": "",
          "uiConfig": {
            "columns": [
              {
                "label": "testId",
                "jsonPath": "businessObject.applicationNo",
              },
              {
                "label": "treatmentProcess",
                "jsonPath": "businessObject.processCode"
              },
              {
                "label": "stage",
                "jsonPath": "businessObject.plantCode"
              },
              {
                "label": "outputType",
                "jsonPath": "businessObject.plantCode",
              },
              {
                "label": "pendingDate",
                "jsonPath": "businessObject.plantCode",
              },
              {
                "label": "status",
                "jsonPath": "businessObject.serviceSla",
              },
              {
                "label": "sla",
                "jsonPath": "businessObject.serviceSla",
                "additionalCustomization": true,
              }
            ],
            "enableGlobalSearch": false,
            "enableColumnSort": true,
            "resultsJsonPath": "items"
          },
          "children": {},
          "show": true
        },
        "links": {
          "uiConfig": {
            "links": [
              {
                "text": "TQM_VIEW_PAST_RESULTS",
                "url": "/employee/tqm/search-test-results",
                "roles": ["FSM_CREATOR_EMP"]
              },
              {
                "text": "TQM_VIEW_IOT_RESULTS",
                "url": "/employee/tqm/search-test-results",
                "roles": ["FSM_CREATOR_EMP"]
              },
              {
                "text": "TQM_SENSOR_MON",
                "url": "/employee/tqm/search-devices",
                "roles": ["FSM_CREATOR_EMP"]
              },
              {
                "text": "TQM_VIEW_DASHBOARD",
                "url": "/employee/tqm/dashboard",
                "roles": ["FSM_CREATOR_EMP"]
              },
            ],
            "label": "TQM_QUALITY_TESTING",
            "logoIcon": {
              "component": "TqmInboxIcon",
              "customClass": "inbox-links-icon"
            }
          },
          "children": {},
          "show": true
        },
        "filter": {
          "uiConfig": {
            "type": "sort",
            "headerStyle": null,
            "headerLabel":"TQM_INBOX_SORTBY",
            "primaryLabel": "TQM_INBOX_SORT",
            "secondaryLabel": "TQM_CLEAR_SEARCH",
            "minReqFields": 0,
            "defaultValues": {
              "sortBy":""
            },
            "fields": [
              {
                "label": "",
                "type": "radio",
                "isMandatory": false,
                "disable": false,
                "populators": {
                  "name": "sortBy",
                  "options": [
                    {
                      "code": "LATEST_FIRST",
                      "name": "TQM_INBOX_LATEST_FIRST"
                    },
                    {
                      "code": "LATEST_LAST",
                      "name": "TQM_INBOX_LATEST_LAST"
                    }
                  ],
                  "optionsKey": "name",
                  "styles": {
                    "gap": "1rem",
                    "flexDirection": "column"
                  },
                  "innerStyles": {
                    "display": "flex"
                  }
                }
              },
              
            ]
          },
          "label": "Filter",
          "labelMobile":"TQM_INBOX_SORT",
          "show": true
        }
      },
      "additionalSections": {}
    }
  ]
}
