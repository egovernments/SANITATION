import React,{useContext} from 'react'
import Joyride from 'react-joyride';
import { TutorialContext } from './TutorialContext';

const theme = {
  primaryColor:"#ad7bff",
  arrowColor:"#000",
  textColor:"#fff"
}


const Tutorial = ({...props}) => {
  
  const {tutorial,updateTutorial} = useContext(TutorialContext)
  
  const {run,stepIndex,steps}  = tutorial;
  const handleCallback = (data) => {
    
  }

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
  />
  )
}

export default Tutorial