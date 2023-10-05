import React, { useState } from "react";
import { NotificationComponent, TreatmentQualityIcon } from "@egovernments/digit-ui-react-components";

function TQMPendingTask(props) {
  // 🚧 WIP: DUMMY DATA IS ADDED HERE 👇
  const [data, setData] = useState([
    {
      icon: <TreatmentQualityIcon />,
      title: "Submit sample for testing",
      action: "VIEW_DETAILS",
      date: "5",
    },
    {
      icon: <TreatmentQualityIcon />,
      title: "Submit sample for testing",
      action: "VIEW_DETAILS",
      date: "5",
    },
    {
      icon: <TreatmentQualityIcon />,
      title: "Submit sample for testing",
      action: "VIEW_DETAILS",
      date: "5",
    },
    {
      icon: <TreatmentQualityIcon />,
      title: "Submit sample for testing",
      action: "VIEW_DETAILS",
      date: "5",
    },
  ]);
  // 🚧 WIP: CODE WILL BE ADDED FOR FETCHING TASKS 👇

  return <NotificationComponent heading="Pending Tasks" data={data} />;
}

export default TQMPendingTask;
