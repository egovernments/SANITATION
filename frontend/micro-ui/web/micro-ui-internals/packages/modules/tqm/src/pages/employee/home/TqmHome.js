import React,{Fragment} from 'react'
import TqmCard from '../../../components/TqmCard'
import Alerts from '../../../components/Alerts'
import YourPerformance from '../../../components/YourPerformance'
import { useTranslation } from "react-i18next";
const TqmHome = (props) => {
  const { t } = useTranslation();
  return (
    <div className='tqm-home-container'>
      <TqmCard t={t} reRoute={false}/>
      <YourPerformance t={t} />
      <Alerts t={t} />
    </div>
  )
}

export default TqmHome