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
              "businessService": [
                "PQM"
              ],
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
            "type": "filter",
            "headerLabel": "TQM_INBOX_FILTERS",
            "searchWrapperStyles": {
              "flexDirection": "column-reverse",
              "marginTop": "1.4rem",
              "alignItems": "center",
              "justifyContent": "end",
              "gridColumn": "3"
            },
            "headerStyle": null,
            "primaryLabel": "TQM_INBOX_FILTER",
            "secondaryLabel": "ES_COMMON_CLEAR_SEARCH",
            "minReqFields": 0,
            "defaultValues": {
              "processCodes": [],
              "materialCodes": [],
              "status": [],
              "dateRange": ""
            },
            "fields": [
              {
                "label": "TQM_TREATMENT_PROCESS",
                "type": "dropdown",
                "isMandatory": false,
                "disable": false,
                "populators": {
                  "optionsCustomStyle": {
                    "top": "2.3rem"
                  },
                  "name": "processCodes",
                  "optionsKey": "i18nKey",
                  "allowMultiSelect": true,
                  "mdmsv2": {
                    "schemaCode": "PQM.Process"
                  }
                }
              },
              {
                "label": "TQM_OUTPUT_TYPE",
                "type": "dropdown",
                "isMandatory": false,
                "disable": false,
                "populators": {
                  "optionsCustomStyle": {
                    "top": "2.3rem"
                  },
                  "name": "materialCodes",
                  "optionsKey": "i18nKey",
                  "allowMultiSelect": true,
                  "mdmsv2": {
                    "schemaCode": "PQM.Material"
                  }
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
                  "optionsKey": "i18nKey",
                  "allowMultiSelect": true,
                  "masterName": "commonUiConfig",
                  "moduleName": "TqmInboxConfig",
                  "customfn": "populateStatusReqCriteria"
                }
              },
              {
                "label": "TQM_INBOX_DATE_RANGE",
                "type": "dateRange",
                "isMandatory": false,
                "disable": false,
                "populators": {
                  "name": "dateRange"
                }
              }
            ]
          },
          "label": "",
          "labelMobile": "TQM_INBOX_FILTER",
          "children": {},
          "show": true
        },
        "searchResult": {
          "uiConfig": {
            "columns": [
              {
                "label": "TQM_TEST_ID",
                "jsonPath": "ProcessInstance.businessId",
                "additionalCustomization": true
              },
              {
                "label": "TQM_TREATMENT_PROCESS",
                "jsonPath": "businessObject.processCode",
                "prefix": "PQM.Process_",
                "translate": true
              },
              {
                "label": "TQM_PLANT_NAME",
                "jsonPath": "businessObject.plantCode",
                "prefix": "PQM.Plant_",
                "translate": true
              },
              {
                "label": "TQM_PROCESS_STAGE",
                "jsonPath": "businessObject.stageCode",
                "prefix": "PQM.Stage_",
                "translate": true
              },
              {
                "label": "TQM_OUTPUT_TYPE",
                "jsonPath": "businessObject.materialCode",
                "prefix": "PQM.Material_",
                "translate": true
              },
              {
                "label": "TQM_PENDING_DATE",
                "jsonPath": "businessObject.scheduledDate",
                "additionalCustomization": true
              },
              {
                "label": "TQM_INBOX_STATUS",
                "jsonPath": "ProcessInstance.state.applicationStatus",
                "prefix": "WF_STATUS_TQM_",
                "translate": true
              },
              {
                "label": "TQM_INBOX_SLA",
                "jsonPath": "businessObject.serviceSla",
                "additionalCustomization": true
              }
            ],
            "enableGlobalSearch": false,
            "enableColumnSort": true,
            "resultsJsonPath": "items",
            "tableClassName":"table pqm-table"
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
                "roles": [
                  "FSM_CREATOR_EMP"
                ]
              },
              {
                "text": "TQM_VIEW_IOT_RESULTS",
                "url": "/employee/tqm/search-test-results",
                "roles": [
                  "FSM_CREATOR_EMP"
                ]
              },
              {
                "text": "TQM_SENSOR_MON",
                "url": "/employee/tqm/search-devices",
                "roles": [
                  "FSM_CREATOR_EMP"
                ]
              },
              {
                "text": "TQM_VIEW_DASHBOARD",
                "url": "/employee/tqm/dashboard",
                "roles": [
                  "FSM_CREATOR_EMP"
                ]
              }
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
            "formClassName": "filter",
            "type": "sort",
            "headerStyle": null,
            "headerLabel": "TQM_INBOX_SORTBY",
            "primaryLabel": "TQM_INBOX_SORT",
            "secondaryLabel": "TQM_CLEAR_SEARCH",
            "minReqFields": 0,
            "defaultValues": {
              "sortOrder": {
                "code": "LATEST_FIRST",
                "name": "TQM_INBOX_LATEST_FIRST",
                "value": "DESC"
              }
            },
            "fields": [
              {
                "label": "",
                "type": "radio",
                "isMandatory": false,
                "disable": false,
                "populators": {
                  "name": "sortOrder",
                  "options": [
                    {
                      "code": "LATEST_FIRST",
                      "name": "TQM_INBOX_LATEST_FIRST",
                      "value": "DESC"
                    },
                    {
                      "code": "LATEST_LAST",
                      "name": "TQM_INBOX_LATEST_LAST",
                      "value": "ASC"
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
              }
            ]
          },
          "label": "Filter",
          "labelMobile": "TQM_INBOX_SORT",
          "show": true
        }
      },
      "additionalSections": {}
    }
  ]
}
