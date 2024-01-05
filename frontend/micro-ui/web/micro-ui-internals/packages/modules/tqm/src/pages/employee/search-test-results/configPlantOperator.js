export const tqmSearchConfigPlantOperator = {
  tenantId: 'pb',
  moduleName: 'commonSanitationUiConfig',
  tqmSearchConfig: [
    {
      "label": "TQM_VIEW_PAST_RESULTS",
      "type": "search",
      "apiDetails": {
        "serviceName": "/pqm-service/v1/_search",
        "requestParam": {},
        "requestBody": {},
        "minParametersForSearchForm": 0,
        "masterName": "commonUiConfig",
        "moduleName": "SearchTestResults",
        "tableFormJsonPath": "requestBody.custom",
        "filterFormJsonPath": "requestBody.custom",
        "searchFormJsonPath": "requestBody.custom"
      },
      "sections": {
        "search": {
          "uiConfig": {
            "type": "filter",
            "headerLabel": "TQM_INBOX_FILTERS",
            "headerStyle": null,
            "primaryLabel": "TQM_INBOX_FILTER",
            "secondaryLabel": "ES_COMMON_CLEAR_SEARCH",
            "minReqFields": 0,
            "showFormInstruction": "TQM_SEARCH_HINT",
            "defaultValues": {
              // "plantCodes": [],
              "processCodes": [],
              "materialCodes": [],
              "testType": [],
              "dateRange": {}
            },
            "fields": [
              // {
              //   "label": "TQM_PLANT_NAME",
              //   "type": "dropdown",
              //   "isMandatory": false,
              //   "disable": false,
              //   "populators": {
              //     "optionsCustomStyle": {
              //       "top": "2.3rem"
              //     },
              //     "name": "plantCodes",
              //     "optionsKey": "i18nKey",
              //     "allowMultiSelect": true,
              //     "mdmsv2": {
              //       "schemaCode": "PQM.Plant"
              //     }
              //   }
              // },
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
                "label": "TQM_TEST_TYPE",
                "type": "dropdown",
                "isMandatory": false,
                "disable": false,
                "populators": {
                  "optionsCustomStyle": {
                    "top": "2.3rem"
                  },
                  "name": "testType",
                  "optionsKey": "i18nKey",
                  "allowMultiSelect": true,
                  "mdmsv2": {
                    "schemaCode": "PQM.SourceType"
                  }
                }
              },
              {
                "label": "TQM_VIEW_PAST_DATE_RANGE",
                "type": "dateRange",
                "isMandatory": false,
                "disable": false,
                "key": "dateRange",
                "preProcess": {
                  "updateDependent": [
                    "populators.maxDate"
                  ]
                },
                "populators": {
                  "name": "dateRange",
                  "maxDate":"currentDate"
                }
              }
            ]
          },
          "label": "",
          "children": {},
          "show": true,
          "labelMobile": "TQM_INBOX_FILTER"
        },
        "searchResult": {
          "uiConfig": {
            "columns": [
              {
                "label": "TQM_TEST_ID",
                "jsonPath": "testId",
                "additionalCustomization": true
              },
              {
                "label": "TQM_TREATMENT_PROCESS",
                "jsonPath": "processCode",
                "additionalCustomization": false,
                "prefix": "PQM.Process_",
                "translate": true
              },
              {
                "label": "TQM_PLANT_NAME",
                "jsonPath": "plantCode",
                "prefix": "PQM.Plant_",
                "translate": true
              },
              {
                "label": "TQM_PROCESS_STAGE",
                "jsonPath": "stageCode",
                "additionalCustomization": false,
                "prefix": "PQM.STAGE_",
                "translate": true
              },
              {
                "label": "TQM_OUTPUT_TYPE",
                "jsonPath": "materialCode",
                "additionalCustomization": false,
                "prefix": "PQM.MATERIAL_",
                "translate": true
              },
              {
                "label": "TQM_TEST_TYPE",
                "jsonPath": "testType",
                "additionalCustomization": false,
                "prefix": "PQM.TESTTYPE_",
                "translate": true
              },
              {
                "label": "TQM_PENDING_DATE",
                "jsonPath": "scheduledDate",
                "additionalCustomization": true
              },
              {
                "label": "TQM_TEST_RESULTS",
                "jsonPath": "status",
                "additionalCustomization": true
              }
            ],
            "showActionBarMobileCard": true,
            "actionButtonLabelMobileCard": "TQM_VIEW_RESULTS",
            "enableGlobalSearch": false,
            "enableColumnSort": true,
            "resultsJsonPath": "tests",
            "tableClassName":"table pqm-table"
          },
          "children": {},
          "show": true
        },
        "filter": {
          "uiConfig": {
            "type": "sort",
            "headerStyle": null,
            "headerLabel": "TQM_INBOX_SORTBY",
            "primaryLabel": "TQM_INBOX_SORT",
            "secondaryLabel": "TQM_CLEAR_SEARCH",
            "minReqFields": 0,
            "defaultValues": {
              "sortOrder": ""
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
    },
  ],
};
