import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { Link, useHistory, useLocation, useParams } from "react-router-dom";
import { Header, Loader, Toast, ViewComposer } from "@egovernments/digit-ui-react-components";
import DocumentsPreview from "./DocumentsPreview";
import CardMessage from "./CardMessage";
import CardReading from "./CardReadings";

const TQMSummary = () => {
  const { t } = useTranslation();
  const history = useHistory();
  const [showToast, setShowToast] = useState(null);
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const searchParams = new URLSearchParams(location.search);
  const id = searchParams.get("id");
  const businessService = "PQM";

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
        data.documents
          ? {
              sections: [
                {
                  cardHeader: { value: t("ES_TQM_DOCUMENTS_HEADING"), inlineStyles: {} },
                  type: "COMPONENT",
                  component: "TqmDocumentsPreview",
                  props: {
                    documents: data.documents,
                  },
                },
              ],
            }
          : {},
        data.reading
          ? {
              sections: [
                {
                  type: "COMPONENT",
                  component: "TqmParameterReadings",
                  props: {
                    reading: data.reading,
                  },
                },
              ],
            }
          : {},
      ],
    }),
  };

  const { isLoading, data: testData, revalidate, isFetching } = Digit.Hooks.tqm.useViewTestSummary({ id: id, config });

  if (isLoading || !testData) {
    return <Loader />;
  }

  if (testData?.length === 0) {
    history.goBack();
  }

  const closeToast = () => {
    setShowToast(null);
  };

  return (
    <React.Fragment>
      <div className="cardHeaderWithOptions">
        <Header>{t("ES_TQM_SUMMARY_HEADING")}</Header>
      </div>

      {!isLoading && <ViewComposer data={testData} isLoading={isLoading} />}

      {showToast && (
        <Toast
          error={showToast.key === "error" ? true : false}
          label={t(showToast.key === "success" ? `ES_TQM_STATUS_UPDATED_SUCCESSFULLY` : showToast.action)}
          onClose={closeToast}
          isDleteBtn={true}
        />
      )}
    </React.Fragment>
  );
};

export default TQMSummary;
