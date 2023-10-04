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
            "primaryLabel": "Search",
            "secondaryLabel": "Clear Search",
            "minReqFields": 1,
            "defaultValues": {
              "processCode":[],
              // "status":"",
              "outputType":[],
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
                  "name": "processCode",
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
                  "name": "outputType",
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
