export const tqmSearchConfigUlbAdmin = {
  tenantId: 'pg',
  moduleName: 'commonSanitationUiConfig',
  tqmSearchConfig: [
    {
      label: 'TQM_VIEW_PAST_RESULTS',
      type: 'search',
      apiDetails: {
        serviceName: '/pqm-service/v1/_search',
        requestParam: {},
        requestBody: {},
        minParametersForSearchForm: 0,
        masterName: 'commonUiConfig',
        moduleName: 'SearchTestResultsUlbAdmin',
        tableFormJsonPath: 'requestBody.custom',
        filterFormJsonPath: 'requestBody.custom',
        searchFormJsonPath: 'requestBody.custom',
      },
      sections: {
        search: {
          uiConfig: {
            type: 'filter',
            headerLabel: 'TQM_INBOX_FILTERS',
            headerStyle: null,
            primaryLabel: 'ES_COMMON_SEARCH',
            secondaryLabel: 'ES_COMMON_CLEAR_SEARCH',
            minReqFields: 0,
            showFormInstruction: 'TQM_SEARCH_HINT',
            defaultValues: {
              id:'',
              plantCodes: [],
              processCodes: [],
              testType: [],
              dateRange: {},
            },
            fields: [
              {
                "label": "TQM_TEST_ID",
                "type": "text",
                "isMandatory": false,
                "disable": false,
                // "preProcess": {
                //   "convertStringToRegEx": [
                //     "populators.validation.pattern"
                //   ]
                // },
                "populators": {
                  "name": "id",
                  // "error": "Enter valid bill number",
                  // "validation": {
                  //   "pattern": "^[A-Za-z0-9\\/-]*$",
                  //   "minlength": 2
                  // }
                }
              },
              {
                label: 'TQM_PLANT_NAME',
                type: 'apidropdown',
                isMandatory: false,
                disable: false,
                populators: {
                  optionsCustomStyle: {
                    top: '2.3rem',
                  },
                  name: 'plantCodes',
                  optionsKey: 'optionKey',
                  allowMultiSelect: true,
                  masterName: 'commonUiConfig',
                  moduleName: 'TqmInboxConfig',
                  customfn: 'populateProcessReqCriteria',
                },
              },
              {
                label: 'TQM_TREATMENT_PROCESS',
                type: 'apidropdown',
                isMandatory: false,
                disable: false,
                populators: {
                  optionsCustomStyle: {
                    top: '2.3rem',
                  },
                  name: 'processCodes',
                  optionsKey: 'optionKey',
                  allowMultiSelect: true,
                  masterName: 'commonUiConfig',
                  moduleName: 'TqmInboxConfig',
                  customfn: 'populateProcessReqCriteria',
                },
              },
              {
                label: 'TQM_TEST_TYPE',
                type: 'apidropdown',
                isMandatory: false,
                disable: false,
                populators: {
                  optionsCustomStyle: {
                    top: '2.3rem',
                  },
                  name: 'testType',
                  optionsKey: 'outputCode',
                  allowMultiSelect: true,
                  masterName: 'commonUiConfig',
                  moduleName: 'TqmInboxConfig',
                  customfn: 'populateOutputTypeReqCriteria',
                },
              },
              {
                label: 'TQM_INBOX_DATE_RANGE',
                type: 'dateRange',
                isMandatory: false,
                disable: false,
                populators: {
                  name: 'dateRange',
                },
              },
            ],
          },
          label: '',
          children: {},
          show: true,
          labelMobile: 'TQM_INBOX_FILTER',
        },
        searchResult: {
          uiConfig: {
            columns: [
              {
                label: 'TQM_TEST_ID',
                jsonPath: 'id',
                additionalCustomization: true,
              },
              {
                label: 'TQM_PLANT',
                jsonPath: 'plantCode',
                additionalCustomization: false,
                prefix:"TQM_MASTERS_PQM.PLANT_",
                translate:true
              },
              {
                label: 'TQM_TREATMENT_PROCESS',
                jsonPath: 'processCode',
                additionalCustomization: false,
                prefix:"TQM_MASTERS_PQM.ProcessType_",
                translate:true
              },
              {
                label: 'TQM_TEST_TYPE',
                jsonPath: 'testType',
                additionalCustomization: false,
                prefix:"TQM_MASTERS_PQM.TestType_",
                translate:true
              },
              {
                label: 'TQM_TEST_DATE',
                jsonPath: 'scheduledDate',
                additionalCustomization: true,
              },
              {
                label: 'TQM_TEST_RESULTS',
                jsonPath: 'wfStatus',
                additionalCustomization: true,
              },
            ],
            showActionBarMobileCard: true,
            actionButtonLabelMobileCard: 'TQM_VIEW_RESULTS',
            enableGlobalSearch: false,
            enableColumnSort: true,
            resultsJsonPath: 'tests',
          },
          children: {},
          show: true,
        },
      },
      additionalSections: {},
    },
  ],
};
