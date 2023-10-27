import React, { useEffect, useState } from "react"
import { useTranslation } from "react-i18next"
import Header from "./Header";
import Button from "./Button";
import { useHistory } from "react-router-dom";
import { CloseSvg, NotificationBell } from "./svgindex";


const Notification = ({ actions }) => {
    const { t } = useTranslation();
    const history = useHistory();
    const [notifications, setNotifications] = useState([]);

    useEffect(() => {
        setNotifications(actions || []);
    }, []);

    const handleClearAll = () => {
        setNotifications([]);
    };

    const handleClearNotification = (index) => {
        const updatedNotifications = [...notifications];
        updatedNotifications.splice(index, 1);
        setNotifications(updatedNotifications);
    };

    return (
        <div className="Container">
            <div className="headerContainer">
                <NotificationBell height={25} width={25} fill="#F47738" className={"bell-icon"} />
                <Header >{t("ES_TQM_NOTIFICATIONS")}</Header>
                <div className="clear-all-link" onClick={handleClearAll}>{t("TQM_CLEAR_SEARCH")}</div>
            </div>
            {notifications.length > 0 &&
                <div className="NotificationItem">
                    <div className="Notification">

                        {notifications.map((item, index) => (
                            <div key={index} className="WhatsNewCard">
                                <div className="NotificationHeader">
                                    <Header>{t(item.header)}</Header>
                                    <CloseSvg onClick={() => handleClearNotification(index)} />
                                </div>
                                <div className="notificationContent">
                                    <div>
                                        <p>{t(item.eventNotificationText)}</p>
                                        <p>{item.timePastAfterEventCreation }</p>
                                    </div>
                                    <div className="button-container">
                                        <Button className={"header-btn viewDetailsButton"} label={t("TQM_VIEW_TEST_DETAILS")} variation="secondary" type="button" 
                                        onButtonClick={() => {
                                            const url = item.actionUrl;
                                            const id = url.split('id=')[1];
                                            history.push(`view-test-results?id=${id}`)
                                        }} />
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>
            }
        </div>

    );
}

export default Notification;