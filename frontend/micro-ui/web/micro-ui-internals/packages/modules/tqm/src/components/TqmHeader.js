import {
  BackButton,
  HelpOutlineIcon,
  Label,
  Tutorial,
} from '@egovernments/digit-ui-react-components';
import React, { useEffect, useContext,Fragment } from 'react';
import { useTranslation } from 'react-i18next';
import { useLocation } from 'react-router-dom';
// import { Tour } from '../utils/TourSteps';
import { TourSteps } from '../utils/TourSteps';
import { useTourState } from './Tutorial/TourProvider';


const mappingScreensToStepIndex = {
  '/sanitation-ui/employee/tqm/landing': 0,
  '/sanitation-ui/employee/tqm/home': 3, //will start from 3,
  '/sanitation-ui/employee/tqm/inbox': 7,
};

const TqmHeader = () => {
  const { tourState, setTourState } = useTourState()
  // const { tutorial, updateTutorial } = useContext(TutorialContext);
  const { t } = useTranslation();
  //using location.pathname we can update the stepIndex accordingly when help is clicked from any other screen(other than home screen)
  const { pathname } = useLocation();

  const startTour = () => {
    setTourState(
      {
        run: true,
        steps:TourSteps[pathname],
        tourActive: true,
      }
    )
  };

  return (
    <>
      <Tutorial tutorial={tourState} updateTutorial={setTourState} />

      <div className="tqm-header">
        <BackButton>{t('CS_COMMON_BACK')}</BackButton>
        {/* {location.pathname.includes('/tqm/landing') && (
          <div className="header-icon-container" onClick={startTour}>
            <Label>{t('Help')}</Label>
            <HelpOutlineIcon />
          </div>
        )} */}
        
          <div className="header-icon-container" onClick={startTour}>
            <Label>{t('Help')}</Label>
            <HelpOutlineIcon />
          </div>
        
      </div>
    </>
  );
};

export default TqmHeader;
