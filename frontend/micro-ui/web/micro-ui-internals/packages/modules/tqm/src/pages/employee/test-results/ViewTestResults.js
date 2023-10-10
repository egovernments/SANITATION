import React, { Fragment, useState } from "react";
import { ViewComposer, Toast, WorkflowTimeline } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";

// 🚧 WIP: DUMMY DATA FOR DETAILS 👇
const data = {
  cards: [
    {
      sections: [
        {
          type: "DATA",
          // sectionHeader: { value: "Section 1", inlineStyles: {} },
          cardHeader: { value: "Card 2", inlineStyles: { marginTop: 0 } },
          values: [
            {
              key: "key 1",
              value: "value 1",
            },
            {
              key: "key 2",
              value: "value 2",
            },
            {
              key: "key 3",
              value: "value 3",
            },
          ],
        },
        {
          cardHeader: { value: "Test Result", inlineStyles: {} },
          type: "COMPONENT",
          component: "TqmDetailsTable",
          props: {
            detail: [
              { id: 1, key: 1, slno: 1, qp: "Quality 1", uom: "Some UOM 1", bench: "Some Benchmark 1", results: 1234 },
              { id: 2, key: 2, slno: 2, qp: "Quality 2", uom: "Some UOM 2", bench: "Some Benchmark 2", results: 5678 },
              { id: 2, key: 2, slno: 2, qp: "Quality 2", uom: "Some UOM 2", bench: "Some Benchmark 2", results: 5678 },
            ],
            rowsData: [
              { id: 1, key: 1, slno: 1, qp: "Quality 1", uom: "Some UOM 1", bench: "Some Benchmark 1", results: 1234 },
              { id: 2, key: 2, slno: 2, qp: "Quality 2", uom: "Some UOM 2", bench: "Some Benchmark 2", results: 5678 },
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
            documents: [{ value: "e7ebc21b-d5c5-44fa-b11a-a63d5057af87" }, { value: "ce605bc2-4309-4f19-8635-aee160502963" }],
          },
        },
      ],
    },
  ],
  apiResponse: {},
  additionalDetails: {},
};

function ViewTestResults() {
  const { t } = useTranslation();
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const businessService = "PQM";
  const applicationNo = "";

  return (
    <>
      <ViewComposer data={data} isLoading={false} />
      <WorkflowTimeline businessService={businessService} tenantId={tenantId} applicationNo={applicationNo} timelineStatusPrefix="TQM_TIMELINE_" statusAttribute="status" />
    </>
  );
}

export default ViewTestResults;
