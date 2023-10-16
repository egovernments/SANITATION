import { Notification } from "@egovernments/digit-ui-react-components";
import React from "react";

// just for testing will be removed later
const Test = () => {
  return (
    <div>

      <Notification

        actions={[
          {
            header: "Test Result Not Uploaded",
            eventNotificationText: "This is a notification message1.",
            actionUrl: 'View', code: 'View Test Details',
            timePastAfterEventCreation: "3",
            timeApproxiamationInUnits: "hours"
          },
          {
            header: "Test Result Not Uploaded",
            eventNotificationText: "This is a notification message2.",
            actionUrl: 'View', code: 'View Test Details',
            timePastAfterEventCreation: "3",
            timeApproxiamationInUnits: "hours"
          }
        ]}

      />
    </div>

    // <h1>hello</h1>
  );
};

export default Test;