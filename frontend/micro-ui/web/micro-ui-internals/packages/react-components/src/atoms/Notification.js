import React, { useEffect, useState } from "react"
import { useTranslation } from "react-i18next"
import Header from "./Header";
import Button from "./Button";
import { useHistory, Link } from "react-router-dom";
import { CloseSvg, NotificationBell } from "./svgindex";


const Notification = ({ actions }) => {
    const { t } = useTranslation();
    const history = useHistory();
    const [notifications, setNotifications] = useState([]);
    const [showall, setshowall] = useState(false);

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

    const toggleShowAll = () => {
        setshowall(!showall);
    }

    const displayNotifications = showall ? notifications : notifications.slice(0, 5);

    return (
        <div className="Container">
            <div className="headerContainer">
                <NotificationBell height={25} width={25} fill="#F47738" className={"bell-icon"} />
                <Header >{t("ES_TQM_NOTIFICATIONS")}</Header>
                <div className="clear-all-link" onClick={handleClearAll}>{t("ES_COMMON_CLEAR_ALL")}</div>
            </div>
            {displayNotifications.length > 0 &&
                <div className="NotificationItem">
                    <div className="Notification">

                        {displayNotifications.map((item, index) => (
                            <div key={index} className="WhatsNewCard">
                                <div className="NotificationHeader">
                                    <h1>{t(item.header)}</h1>
                                    <CloseSvg onClick={() => handleClearNotification(index)} />
                                </div>
                                <div className="notificationContent">
                                    <div>
                                        <p>{t(item.eventNotificationText)}</p>
                                        <p>{item.timePastAfterEventCreation}</p>
                                    </div>
                                    <div className="button-container">
                                    <Link to={`/${window.contextPath}/employee/tqm/view-test-results?id=${item.actionUrl.split('id=')[1]}`}>
                                        <Button className={"header-btn viewDetailsButton"} label={t("VIEW_TEST_DETAILS")} variation="secondary" type="button"/>
                                    </Link>
                                    </div>
                                </div>
                            </div>
                        ))}
                        {notifications.length > 5 && (
                            <div className="view-all-button-container" onClick={toggleShowAll}>
                                <button className="view-all-button">
                                    {showall ? t("ES_SHOW_LESS") : t("DASHBOARD_VIEW_ALL_LABEL")}
                                </button>
                            </div>
                        )}
                    </div>
                </div>
            }
        </div>

    );
}

export default Notification;