import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { Link, useHistory } from "react-router-dom";
import { Header, Loader, Toast, ViewComposer } from "@egovernments/digit-ui-react-components";

const TQMSummary = () => {
  const { t } = useTranslation();
  const history = useHistory();
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const searchParams = new URLSearchParams(location.search);
  const id = searchParams.get("id");
  const isMobile = window.Digit.Utils.browser.isMobile();

  const config = {
    select: (data) => ({
      cards: [
        {
          sections: [
            {
              type: "DATA",
              cardHeader: { value: t("ES_TQM_TEST_DETAILS_HEADING"), inlineStyles: { marginTop: 0 } },
              values: data.details,
            },
          ],
        },
        data?.documents?.[0]?.value !== null
          ? {
              sections: [
                {
                  cardHeader: { value: t("ES_TQM_DOCUMENTS_HEADING"), inlineStyles: isMobile ? {} : { marginTop: 0 } },
                  type: "COMPONENT",
                  component: "TqmDocumentsPreview",
                  props: {
                    documents: data?.documents,
                  },
                },
              ],
            }
          : {},
        data?.reading
          ? {
              sections: [
                {
                  type: "COMPONENT",
                  component: "TqmParameterReadings",
                  props: {
                    reading: data?.reading,
                    responseData: data?.testResponse,
                  },
                },
              ],
            }
          : {},
      ],
    }),
  };

  const { isLoading, data: testData, revalidate, isFetching } = Digit.Hooks.tqm.useViewTestSummary({ tenantId, t, id: id, config });

  if (isLoading || !testData) {
    return <Loader />;
  }

  if (testData?.length === 0) {
    history.goBack();
  }

  return (
    <React.Fragment>
      <div className="cardHeaderWithOptions">
        <Header>{t("ES_TQM_SUMMARY_HEADING")}</Header>
      </div>

      {!isLoading && <ViewComposer data={testData} isLoading={isLoading} />}
    </React.Fragment>
  );
};

export default TQMSummary;
