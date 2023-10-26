import React, { useState } from 'react';
import Joyride, { ACTIONS, EVENTS, LIFECYCLE, STATUS } from 'react-joyride';
import { useHistory } from 'react-router-dom';

const theme = {
  primaryColor: '#ad7bff',
  arrowColor: '#000',
  textColor: '#fff',
};

const Tutorial = ({ tutorial, updateTutorial, ...props }) => {
  const history = useHistory()
  const { run, stepIndex, steps } = tutorial;
  //currently writing this fn here, this fn will be custom for every module so accept it as props
  const handleCallback = (data) => {
    const { action, type, lifecycle, size, index, status } = data;
    const currentStep = tutorial?.steps?.[index]
    debugger
    // console.log('here', ACTIONS, EVENTS, LIFECYCLE, STATUS);

    if (type === 'step:after') {
      if (action === 'next') {
        updateTutorial({
          type: 'updateTourState',
          state: {
            ...tutorial,
            stepIndex: tutorial.stepIndex + 1,
          },
        });
        if(currentStep.redirectTo){
          history.push(currentStep.redirectTo)
        }
      } else if (action === 'skip') {
        updateTutorial({
          type: 'updateTourState',
          state: {
            ...tutorial,
            stepIndex: tutorial.stepIndex + 1,
          },
        });
      } else if (action === 'prev') {
        updateTutorial({
          type: 'updateTourState',
          state: {
            ...tutorial,
            stepIndex: tutorial.stepIndex - 1,
          },
        });
      } else if (action === 'close') {
        updateTutorial({
          type: 'updateTourState',
          state: {
            ...tutorial,
            run: false,
            tourActive: false,
            stepIndex: 0,
          },
        });
      }
    }

    if (type === 'tour:end') {
      updateTutorial({
        type: 'updateTourState',
        state: {
          ...tutorial,
          run: false,
          tourActive: false,
          stepIndex: 0,
        },
      });
    }
  };

  return (
    <Joyride
      callback={handleCallback}
      continuous
      run={run}
      stepIndex={stepIndex}
      steps={steps}
      styles={{
        options: {
          arrowColor: theme.arrowColor,
          backgroundColor: theme.arrowColor,
          primaryColor: theme.primaryColor,
          textColor: theme.textColor,
        },
      }}
      showProgress={true}
    />
  );
};

export default Tutorial;
