export const useCustomMDMSV2 = ({ tenantId, schemaCode, select,filters={},config={} }) => {
  
  const requestCriteria = {
    url: "/mdms-v2/v2/_search",
    body: {
      tenantId,
      MdmsCriteria: {
        tenantId: tenantId,
        filters: filters,
        schemaCode: schemaCode,
        isActive: true
      },
    },
    config: {
      select: select
        ? select
        : (response) => {
            //mdms will be an array of master data
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
          ...config
    },
  };
  const { isLoading, data } = Digit.Hooks.useCustomAPIHook(requestCriteria);
  return { isLoading, data };
};
