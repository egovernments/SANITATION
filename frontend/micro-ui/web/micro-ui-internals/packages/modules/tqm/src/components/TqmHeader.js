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

const excludeBackBtn = [
  'landing'
]

const TqmHeader = ({location,defaultPath}) => {
  const history = useHistory()
  const { tourState, setTourState } = useTourState();
  const pathVar=location.pathname.replace(defaultPath+'/',"").split("?")?.[0];
  
  // const { tutorial, updateTutorial } = useContext(TutorialContext);
  const { t } = useTranslation();
  //using location.pathname we can update the stepIndex accordingly when help is clicked from any other screen(other than home screen)
  const { pathname } = useLocation();
  const startTour = () => {
    history.push(`/${window.contextPath}/employee/tqm/how-it-works`)
  };

  return (
    <>
      <Tutorial tutorial={tourState} updateTutorial={setTourState} />

      <div className="tqm-header">
        {!pathVar?.includes(excludeBackBtn) ?  <BackButton>{t('CS_COMMON_BACK')}</BackButton> : <div></div>}

        <Help startTour={startTour} />
      </div>
    </>
  );
};

export default TqmHeader;
