import React, { Fragment } from "react";
import { ViewComposer, Header, Loader } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";
import { useLocation } from "react-router-dom";

function ViewTestResults() {
  const { t } = useTranslation();
  const location = useLocation();
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const searchParams = new URLSearchParams(location.search);
  const id = searchParams.get("id");
  const type = searchParams.get("type");
  const businessService = "PQM";

  const { isLoading, data: testData, revalidate, isFetching } = Digit.Hooks.tqm.useViewTestResults({
    t,
    tenantId: tenantId,
    id: id,
    type: type,
    config: {
      select: (data) => ({
        cards: [
          {
            sections: [
              {
                type: "DATA",
                cardHeader: { value: "Test Details", inlineStyles: { marginTop: 0 } },
                values: data?.details,
              },
              data?.tableData
                ? {
                    type: "COMPONENT",
                    component: "TqmDetailsTable",
                    props: {
                      cardHeader: { value: "Test Result", inlineStyles: {} },
                      rowsData: data?.tableData.map((i, index) => {
                        return {
                          slno: index + 1,
                          qp: i.qparameter,
                          uom: i.uom,
                          bench: i.benchmarkValues,
                          results: i.results,
                          status: i.status,
                        };
                      }),
                      columnsData: [
                        {
                          Header: t("ES_TQM_SNO"),
                          accessor: "slno",
                        },
                        {
                          Header: t("ES_TQM_QUALITY_PARAMETER"),
                          accessor: "qp",
                        },
                        {
                          Header: t("ES_TQM_UOM"),
                          accessor: "uom",
                        },
                        {
                          Header: t("ES_TQM_BENCHMARK"),
                          accessor: "bench",
                        },
                        {
                          Header: t("ES_TQM_RESULTS"),
                          accessor: "results",
                          Cell: ({ row }) => {
                            return <span className={row?.original?.status === "PASS" ? "sla-cell-success" : "sla-cell-error"}>{row?.original?.results}</span>;
                          },
                        },
                      ],
                      summaryRows: data?.testSummary,
                    },
                  }
                : {},
              data?.documents?.[0]?.value
                ? {
                    cardHeader: { value: "Documents", inlineStyles: {} },
                    type: "COMPONENT",
                    component: "TqmDocumentsPreview",
                    props: {
                      documents: data?.documents,
                    },
                  }
                : {},
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
      staleTime: 0,
      cacheTime: 0,
    },
  });

  if (isLoading) {
    return <Loader />;
  }

  return (
    <>
      <Header> {t("ES_TQM_TEST_RESULTS_DETAILS_HEADER")} </Header>
      {!isLoading && <ViewComposer data={testData} isLoading={isLoading} />}
    </>
  );
}

export default ViewTestResults;
