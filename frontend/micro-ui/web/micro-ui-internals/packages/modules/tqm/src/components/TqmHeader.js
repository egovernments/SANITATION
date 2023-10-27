import {
  BackButton,
  HelpOutlineIcon,
  Label,
} from '@egovernments/digit-ui-react-components';
import React, { useEffect, useContext } from 'react';
import { useTranslation } from 'react-i18next';
import { TutorialContext } from './Tutorial/TutorialContext';
import { useLocation } from 'react-router-dom';
import { Tour } from '../utils/TourSteps';

const mappingScreensToStepIndex = {
  '/sanitation-ui/employee/tqm/landing': 0,
  '/sanitation-ui/employee/tqm/home': 3, //will start from 3,
  '/sanitation-ui/employee/tqm/inbox': 7,
};

const TqmHeader = () => {
  const { updateTest, viewTest } = Tour;
  const { tutorial, updateTutorial } = useContext(TutorialContext);
  const { t } = useTranslation();
  //using location.pathname we can update the stepIndex accordingly when help is clicked from any other screen(other than home screen)
  const { pathname } = useLocation();

  const startTour = () => {
    updateTutorial({
      type: 'updateTourState',
      state: {
        steps: updateTest,
        run: true,
        // stepIndex:mappingScreensToStepIndex[pathname] || 0
        stepIndex: 0,
      },
    });
  };

  return (
    <div className="tqm-header">
      <BackButton>{t('CS_COMMON_BACK')}</BackButton>
      {location.pathname.includes('/tqm/landing') && (
        <div className="header-icon-container" onClick={startTour}>
          <Label>{t('Help')}</Label>
          <HelpOutlineIcon />
        </div>
      )}
    </div>
  );
};

export default TqmHeader;
