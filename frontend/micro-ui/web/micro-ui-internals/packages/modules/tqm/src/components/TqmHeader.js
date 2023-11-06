import {
  BackButton,
  HelpOutlineIcon,
  Label,
  Tutorial,
  useTourState,
  Help,
} from '@egovernments/digit-ui-react-components';
import React, { useEffect, useContext, Fragment } from 'react';
import { useTranslation } from 'react-i18next';
import { useLocation,useHistory } from 'react-router-dom';

import { TourSteps } from '../utils/TourSteps';


const TqmHeader = () => {
  const history = useHistory()
  const { tourState, setTourState } = useTourState();
  // const { tutorial, updateTutorial } = useContext(TutorialContext);
  const { t } = useTranslation();
  //using location.pathname we can update the stepIndex accordingly when help is clicked from any other screen(other than home screen)
  const { pathname } = useLocation();
  const startTour = () => {
    history.push(`/${window.contextPath}/citizen/tqm-how-it-works`)
  };

  return (
    <>
      <Tutorial tutorial={tourState} updateTutorial={setTourState} />

      <div className="tqm-header">
        <BackButton>{t('CS_COMMON_BACK')}</BackButton>

        <Help startTour={startTour} />
      </div>
    </>
  );
};

export default TqmHeader;
