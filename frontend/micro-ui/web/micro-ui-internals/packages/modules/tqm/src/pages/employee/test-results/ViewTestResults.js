import React, { Fragment } from "react";
import { ViewComposer, Header } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";
import { useLocation } from "react-router-dom";

function ViewTestResults() {
  const { t } = useTranslation();
  const location = useLocation();
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const searchParams = new URLSearchParams(location.search);
  const id = searchParams.get("id");
  const businessService = "PQM";

  const { isLoading, data: testData, revalidate, isFetching } = Digit.Hooks.tqm.useViewTestResults({
    id: id,
    config: {
      select: (data) => ({
        cards: [
          {
            sections: [
              {
                type: "DATA",
                // sectionHeader: { value: "Section 1", inlineStyles: {} },
                cardHeader: { value: "Test Details", inlineStyles: { marginTop: 0 } },
                values: data.details,
              },
              {
                type: "COMPONENT",
                component: "TqmDetailsTable",
                props: {
                  cardHeader: { value: "Test Result", inlineStyles: {} },
                  rowsData: [
                    { slno: 1, qp: "Quality 1", uom: "Some UOM 1", bench: "Some Benchmark 1", results: 1234 },
                    { slno: 2, qp: "Quality 2", uom: "Some UOM 2", bench: "Some Benchmark 2", results: 5678 },
                  ],
                  columnsData: [
                    {
                      Header: "SL. No.",
                      accessor: "slno",
                    },
                    {
                      Header: "Quality Parameter",
                      accessor: "qp",
                    },
                    {
                      Header: "UOM",
                      accessor: "uom",
                    },
                    {
                      Header: "Benchmark",
                      accessor: "bench",
                    },
                    {
                      Header: "Results",
                      accessor: "results",
                    },
                  ],
                  summaryRows: ["", "", "", "Total Sum", "1234"],
                },
              },
              {
                cardHeader: { value: "Documents", inlineStyles: {} },
                type: "COMPONENT",
                component: "TqmDocumentsPreview",
                props: {
                  documents: data.documents,
                },
              },
              {
                cardHeader: { value: "Application Timeline", inlineStyles: {} },
                type: "WFHISTORY",
                businessService: businessService,
                tenantId: tenantId,
                applicationNo: id,
                timelineStatusPrefix: "TQM_TIMELINE_",
                statusAttribute: "status",
              },
            ],
          },
        ],
      }),
    },
  });

  return (
    <>
      <Header> {t("ES_TQM_TEST_RESULTS_DETAILS_HEADER")} </Header>
      {!isLoading && <ViewComposer data={testData} isLoading={isLoading} />}
    </>
  );
}

export default ViewTestResults;
