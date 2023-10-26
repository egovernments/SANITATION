
// lets put all the steps for a module in this array, set this array from the index.js file of a module (currently sitting here for testing)
// Since it's a controlled Tutorial we can use a different stepIndex for every page, this way we can do multiRoute as well as each page's tutorial in one go
//When a user goes to any page and clicks on help, the tutorial will start from a particular stepIndex
const steps = [
  {
    content:"this is my content1",
    target:".tiles-card-container",
    disableBeacon:true,
    redirectTo:`/sanitation-ui/employee/tqm/home`
    // placement:"center"
  },
  {
    content:"this is my content2",
    target:".complaint-links-container",
    disableBeacon:true,
    // placement:"center"
  },

]

export const initialTutorialState = {
  run: false,
  stepIndex: 0,
  steps,
  tourActive: false,
}

export const TutorialReducer = (state, action) => {
  switch (action.type) {
    case "updateTourState":
      return {...action.state};
  }
}


