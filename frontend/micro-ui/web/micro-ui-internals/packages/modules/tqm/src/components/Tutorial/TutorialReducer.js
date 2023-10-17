export const initialTutorialState = {
  run: false,
  stepIndex: 0,
  steps: [
    {
      content:"this is my content",
      target:".tiles-card-container",
      disableBeacon:true
    }
  ],
  tourActive: false,
}

const reducer = (state, action) => {
  switch (action.type) {
    case "updateTourState":
      return {...action.state};
  }
}

export default reducer;
