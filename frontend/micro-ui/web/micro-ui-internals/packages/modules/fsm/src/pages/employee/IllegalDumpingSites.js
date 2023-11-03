import React from "react";
import {Header} from "@egovernments/digit-ui-react-components";

const IllegalDumpingSites = () => {
  return (
    <React.Fragment>
      <Header>{"Illegal Dumping Sites"}</Header>
      <div className="app-iframe-wrapper">
        <iframe src={"http://myflutterapp.local:8080/#/map2"} title={"title"} className="app-iframe" />
      </div>
    </React.Fragment>
  );
};

export default IllegalDumpingSites;
