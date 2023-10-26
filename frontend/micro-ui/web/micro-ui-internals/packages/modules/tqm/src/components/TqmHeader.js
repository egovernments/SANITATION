import { BackButton,HelpOutlineIcon,Label } from '@egovernments/digit-ui-react-components'
import React,{useEffect,useContext} from 'react'
import { useTranslation } from 'react-i18next'
import { TutorialContext } from './Tutorial/TutorialContext'
import { useLocation } from "react-router-dom";



const TqmHeader = () => {
  const {tutorial,updateTutorial} = useContext(TutorialContext)
  const { t } = useTranslation()
  //using location.pathname we can update the stepIndex accordingly when help is clicked from any other screen(other than home screen)
  const location = useLocation()

  const startTour = () => {
    updateTutorial({
      type:"updateTourState",
      state:{
        ...tutorial,
        run:true,
        tourActive:true,
        stepIndex:0
      }
    })
  }

  return (
    <div className='tqm-header'> 
      <BackButton>{t("CS_COMMON_BACK")}</BackButton>
      <div className='header-icon-container' onClick={startTour}>
        <Label>{t("Help")}</Label>
        <HelpOutlineIcon />
      </div>
    </div>
  )
}

export default TqmHeader