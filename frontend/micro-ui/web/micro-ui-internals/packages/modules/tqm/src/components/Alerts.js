import React,{Fragment} from 'react'
import { Card } from '@egovernments/digit-ui-react-components'

const alerts =  [
  {
    label:"No reading from Sensor",
    count:5
  },
  {
    label:"Device & lab result mismatch",
    count:4
  },
  {
    label:"Results not upto benchmark",
    count:6
  },

]

const Alerts = ({t}) => {
  return (
    <Card className={'alerts-container'} style={{paddingLeft:"0px",paddingRight:"0px"}}>
      <div className='alerts-container-header alerts-container-item'>
        <div>
          <p>
            Alerts
          </p>
          <p className='alerts-container-subheader'>Alerts raised in the past 30 days</p>
        </div>
        <div className='alerts-container-count'>5</div>
      </div>

      <div className={'alerts-container'}>
        {alerts?.map((alert,alertIdx) => {
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