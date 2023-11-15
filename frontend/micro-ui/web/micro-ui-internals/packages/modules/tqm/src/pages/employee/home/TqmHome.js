import React, { Fragment } from 'react'
import TqmCard from '../../../components/TqmCard'
import Alerts from '../../../components/Alerts'
import YourPerformance from '../../../components/YourPerformance'
import { useTranslation } from "react-i18next";
const TqmHome = (props) => {
  const { t } = useTranslation();
  const endDate = Date.now();
  const startDate = new Date(endDate);
  startDate.setMonth(startDate.getMonth() - 1);
  
  const requestCriteria1 = {
    url: "/dashboard-analytics/dashboard/getChartV2",
    params: {},
    body: {
      "aggregationRequestDto": {
        "visualizationType": "METRIC",
        "visualizationCode": "pqmTestCompliance",
        "queryType": "",
        "filters": {},
        "moduleLevel": "",
        "aggregationFactors": null,
        "requestDate": {
          "startDate": startDate.getTime(),
          "endDate": endDate,
        }
      },
      "headers": {
        "tenantId": "pg.citya"
      }
    },
    changeQueryName: "data1",
    staletime: 0
  };
  const { isLoading, data: data1 } = Digit.Hooks.useCustomAPIHook(requestCriteria1);
  
  const requestCriteria2 = {
    url: "/dashboard-analytics/dashboard/getChartV2",
    params: {},
    body: {
      "aggregationRequestDto": {
        "visualizationType": "METRIC",
        "visualizationCode": "pqmPercentageOfTestResultsMeetingBenchmarks",
        "queryType": "",
        "filters": {},
        "moduleLevel": "",
        "aggregationFactors": null,
        "requestDate": {
          "startDate": startDate.getTime(),
          "endDate": endDate,
        }
      },
      "headers": {
        "tenantId": "pg.citya"
      }
    },
    changeQueryName: "data2",
    staletime: 0
  };
  const { data: data2 } = Digit.Hooks.useCustomAPIHook(requestCriteria2);

  const requestCriteria3 = {
    url: "/dashboard-analytics/dashboard/getChartV2",
    params: {},
    body: {
      "aggregationRequestDto": {
        "visualizationType": "xtable",
        "visualizationCode": "pqmAlertsTest2",
        "queryType": "",
        "filters": {},
        "moduleLevel": "",
        "aggregationFactors": null,
        "requestDate": {
          "startDate": startDate.getTime(),
          "endDate": endDate
        }
      },
      "headers": {
        "tenantId": "pg.citya"
      }
    },
    changeQueryName: "data3",
    staletime: 0
  };
  const { data: data3 } = Digit.Hooks.useCustomAPIHook(requestCriteria3);

  const combinedData = [...(data1 || []), ...(data2 || [])];
  
  return (
    <div className='tqm-home-container'>
      <TqmCard t={t} reRoute={false} />
      <YourPerformance performance={combinedData} />
      <Alerts ale={data3} />
    </div>
  )
}

export default TqmHome