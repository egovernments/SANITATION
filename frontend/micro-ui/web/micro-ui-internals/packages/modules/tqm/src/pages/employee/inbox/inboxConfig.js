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
              "moduleName": "pqm"
            },
            "moduleSearchCriteria": {}
          }
        },
        "minParametersForSearchForm": 0,
        "minParametersForFilterForm": 0,
        "masterName": "commonUiConfig",
        "moduleName": "TqmInboxConfigUlbAdmin",
        "tableFormJsonPath": "requestBody.inbox",
        "filterFormJsonPath": "requestBody.custom",
        "searchFormJsonPath": "requestBody.custom"
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
              "plantCodes": "",
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
                label: 'TQM_PLANT_NAME',
                type: 'dropdown',
                isMandatory: false,
                disable: false,
                populators: {
                  optionsCustomStyle: {
                    // top: '2.3rem',
                  },
                  name: 'plantCodes',
                  optionsKey: 'i18nKey',
                  allowMultiSelect: false,
                  mdmsv2:{
                    schemaCode:"PQM.Plant",
                  }
                },
              },
            ]
          },
          "label": "",
          "children": {},
          "show": true
        },
        "searchResult": {
          "uiConfig": {
            "columns": [
              {
                "label": "TQM_TEST_ID",
                "jsonPath": "businessObject.id",
              },
              {
                "label": "TQM_PLANT_NAME",
                "jsonPath": "businessObject.plantCode"
              },
              {
                "label": "TQM_PENDING_DATE",
                "jsonPath": "businessObject.scheduledDate",
              },
              {
                "label": "TQM_TREATMENT_PROCESS",
                "jsonPath": "businessObject.processCode"
              },
              {
                "label": "TQM_INBOX_STATUS",
                "jsonPath": "processInstance.state.applicationStatus",
                prefix:"WF_STATUS",
                translate:true
              },
              {
                "label": "TQM_INBOX_SLA",
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
            "formClassName":"filter",
            "type": "filter",
            "headerStyle": null,
            "primaryLabel": "Filter",
            // "secondaryLabel": "",
            "minReqFields": 0,
            "defaultValues": {
              "processCodes":[],
              "stage":[],
              "materialCodes":[],
              "status":[]
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
                  "labelKey":"i18nKey",
                  "masterName": "commonUiConfig",
                  "moduleName": "TqmInboxConfigUlbAdmin",
                  "customfn": "populateMdmsv2SearchReqCriteria",
                  "schemaCode":"PQM.Process"
                }
              },   
              {
                label: 'TQM_PROCESS_STAGE',
                type: 'dropdown',
                isMandatory: false,
                disable: false,
                populators: {
                  // optionsCustomStyle: {
                  //   top: '2.3rem',
                  // },
                  name: 'stage',
                  optionsKey: 'i18nKey',
                  allowMultiSelect: true,
                  mdmsv2:{
                    schemaCode:"PQM.Stage",
                  }
                },
              },
              {
                "label": "TQM_OUTPUT_TYPE",
                "type": "apicheckboxes",
                "isMandatory": false,
                "disable": false,
                "populators": {
                  "name": "materialCodes",
                  "optionsKey": "i18nKey",
                  "masterName": "commonUiConfig",
                  "moduleName": "TqmInboxConfigUlbAdmin",
                  "customfn": "populateMdmsv2SearchReqCriteria",
                  "labelKey":"i18nKey",
                  "schemaCode":"PQM.Material"
                }
              },
              {
                "type": "workflowstatesfilter",
                "isMandatory": false,
                "disable": false,
                "populators": {
                  "componentLabel": "TQM_WF_STATUS",
                  "name": "state",
                  "labelPrefix": "WF_STATUS",
                  "businessService": "PQM"
                }
              },
              
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
