export const tqmInboxConfig = {
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
              "businessService": ["TQM"],
              "moduleName": "pqm-service"
            },
            "moduleSearchCriteria": {}
          }
        },
        "minParametersForSearchForm": 0,
        "minParametersForFilterForm": 0,
        "masterName": "commonUiConfig",
        "moduleName": "TqmInboxConfig",
        "tableFormJsonPath": "requestBody.inbox",
        "filterFormJsonPath": "requestBody.inbox.moduleSearchCriteria",
        "searchFormJsonPath": "requestBody.inbox.moduleSearchCriteria"
      },
      "sections": {
        "search": {
          "uiConfig": {
            "searchWrapperStyles":{
              flexDirection:"column-reverse",
              marginTop:"1.4rem",
              alignItems:"center",
              justifyContent:"end",
              gridColumn:"3"
            },
            "headerStyle": null,
            "primaryLabel": "Search",
            "secondaryLabel": "Clear Search",
            "minReqFields": 1,
            "defaultValues": {
              "id": "",
              "plantCode": "",
            },
            "fields": [
              {
                "label": "TQM_TEST_ID",
                "type": "text",
                "isMandatory": false,
                "disable": false,
                // "preProcess": {
                //   "convertStringToRegEx": ["populators.validation.pattern"]
                // },
                "populators": {
                  "name": "id",
                  "error": "TQM_ERR_VALID_TEST_ID",
                  // "validation": {
                  //   "pattern": "ES\/[0-9]+-[0-9]+\/[0-9]+",
                  //   "minlength": 2
                  // }
                }
              },
              {
                "label": "TQM_PLANT_NAME",
                "type": "dropdown",
                "isMandatory": false,
                "disable": false,
                "populators": {
                  "name": "plantCode",
                  "optionsKey": "name",
                  "optionsCustomStyle": {
                    "top": "2.3rem"
                  },
                  // TODO: Update here once plants are configured in mdms
                  "mdmsConfig": {
                    "masterName": "ProjectType",
                    "moduleName": "works",
                    "localePrefix": "COMMON_MASTERS"
                  }
                }
              }
            ]
          },
          "label": "",
          "children": {},
          "show": true
        },
        "searchResult": {
          "label": "",
          "estimateNumber": "",
          "projectId": "",
          "department": "",
          "estimateStatus": "",
          "fromProposalDate": "",
          "toProposalDate": "",
          "uiConfig": {
            "columns": [
              {
                "label": "ESTIMATE_ESTIMATE_NO",
                "jsonPath": "ProcessInstance.businessId",
                "key": "estimateNumber",
                "additionalCustomization": true
              },
              {
                "label": "ES_COMMON_PROJECT_NAME",
                "jsonPath": "businessObject.project.name"
              },
              {
                "label": "ESTIMATE_PREPARED_BY",
                "jsonPath": "businessObject.additionalDetails.creator"
              },
              {
                "label": "COMMON_ASSIGNEE",
                "jsonPath": "ProcessInstance.assignes",
                "additionalCustomization": true,
                "key": "assignee"
              },
              {
                "label": "COMMON_WORKFLOW_STATES",
                "jsonPath": "ProcessInstance.state.state",
                "additionalCustomization": true,
                "key": "state"
              },
              {
                "label": "WORKS_ESTIMATED_AMOUNT",
                "jsonPath": "businessObject.additionalDetails.totalEstimatedAmount",
                "additionalCustomization": true,
                "key": "estimatedAmount",
                "headerAlign": "right"
              },
              {
                "label": "COMMON_SLA_DAYS",
                "jsonPath": "businessObject.serviceSla",
                "additionalCustomization": true,
                "key": "sla"
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
              // {
              //   "text": "ACTION_TEST_CREATE_ESTIMATE",
              //   "url": "/employee/project/search-project",
              //   "roles": ["ESTIMATE_CREATOR"]
              // },
              // {
              //   "text": "ACTION_TEST_SEARCH_ESTIMATE",
              //   "url": "/employee/estimate/search-estimate",
              //   "roles": [
              //     "ESTIMATE_VIEWER",
              //     "ESTIMATE_CREATOR",
              //     "ESTIMATE_VERIFIER",
              //     "TECHNICAL_SANCTIONER",
              //     "ESTIMATE_APPROVER"
              //   ]
              // }
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
            "type": "filter",
            "headerStyle": null,
            "primaryLabel": "Filter",
            "secondaryLabel": "",
            "minReqFields": 0,
            "defaultValues": {
              "state": "",
              "ward": [],
              "locality": [],
              "assignee": {
                "code": "ASSIGNED_TO_ALL",
                "name": "EST_INBOX_ASSIGNED_TO_ALL"
              },
              "processCode":"",
              "stage":[],
              "outputType":""
            },
            "fields": [
              {
                "label": "TQM_TREATMENT_PROCESS",
                "type": "apicheckboxes",
                "isMandatory": false,
                "disable": false,
                "populators": {
                  "name": "processCode",
                  "optionsKey": "processCode",
                  "labelKey":"optionKey",
                  "masterName": "commonUiConfig",
                  "moduleName": "TqmInboxConfig",
                  "customfn": "populateProcessReqCriteria"
                }
              },
              {
                "label": "TQM_PROCESS_STAGE",
                "type": "apidropdown",
                "isMandatory": false,
                "disable": false,
                "populators": {
                  "name": "stage",
                  "optionsKey": "optionKey",
                  "allowMultiSelect": true,
                  "masterName": "commonUiConfig",
                  "moduleName": "TqmInboxConfig",
                  "customfn": "populateStageReqCriteria"
                }
              },
              {
                "label": "TQM_OUTPUT_TYPE",
                "type": "apicheckboxes",
                "isMandatory": false,
                "disable": false,
                "populators": {
                  "name": "outputType",
                  "optionsKey": "outputCode",
                  "masterName": "commonUiConfig",
                  "moduleName": "TqmInboxConfig",
                  "customfn": "populateOutputTypeReqCriteria",
                  "labelKey":"optionKey",
                }
              },
              // {
              //   "type": "workflowstatesfilter",
              //   "isMandatory": false,
              //   "disable": false,
              //   "populators": {
              //     "componentLabel": "TQM_WF_STATUS",
              //     "name": "state",
              //     "labelPrefix": "WF_",
              //     "businessService": "tqm"
              //   }
              // },
              
            ]
          },
          "label": "Filter",
          "show": true
        }
      },
      "additionalSections": {}
    }
  ]
}
