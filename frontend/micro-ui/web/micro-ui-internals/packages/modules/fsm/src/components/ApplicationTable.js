import React, { Fragment } from "react";
import { useTranslation } from "react-i18next";

function formatDate(dateString) {
  const date = new Date(dateString);

  // Format date as DD/MM/YY
  const formattedDate = date.toLocaleDateString("en-GB", { day: "2-digit", month: "2-digit", year: "2-digit" });

  return dateString ? formattedDate : null;
}

function formatTime(dateString) {
  const date = new Date(dateString);

  // Format time as hh:mm
  const formattedTime = date.toLocaleTimeString("en-US", { hour12: false, hour: "2-digit", minute: "2-digit" });

  return dateString ? formattedTime : null;
}

export const ApplicationTable = ({ detail }) => {
  const { t } = useTranslation();
  console.log("detail", detail);
  // const tripData = detail?.flatMap((i) => i?.tripDetails);
  const filterData = detail?.map((i) => ({
    tripid: i?.id || "N/A",
    tripstatus: i?.status || "N/A",
    startdate: formatDate(i?.actualStartTime) || "N/A",
    starttime: formatTime(i?.actualStartTime) || "N/A",
    endtime: formatTime(i?.actualEndTime) || "N/A",
    endtype: i?.tripEndType || "N/A",
    alerts: i?.alerts?.split(",")?.length || "0",
    route: i?.routeId || "N/A",
  }));

  const headers = [
    { key: "tripid", label: "Trip ID" },
    { key: "tripstatus", label: "Trip Status" },
    { key: "startdate", label: "Start Date" },
    { key: "starttime", label: "Start Time" },
    { key: "endtime", label: "End Time" },
    { key: "endtype", label: "End Type" },
    { key: "alerts", label: "Alerts" },
    { key: "route", label: "Route" },
  ];

  let rowData = [];

  return (
    <div className="fsm-table-alerts-container">
      {/* Here Render the table for adjustment amount details detail.isTable is true for that table*/}
      <table className="table">
        <thead>
          <tr className="row">
            {headers.map((header) => (
              <>
                <th className="head">{t(header?.label)}</th>
                {/* <hr className="underline" /> */}
              </>
            ))}
          </tr>
        </thead>

        <tbody>
          {filterData?.map((row, index) => {
            // if (index === filterData.length - 1) {
            //   return (
            //     <>
            //       <hr style={{ width: "370%", marginTop: "15px" }} className="underline" />
            //       <tr className="row">
            //         {Object.keys(row).map((key, colIndex) => (
            //           <td style={{ textAlign: "left" }}>{t(row[key])}</td>
            //         ))}
            //       </tr>
            //     </>
            //   );
            // }
            return (
              <>
                <tr className="row">
                  {Object.keys(row).map((key, colIndex) => (
                    <td className="data">
                      {key === "tripstatus" ? (
                        <span className={row[key] === "Completed" ? "sla-cell-success" : row[key] === "Ongoing" ? "sla-cell-warning" : "sla-cell-error"}>{t(row[key] || "NA")}</span>
                      ) : (
                        row[key]
                      )}
                      {console.log("row[key]", row[key])}
                    </td>
                  ))}
                </tr>
                {/* <hr className="underline" /> */}
              </>
            );
          })}
        </tbody>
      </table>
    </div>
  );
};
