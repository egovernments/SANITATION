import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { Link, useHistory, useLocation, useParams } from "react-router-dom";
import {
  Header,
  Card,
  LinkButton,
  KeyNote,
  Loader,
  MultiLink,
  Row,
  StatusTable,
  CardHeader,
  CardSubHeader,
  ActionBar,
  SubmitBar,
  CardCaption,
  Toast,
} from "@egovernments/digit-ui-react-components";
import DocumentsPreview from "./DocumentsPreview";
import CardMessage from "./CardMessage";
import CardReading from "./CardReadings";

const TQMSummary = () => {
  const { t } = useTranslation();
  // 🚧 WIP: WILL FETCH ID/APPLICATION NUMBER FROM URL 👇
  const { id } = useParams();
  const history = useHistory();
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const [showToast, setShowToast] = useState(null);
  // 🚧 WIP: STATE FOR READINGS AND DOCUMENTS 👇
  const [reading, setReading] = useState(null);
  const [documents, setDocuments] = useState(null);

  // 🚧 WIP: API NEEDS TO CHANGE HERE 👇 USED THIS FOR SCREEN CREATION
  //   const { isLoading, isError, error, data: application, error: errorApplication } = Digit.Hooks.fsm.useApplicationDetail(t, tenantId, id, {}, "CITIZEN");

  if (isLoading || !application) {
    return <Loader />;
  }

  if (application?.applicationDetails?.length === 0) {
    history.goBack();
  }

  const closeToast = () => {
    setShowToast(null);
  };

  return (
    <React.Fragment>
      <div className="cardHeaderWithOptions">
        <Header>{t("Summary")}</Header>
      </div>
      {application?.applicationDetails?.map(({ title, values }, index) => {
        return (
          <Card
            style={{
              position: "relative",
              marginBottom: "16px",
              padding: "16px",
            }}
          >
            {<CardSubHeader>{t(title)}</CardSubHeader>}
            <StatusTable>
              {values?.map(({ title, value }, index) => {
                return (
                  <Row
                    key={t(value)}
                    label={t(title)}
                    text={t(value) || "N/A"}
                    className="border-none"
                    rowContainerStyle={{
                      marginBottom: 0,
                      display: "flex",
                      justifyContent: "space-between",
                    }}
                  />
                );
              })}
            </StatusTable>
          </Card>
        );
      })}

      {/* 🚧 WIP: STRUCTURE DESIGN FOR DOCUMENTS 👇 */}
      {/* [
        {
          title: "adhar",
          value: "d602075a-7078-4cad-ab6d-ab859c1bb70e",
        },
        {
          title: "some",
          value: "a4235581-0137-42ab-81a9-35fe6b1c8f03",
        },
      ] */}

      {documents && documents?.length > 0 ? (
        <Card
          style={{
            position: "relative",
            marginBottom: "16px",
            padding: "16px",
          }}
        >
          <CardSubHeader>{t("Attached Documents")}</CardSubHeader>
          <DocumentsPreview documents={documents} />
        </Card>
      ) : null}

      {/* 🚧 WIP: STRUCTURE FOR READING COMPONENTS 👇*/}
      {/* {
          title: "Treated Effluent Quality Overview",
          date: "28/10/2021",
          readings: [
            {
              title: "pH",
              value: 10,
              pass: true,
            },
            {
              title: "pH",
              value: 10,
              pass: true,
            },
            {
              title: "pH",
              value: 10,
              pass: false,
            },
            {
              title: "pH",
              value: 10,
              pass: false,
            },
          ],
        } */}

      {reading ? (
        <Card
          style={{
            position: "relative",
            marginBottom: "16px",
            padding: "16px",
          }}
        >
          {reading?.title ? <CardSubHeader>{t(reading?.title)}</CardSubHeader> : null}
          {reading?.date ? (
            <CardCaption style={{ display: "flex" }}>
              <p>{t("Result Date")}: </p>
              <p> {reading?.date}</p>
            </CardCaption>
          ) : null}
          {reading?.readings && reading?.readings?.length > 0
            ? reading?.readings?.map(({ title, value, pass }) => <CardReading showInfo={true} success={pass} title={title} value={value} />)
            : null}
          {reading?.readings?.length > 0 ? (
            <CardMessage success={reading?.readings?.map((i) => i.pass).includes(false) ? false : true} title="HEHE" message="djhsdjkhsdjkhsdfjk" />
          ) : null}
          <SubmitBar label={t("Back")} onSubmit={() => history.goBack()} style={{ marginBottom: "12px" }} />
        </Card>
      ) : null}

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
