import { BackButton,HelpOutlineIcon,Label } from '@egovernments/digit-ui-react-components'
import React,{useEffect,useContext} from 'react'
import { useTranslation } from 'react-i18next'
import { TutorialContext } from './Tutorial/TutorialContext'


const TqmHeader = () => {
  const {tutorial,updateTutorial} = useContext(TutorialContext)
  const { t } = useTranslation()

  const startTour = () => {
    updateTutorial({
      type:"updateTourState",
      state:{
        ...tutorial,
        run:true,
        tourActive:true
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