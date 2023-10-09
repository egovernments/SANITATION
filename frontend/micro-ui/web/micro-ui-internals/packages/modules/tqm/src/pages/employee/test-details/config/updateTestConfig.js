export const updateConfig = ({ t, options }) => {
  return [
    {
      head: t("ES_TQM_SELECT_SAMPLE_TO_LAB_LABEL"),
      body: [
        {
          label: t("ES_TQM_SELECT_LAB_LABEL"),
          isMandatory: true,
          key: "status",
          type: "dropdown",
          disable: false,
          populators: {
            name: "status",
            optionsKey: "name",
            error: t("ES_TQM_SELECT_LAB_LABEL_ERROR"),
            required: true,
            // 🚧 WIP: DUMMY DATA FOR UPDATE STATUS 👇
            options: [{ name: "djhsdkj" }, { name: "34734786" }],
            // 🚧 WIP: way to call option from mdms 👇
            //   mdmsConfig: {
            //     masterName: "ProjectType",
            //     moduleName: "works",
            //     localePrefix: "COMMON_MASTERS"
            // }
          },
        },
      ],
    },
  ];
};
