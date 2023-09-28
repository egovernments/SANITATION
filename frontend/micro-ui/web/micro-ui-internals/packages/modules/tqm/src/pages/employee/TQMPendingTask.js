import React, { useState } from "react";
import { NotificationComponent } from "@egovernments/digit-ui-react-components";

function TQMPendingTask(props) {
  const [data, setData] = useState();
  // 🚧 WIP: CODE WILL BE ADDED FOR FETCHING TASKS 👇
  
  return <NotificationComponent heading="Pending Tasks" data={data} />;
}

export default TQMPendingTask;
