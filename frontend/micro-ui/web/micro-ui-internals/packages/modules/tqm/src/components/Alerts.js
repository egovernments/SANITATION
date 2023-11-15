import React,{Fragment} from 'react'
import { Card } from '@egovernments/digit-ui-react-components'
import { useTranslation } from "react-i18next"
const alerts =({ale})=> {
  const { t } = useTranslation();
  
  return [
  {
    label:"No reading from Sensor",
    count:ale?.responseData?.data?.[0]?.plots?.[5]?.value || 0
  },
  {
    label:"Device & lab result mismatch",
    count: ale?.responseData?.data?.[0]?.plots?.[4]?.value || 0
  },
  {
    label:t("PQM_RESULTS_NOT_UPTO_BENCHMARK"),
    count:ale?.responseData?.data?.[0]?.plots?.[2]?.value || 0
  },

]}

const Alerts = ({ale}) => {
  const { t } = useTranslation();
  const data = alerts({ale});
  return (
    <Card className={'alerts-container'} style={{paddingLeft:"0px",paddingRight:"0px"}}>
      <div className='alerts-container-header alerts-container-item'>
        <div>
          <p>
          {t("PQM_TEST_ALERTS")}
          </p>
          <p className='alerts-container-subheader'>{t("PQM_TEST_ALERTS_LAST_30_DAYS")}</p>
        </div>
        <div className='alerts-container-count'>{ale?.responseData?.data?.[0]?.plots?.[3]?.value}</div>
      </div>
      
      <div className={'alerts-container'}>
        {data?.map((alert,alertIdx) => {
          return (
            <div className='alerts-container-item'> 
              <p className='alerts-container-item-label'> {alert.label} </p>
              <p className='alerts-container-item-count'>{alert.count} </p> 
            </div>
          )
        })}
      </div>
    </Card>
  )
}

export default Alerts