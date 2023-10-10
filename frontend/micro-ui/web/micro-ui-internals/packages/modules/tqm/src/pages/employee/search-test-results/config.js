export const tqmSearchConfig = {
  tenantId: 'pb',
  moduleName: 'commonSanitationUiConfig',
  tqmSearchConfig: [
    {
      label: 'TQM_VIEW_PAST_RESULTS',
      type: 'search',
      apiDetails: {
        serviceName: '/pqm/v1/_search',
        requestParam: {},
        requestBody: {},
        minParametersForSearchForm: 1,
        masterName: 'commonUiConfig',
        moduleName: 'SearchTestResults',
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
            minReqFields: 1,
            showFormInstruction: 'TQM_SEARCH_HINT',
            defaultValues: {
              plantCodes: [],
              processCodes: [],
              materialCodes: [],
              testType: [],
              dateRange: '',
            },
            fields: [
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
                label: 'TQM_OUTPUT_TYPE',
                type: 'apidropdown',
                isMandatory: false,
                disable: false,
                populators: {
                  optionsCustomStyle: {
                    top: '2.3rem',
                  },
                  name: 'materialCodes',
                  optionsKey: 'outputCode',
                  allowMultiSelect: true,
                  masterName: 'commonUiConfig',
                  moduleName: 'TqmInboxConfig',
                  customfn: 'populateOutputTypeReqCriteria',
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
                jsonPath: 'label',
                additionalCustomization: false,
              },
              {
                label: 'TQM_TREATMENT_PROCESS',
                jsonPath: 'label',
                additionalCustomization: false,
              },
              {
                label: 'TQM_PROCESS_STAGE',
                jsonPath: 'label',
                additionalCustomization: false,
              },
              {
                label: 'TQM_OUTPUT_TYPE',
                jsonPath: 'label',
                additionalCustomization: false,
              },
              {
                label: 'TQM_TEST_TYPE',
                jsonPath: 'label',
                additionalCustomization: false,
              },
              {
                label: 'TQM_PENDING_DATE',
                jsonPath: 'label',
                additionalCustomization: false,
              },
              {
                label: 'TQM_TEST_RESULTS',
                jsonPath: 'label',
                additionalCustomization: true,
              },
            ],
            showActionBarMobileCard: true,
            actionButtonLabelMobileCard: 'TQM_TAKE_ACTION',
            enableGlobalSearch: false,
            enableColumnSort: true,
            resultsJsonPath: 'estimates',
          },
          children: {},
          show: true,
        },
        filter: {
          uiConfig: {
            type: 'sort',
            headerStyle: null,
            headerLabel: 'TQM_INBOX_SORTBY',
            primaryLabel: 'TQM_INBOX_SORT',
            secondaryLabel: 'TQM_CLEAR_SEARCH',
            minReqFields: 0,
            defaultValues: {
              sortOrder: '',
            },
            fields: [
              {
                label: '',
                type: 'radio',
                isMandatory: false,
                disable: false,
                populators: {
                  name: 'sortOrder',
                  options: [
                    {
                      code: 'LATEST_FIRST',
                      name: 'TQM_INBOX_LATEST_FIRST',
                      value:"ASC"
                    },
                    {
                      code: 'LATEST_LAST',
                      name: 'TQM_INBOX_LATEST_LAST',
                      value:"DESC"
                    },
                  ],
                  optionsKey: 'name',
                  styles: {
                    gap: '1rem',
                    flexDirection: 'column',
                  },
                  innerStyles: {
                    display: 'flex',
                  },
                },
              },
            ],
          },
          label: 'Filter',
          labelMobile: 'TQM_INBOX_SORT',
          show: true,
        },
      },
      additionalSections: {},
    },
  ],
};
