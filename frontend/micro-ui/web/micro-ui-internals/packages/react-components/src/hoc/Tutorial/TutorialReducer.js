
// lets put all the steps for a module in this array, set this array from the index.js file of a module (currently sitting here for testing)
// Since it's a controlled Tutorial we can use a different stepIndex for every page, this way we can do multiRoute as well as each page's tutorial in one go
//When a user goes to any page and clicks on help, the tutorial will start from a particular stepIndex
const steps = [
  {
    content:"TOUR_STEP_0", // Welcome plant operator this is your tqm landing screen
    target:".app-container",
    disableBeacon:true,
    placement:"center",
  },
  {
    content:"TOUR_STEP_1", // this is your pending tasks you can click on the action button to open test's view screen and clicking on all tasks will route to the inbox screen
    target:".notification",
    disableBeacon:true,
    // placement:"center"
  },
  {
    content:"TOUR_STEP_2", // all the cards available in your landing screen, click on the tqm card to go to the home screen of tqm
    target:".home-card-tiles-container",
    disableBeacon:true,
    redirectTo:`/sanitation-ui/employee/tqm/home`,
    // placement:"center"
  },
  //starting point of home page index 3
  {
    content:"TOUR_STEP_3",
    // target:".tqm-home-container",
    target:".complaint-links-container",
    disableBeacon:true,
    placement:"auto",
    hideBackButton:true
  },
  {
    content:"TOUR_STEP_4",
    target:".complaint-links-container",
    disableBeacon:true,
    // placement:"center"
  },
  {
    content:"TOUR_STEP_5",
    target:".performance-container",
    disableBeacon:true,
    // placement:"center"
  },
  {
    content:"TOUR_STEP_6",
    target:".alerts-container",
    disableBeacon:true,
    redirectTo:`/sanitation-ui/employee/tqm/inbox`,
    // placement:"center"
  },
  //starting point of inbox
  {
    content:"TOUR_STEP_7",
    target:".employee-app-wrapper",
    disableBeacon:true,
    placement:"center",
    hideBackButton:true
  },
  {
    content:"TOUR_STEP_8",
    target:".employee-app-wrapper",
    disableBeacon:true,
    placement:"center",
    hideBackButton:true
  },
  

]

export const initialTutorialState = {
  run: false,
  stepIndex: 0,
  // steps,
  tourActive: true,
}

export const TutorialReducer = (state, action) => {
  switch (action.type) {
    case "updateTourState":
      return {...action.state};
  }
}


