import React, { Fragment } from "react";
import Card from "./Card";
import CardHeader from "./CardHeader";
import ButtonSelector from "./ButtonSelector";
import CardText from "./CardText";
import ActionLinks from "./ActionLinks";
import { useTranslation } from "react-i18next";

function NotificationComponent(props) {
  const { t } = useTranslation();
  return (
    <Card className="notification">
      <CardHeader>{props?.heading}</CardHeader>
      {props?.data?.length > 0 ? (
        props?.data?.map((i, index) =>
          index < 3 ? (
            <>
              <div className="notification-flex-container">
                <div className="icon">{i?.icon}</div>
                <CardText className="label">{t(i?.title)}</CardText>
                <ButtonSelector theme="secondary" label={t(`ES_LABEL_${i?.action ? i?.action : ""}`)} />
              </div>
              <span className="sla-cell-success">{t(`ES_TQM_SLA_DUE_NO_DAYS`, { NO_OF_DAYS: i?.date })}</span>
              <hr className="break-line" />
            </>
          ) : (
            index === 3 && <ActionLinks>{t(`ES_VIEW_ALL_PENDING_TASKS`)}</ActionLinks>
          )
        )
      ) : (
        <CardText className="label">{t("ES_TQM_NO_PENDING_TASK")}</CardText>
      )}
    </Card>
  );
}

export default NotificationComponent;
